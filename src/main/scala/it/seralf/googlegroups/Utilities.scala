package it.seralf.googlegroups

import java.io.FileOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import org.slf4j._
import scala.util.Random

object Utilities {

  val logger = LoggerFactory.getLogger(this.getClass)

  val sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

  def now = sdf.format(new Date())

  def sleep_random(seed: Int) = Thread.sleep(Random.nextInt(seed))

  def saveFile(file: File, content: String, overwrite: Boolean = false): File = {
    if (overwrite || !file.exists()) {
      logger.debug("SAVING {}", file)
      if (!file.getParentFile.exists()) file.getParentFile.mkdirs()
      val fos = new FileOutputStream(file)
      fos.write(content.getBytes())
      fos.close()
    } else {
      logger.debug(s"FILE $file already exists {}", file)
    }
    file
  }

  def listFiles(dir: File): List[File] = {
    logger.debug(s"list files in dir: $dir")
    if (dir.isFile()) List(dir.getAbsoluteFile)
    else
      dir.listFiles().toList.flatMap { child => listFiles(child) }
  }

}