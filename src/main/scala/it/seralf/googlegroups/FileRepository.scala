package it.seralf.googlegroups

import java.io.File
import it.seralf.experiments.MessageParser
import com.typesafe.config.Config

class FileRepository(conf: Config) {

  val cache_dir = new File(conf.getString("ggroup.export.dir")).getCanonicalFile

  if (!cache_dir.exists()) cache_dir.mkdirs()

  def msg_exists(ggroup_name: String, topic_id: String, msg_id: String): Boolean = {
    val msg_cache = new File(cache_dir, s"${ggroup_name}/${topic_id}/${msg_id}.eml")
    msg_cache.exists()
  }

  def topic_exists(ggroup_name: String, topic_id: String): Boolean = {
    val topic_cache = new File(cache_dir, s"${ggroup_name}/${topic_id}")
    topic_cache.exists()
  }

  def msg_save(ggroup_name: String, topic_id: String, msg_id: String, msg_raw: String) {

    val msg_file = new File(cache_dir, s"${ggroup_name}/${topic_id}/${msg_id}.eml")
    Utilities.saveFile(msg_file, msg_raw, false)

    val json_file = new File(cache_dir, s"${ggroup_name}/${topic_id}/${msg_id}.json")
    val msg_json = MessageParser.parseMime(msg_file.toURI().toURL())
    Utilities.saveFile(json_file, msg_json._2, false)

  }

}