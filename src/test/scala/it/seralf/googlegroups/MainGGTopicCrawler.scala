package it.seralf.googlegroups

import com.typesafe.config.ConfigFactory
import java.io.File

/*
 * TODO: rewrite as Unit test 
 */
object MainGGTopicCrawler extends App {

  val ggroup_name = "spaghettiopendata"
  //  val topic_id = "58Wfrxw-KrI" // OK
  // ERROR:  val topic_id = "rk8UWAtlFJY" // ERROR 500 EXAMPLE
  val topic_id = "_1tpuqWJl_M"

  val conf = ConfigFactory.load().withFallback(ConfigFactory.parseFile(new File("./conf/ggroup.conf")))
  val topic = new GGTopicCrawler(conf).parse(ggroup_name, topic_id)

  println("\n\nTOPIC\n" + topic)
}