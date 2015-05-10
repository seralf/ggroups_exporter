package it.seralf.googlegroups

import java.io._
import java.net.URL
import scala.collection.JavaConversions._
import scala.io.Source
import org.jsoup.Jsoup
import org.jsoup.nodes._
import scala.Stream
import scala.collection.immutable.Stream.consWrapper
import com.typesafe.config.ConfigFactory
import it.seralf.experiments.MessageParser
import scala.util.Random
import Utilities._
import org.slf4j._

object GGMain extends App {

  val logger = LoggerFactory.getLogger(this.getClass)

  val conf = ConfigFactory.load().withFallback(ConfigFactory.parseFile(new File("./conf/ggroup.conf")))

  val ggroups_name = conf.getString("ggroup.name")

  val repo_cache = new FileRepository(conf)

  val (start, end) = (conf.getString("ggroup.start"), conf.getString("ggroup.end"))

  val save_json = conf.getBoolean("ggroup.save_json")

  logger.info("STARTED {}", now)

  val gg_crawler = new GGroupCrawler(ggroups_name, conf)
  val messages = gg_crawler.messages()

  val max = messages.size

  messages
    .par
    .foreach {
      message =>

        sleep_random(500)

        val ggroup_name = message.ggroup_name
        val topic_id = message.topic_id
        val msg_id = message.msg_id
        val msg_raw = message.raw

        logger.debug(s"caching /${ggroup_name}/${topic_id}/${msg_id}")

        repo_cache.msg_save(ggroup_name, topic_id, msg_id, msg_raw)

    }

  logger.info("ENDED {}", now)

  System.exit(0)
}

