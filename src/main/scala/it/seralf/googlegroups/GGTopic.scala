package it.seralf.googlegroups

import java.net.URL

case class GGTopic(topic_id: String, url: URL, messages: Stream[GGMessage] = Stream.empty) {
  override def toString() = s"""TOPIC [$url] ${messages.toList.size} messages"""
}