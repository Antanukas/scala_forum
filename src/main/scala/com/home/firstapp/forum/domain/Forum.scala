package com.home.firstapp.forum.domain

import com.github.nscala_time.time.Imports._
import com.home.firstapp.forum.dao.TopicRepository

/**
 *
 * @author Antanas Bastys
 */
object Forum {
  val topicRepository = TopicRepository()

  def createTopic(username: String, topic: String, text: String) =
    topicRepository.persist(new Topic(username, topic, text))


  def reply(username: String, id: Long, text: String) {
    //todo persist actually
    val reply = new PostedTopicReply(1, username, text, DateTime.now)
    val topic = topicRepository.find(id)
    val updatedTopic = new PostedTopic(topic.id, topic.username, topic.topic, topic.text, topic.postDate, reply :: topic.replies)
    topicRepository.update(updatedTopic)
  }

  def topics = topicRepository.findAll

}

class TopicReply(val username: String, val text: String)
class PostedTopicReply(val id: Long, username: String, text: String, val postDate: DateTime) extends TopicReply(username, text)

class Topic(val username: String, val topic: String, val text: String)
class PostedTopic(val id: Long, username: String, topic: String, text: String, val postDate: DateTime, val replies: List[PostedTopicReply]) extends Topic(username, topic, text) {

  override def hashCode(): Int = 31 * id.toInt
  override def equals(obj: Any) = {
    obj match {
      case topic: PostedTopic if topic != null => this.id == topic.id
      case _ => false
    }
  }
}