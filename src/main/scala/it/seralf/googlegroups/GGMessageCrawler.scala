package it.seralf.googlegroups

import org.jsoup.nodes.Element
import java.net.URL
import scala.io.Source
import org.slf4j._
import org.jsoup.Jsoup
import scala.collection.JavaConversions._

object GGMessageCrawler {

  val logger = LoggerFactory.getLogger(this.getClass)

  // TODO: think about caching
  // REDO: remove meta, leave only msg_id (at the moment it's used for debug, it can be handled elsewhere)

  def parse(ggroup_name: String, topic_id: String, meta: GGMsgMeta): GGMessage = {

    val msg_raw_url = new URL(s"https://groups.google.com/forum/message/raw?msg=${ggroup_name}/${topic_id}/${meta.msg_id}")
    logger.debug("parsing message url: {}", msg_raw_url)

    val msg_id = meta.msg_id
    val content = openMessage(msg_raw_url)

    GGMessage(ggroup_name, topic_id, msg_id, content, meta)
  }

  private def openMessage(msg_url: URL, encoding: String = "UTF-8"): String = {
    val src = Source.fromURL(msg_url)(encoding)
    val content = src.mkString
    src.close()
    content
  }

}