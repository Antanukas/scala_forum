package com.home.firstapp.forum.domain

import com.github.nscala_time.time.Imports._
import com.home.firstapp.forum.dao.TopicRepository

/**
 *
 * @author Antanas Bastys
 */
object Forum {
  val topicRepository = TopicRepository()

  def createTopic(username: String, topic: String, text: String) = {
    topicRepository.addTopic(Topic(username, topic, text, DateTime.now, List()))
  }

  def topics = topicRepository.allTopics

}

abstract class Post {
  val username: String
  val text: String
  val postDate: DateTime
}

case class TopicPost(id: Long, username: String, text: String, postDate: DateTime) extends Post
case class Topic(username: String, topic: String, text: String, postDate: DateTime, posts: List[TopicPost]) extends Post
case class SavedTopic(override val username: String) extends Topic(username, topic)