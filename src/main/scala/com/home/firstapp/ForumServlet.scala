package com.home.firstapp

import Routes._
import com.home.firstapp.auth.{AuthenticationController, AuthenticationSupport}
import org.scalatra.MethodOverride
import com.home.firstapp.forum.domain.Forum
import com.home.firstapp.forum.ForumController
import com.home.firstapp.chat.ChatController

class ForumServlet extends ForumAppStack
                   with AuthenticationSupport
                   with MethodOverride
                   with AuthenticationController
                   with ForumController
                   with ChatController {

  get(HOME) {
    val postedId = Forum.createTopic(user.username, "Test topic 1", "Test content 1").id
    Forum.reply("testUser", postedId, "you die")
    Forum.reply("testUser", postedId, "you die2")
    Forum.reply("testUser", postedId, "you die3")
    Forum.createTopic(user.username, "Test topic 2", "Test content 2")
    Forum.createTopic(user.username, "Test topic 3", "Test content 3")
    contentType="text/html"
    layoutTemplate("/WEB-INF/templates/views/home.ssp",
      "user" -> user,
      "title" -> "Homepage",
      "threads" -> Forum.topics)
  }

  notFound {
    serveStaticResource() getOrElse {
      println(servletContext.getResourcePaths("/"))
      contentType="text/html"
      response.setStatus(404)
      layoutTemplate("/WEB-INF/templates/views/not-found.ssp", "title" -> "Error: Not Found", "layout" -> "")
    }
  }

}
