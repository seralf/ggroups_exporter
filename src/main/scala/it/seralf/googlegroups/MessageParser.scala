package it.seralf.experiments

import java.net.URL
import javax.mail._
import javax.mail.internet._
import scala.collection.JavaConverters._
import scala.collection.JavaConversions._
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import java.io.File
import com.sun.org.apache.bcel.internal.generic.INSTANCEOF

/*
 * SEE: https://javamail.java.net/nonav/docs/api/
 * SEE: http://docs.oracle.com/javaee/6/api/javax/mail/package-summary.html
 */
object MessageParser {

  val properties = System.getProperties()
  properties.setProperty("mail.store.protocol", "imap")

  def parseMime(url: URL): (String, String) = {
    val bais = url.openStream()

    val mimemsg = new MimeMessage(Session.getDefaultInstance(properties), bais)

    val sender = mimemsg.getFrom()
    //  val content = mimemsg.getContent().asInstanceOf[MimeMultipart].getBodyPart(0).writeTo(System.out)
    val json_mapper = new ObjectMapper()
      .writerWithDefaultPrettyPrinter()
      .without(SerializationFeature.FAIL_ON_EMPTY_BEANS)

    val headers = mimemsg.getAllHeaders()
      .map { h =>
        val header = h.asInstanceOf[Header]
        (header.getName, header.getValue)
      }.toList

    val content = mimemsg.getContent() match {
      case c if (c.isInstanceOf[String]) => c.asInstanceOf[String]
      case body                          => body.toString()
      //      case _                             => "TODO"
    }

    val msg = (("Body" -> content) :: headers).toMap.asJava

    (msg.get("Message-ID"), json_mapper.writeValueAsString(msg))

  }

}