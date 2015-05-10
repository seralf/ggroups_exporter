package it.seralf.googlegroups

import com.typesafe.config.ConfigFactory
import java.io.File

object MainErrorGGGroupCrawler extends App {

  val ggroup_name = "spaghettiopendata"
  val topic_id = "TygQe55iYFc"

  val conf = ConfigFactory.load().withFallback(ConfigFactory.parseFile(new File("./conf/ggroup.conf")))

  val gg_crawler = new GGroupCrawler(ggroup_name, conf)

  val msgs = gg_crawler.messages()
  
  
}