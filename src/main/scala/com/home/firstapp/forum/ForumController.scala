package com.home.firstapp.forum

import com.home.firstapp.{Routes, ForumServlet}
import com.home.firstapp.forum.dao.TopicRepository

/**
 *
 * @author Antanas Bastys
 */
trait ForumController {
  self: ForumServlet =>
  val topicRepository = TopicRepository()

  get(Routes.TOPIC + "/:id") {
    contentType="text/html"
    layoutTemplate("/WEB-INF/templates/views/topic.ssp",
      "user" -> user,
      "topic" -> topicRepository.find(Integer.parseInt(params("id"))))
  }
}
