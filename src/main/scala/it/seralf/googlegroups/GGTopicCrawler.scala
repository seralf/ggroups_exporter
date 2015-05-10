package it.seralf.googlegroups

import java.net.URL
import org.jsoup.Jsoup
import scala.collection.JavaConversions._
import org.slf4j._
import org.jsoup.nodes._
import com.typesafe.config._
import java.io.File

class GGTopicCrawler(conf: Config) {

  val logger = LoggerFactory.getLogger(this.getClass)

  val timeout = conf.getInt("ggroup.timeout")

  val repo_cache = new FileRepository(conf)

  def parse(url: URL): GGTopic = {
    val ggroup_name = url.getPath.replaceAll("""\/d\/topic\/(.+)\/(.+)$""", "$1")
    val topic_id = url.getPath.replaceAll("""\/d\/topic\/(.+)\/(.+)$""", "$2")
    parse(ggroup_name, topic_id)
  }

  def parse(ggroup_name: String, topic_id: String): GGTopic = {

    val topic_url = new URL(s"https://groups.google.com/forum/?_escaped_fragment_=topic/${ggroup_name}/${topic_id}")
    logger.debug("parsing TOPIC url {}", topic_url)

    val doc = Jsoup.parse(topic_url, timeout)

    def msgs = doc.select("tr td.subject").par.toStream
      .flatMap {
        msg =>
          try {
            val msg_meta = getMsgMeta(ggroup_name, topic_id, msg)
            val msg_id = msg_meta.msg_id

            if (!repo_cache.msg_exists(ggroup_name, topic_id, msg_id)) {
              val message = GGMessageCrawler.parse(ggroup_name, topic_id, msg_meta)
              repo_cache.msg_save(ggroup_name, topic_id, msg_id, message.raw)
              Some(message)
            } else {
              logger.debug("skipping existing message: {}", msg_meta.original_url)
              None
            }

          } catch {
            case e: Throwable =>
              logger.error(s"ERROR with url $topic_url\n {}", e.getMessage)
              None
          }

      }

    GGTopic(topic_id, topic_url, msgs)
  }

  private def getMsgMeta(ggroup: String, topic_id: String, msg: Element): GGMsgMeta = {
    val a = msg.select("a")
    val href = a.attr("abs:href")
    val author = msg.parent().select(".author span").text()
    val msg_id = href.replaceAll(s"""https://groups.google.com/d/msg/$ggroup/$topic_id/(.*?)""", "$1")
    GGMsgMeta(href, author, msg_id)
  }

}
