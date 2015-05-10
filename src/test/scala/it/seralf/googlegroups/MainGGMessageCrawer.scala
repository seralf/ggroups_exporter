package it.seralf.googlegroups

import java.net.URL
import org.jsoup.Jsoup
import scala.collection.JavaConversions._

object MainGGMessageCrawler extends App {

  val timeout = 300000

  val ggroup_name = "spaghettiopendata"
  val topic_id = "58Wfrxw-KrI"
  val msg_id = "LaBagYeD4pcJ"

  val topic_url = new URL(s"https://groups.google.com/forum/?_escaped_fragment_=topic/${ggroup_name}/${topic_id}")

  val doc = Jsoup.parse(topic_url, timeout)
  def msgs = doc.select("tr td.subject").toList
  val msg_el = msgs.head

  val a = msg_el.select("a")
  val href = a.attr("abs:href")
  val author = msg_el.parent().select(".author span").text()

  val meta = GGMsgMeta(href, author, msg_id)
  val msg: GGMessage = GGMessageCrawler.parse(ggroup_name, topic_id, meta)

  println(msg)
}