package it.seralf.googlegroups

import it.seralf.experiments.MessageParser

import java.io.File

class MainMessageParser {

  val export_dir = "/var/EXPORT"

  val ggroup = "spaghettiopendata"

  val url = new File(s"$export_dir/$ggroup/HpY_NBOxaPw/kXQpb8bpn40J.eml").toURI().toURL()

  val msg = MessageParser.parseMime(url)
  println(msg)

}