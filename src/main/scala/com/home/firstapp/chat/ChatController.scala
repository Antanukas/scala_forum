package com.home.firstapp.chat

import com.home.firstapp.ForumServlet
import org.scalatra.SessionSupport
import org.scalatra.json.{JValueResult, JacksonJsonSupport}
import org.scalatra.atmosphere._
import org.scalatra.atmosphere.JsonMessage
import org.scalatra.atmosphere.TextMessage
import scala.Some
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import org.json4s.{Formats, DefaultFormats}
import com.home.firstapp.Routes._;

/**
 *
 * @author Antanas Bastys <antanas.bastys@tieto.com>
 */
trait ChatController extends JValueResult
with JacksonJsonSupport with SessionSupport
with AtmosphereSupport {
  self: ForumServlet =>

  implicit protected val jsonFormats: Formats = DefaultFormats

  atmosphere(CHAT_WS) {
    new AtmosphereClient {
      //username can be retrieved only on initial websocket connection
      val username = user.username;
      override def receive = {
        case Connected =>
        case Disconnected(disconnector, Some(error)) =>
        case Error(Some(error)) =>
        case TextMessage(text) => broadcast(s"[${username}]: $text", Everyone)
      }
    }
  }

  get(CHAT) {
    contentType="text/html"
    layoutTemplate("/WEB-INF/templates/views/chat.ssp", "user" -> user)
  }
}
