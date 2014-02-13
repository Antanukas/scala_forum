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

/**
 *
 * @author Antanas Bastys <antanas.bastys@tieto.com>
 */
trait ChatController extends JValueResult
with JacksonJsonSupport with SessionSupport
with AtmosphereSupport {
  self: ForumServlet =>

  implicit protected val jsonFormats: Formats = DefaultFormats

  atmosphere("/chat") {
    new AtmosphereClient {
      override def receive: _root_.org.scalatra.atmosphere.AtmoReceive = {
        case Connected => println("connected")
        case Disconnected(disconnector, Some(error)) => println("disconnected")
        case Error(Some(error)) =>
        case TextMessage(text) => { println(s"echo: $text"); send("ECHO: " + text); }
        case JsonMessage(json) => broadcast(json)
      }
    }
  }

  get("/chat-page") {
    contentType="text/html"
    layoutTemplate("/WEB-INF/templates/views/chat.ssp", "user" -> user)
  }
}
