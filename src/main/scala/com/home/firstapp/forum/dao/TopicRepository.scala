package com.home.firstapp.forum.dao

import scala.collection.concurrent.{TrieMap, Map}
import com.home.firstapp.forum.domain.{PostedTopic, Topic}
import com.github.nscala_time.time.Imports._

trait TopicRepository extends GenericDao[Topic, PostedTopic, Long] {
  def userTopics(username: String): List[Topic]
}

object TopicRepository {
  def apply() = MapTopicRepository
}
/**
 *
 * @author Antanas Bastys
 */
object MapTopicRepository extends TopicRepository {
  val topics : Map[String, Set[PostedTopic]] = TrieMap()
  var topicCount = 0

  def userTopics(username: String) = topics(username).toList

  override def findAll = topics.foldLeft(List[PostedTopic]())(_ ++ _._2)
  override def persist(topic: Topic) = {
    val count = synchronized { topicCount += 1; topicCount }
    val saved = new PostedTopic(count, topic.username, topic.topic, topic.text, DateTime.now, List())
    topics.put(topic.username, topics.getOrElse(topic.username, Set()) + saved)
    saved
  }

  override def find(id: Long) = findAll.find(topic => topic.id == id) match {
    case Some(topic) => topic
    case None => throw new IllegalArgumentException("Topic not found")
  }

  override def update(topic: PostedTopic) = {
    topics.put(topic.username, topics.getOrElse(topic.username, Set()) - topic + topic)
    topic
  }

  override def delete(id: Long) { throw new NotImplementedError("Not yet implemented")}
}


