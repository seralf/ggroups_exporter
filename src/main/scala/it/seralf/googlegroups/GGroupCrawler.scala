package it.seralf.googlegroups

import java.net.URL
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import scala.collection.JavaConversions._
import java.net.UnknownHostException
import org.slf4j._

import com.typesafe.config.Config

class GGroupCrawler(ggroup_name: String, conf: Config) {

  val logger = LoggerFactory.getLogger(this.getClass)

  val timeout = conf.getInt("ggroup.timeout")

  val ggroup_url = new URL(s"https://groups.google.com/forum/?_escaped_fragment_=forum/$ggroup_name")
  logger.info("Crawling group: {}", ggroup_url)

  def topics(): Stream[GGTopic] = pages().flatMap { page => page.topics }

  def messages(): Stream[GGMessage] = this.topics().flatMap { topic => topic.messages }

  private def pages() = pages_iterator(ggroup_url)

  private def topics_iterator(doc: Document) = {
    doc.select("tr td.subject a")
      .par.toStream
      .map {
        el =>
          val topic_url = new URL(el.attr("abs:href"))
          new GGTopicCrawler(conf).parse(topic_url)
      }
  }

  private def pages_iterator(url: URL): Stream[GGroupPage] = {

    logger.debug(s"parsing PAGES url: $url", url)

    try {

      lazy val doc = Jsoup.parse(url, timeout)

      lazy val next_href = doc.select("body > a")

      lazy val page = GGroupPage(url, topics_iterator(doc))

      if (next_href.size() == 0)
        Stream(page)
      else
        page #:: pages_iterator(new URL(next_href.last().attr("abs:href")))

    } catch {

      // UnknownHostException
      case e: Throwable =>
        logger.error(s"ERROR parsing url: $url", e.getMessage)
        Stream.Empty
    }

  }

}