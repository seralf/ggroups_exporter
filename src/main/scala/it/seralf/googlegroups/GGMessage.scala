package it.seralf.googlegroups

case class GGMessage(ggroup_name: String, topic_id: String, msg_id: String, raw: String, meta: GGMsgMeta) {

  override def toString() = s"""MAIL(${meta.msg_id}) [${meta.original_url}] by ${meta.author}\n$raw\n"""

}

case class GGMsgMeta(original_url: String, author: String, msg_id: String)
