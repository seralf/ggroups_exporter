package it.seralf.googlegroups

import java.net.URL

case class GGroupPage(val url: URL, topics: Stream[GGTopic] = Stream.empty)
