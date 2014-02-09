package com.home.firstapp.forum.dao

import scala.collection.concurrent.{TrieMap, Map}
import com.home.firstapp.forum.domain.Topic

trait TopicRepository {
  def allTopics: List[Topic]
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
  val topics : Map[String, List[Topic]] = TrieMap()

  def allTopics = topics.foldLeft(List[Topic]())(_ ++ _._2)
  def userTopics(username: String) = topics(username)
  def addTopic(topic: Topic) = topics.put(topic.username, topic :: topics.getOrElse(topic.username, List()) )

}


