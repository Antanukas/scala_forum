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
import org.json4s.JsonDSL._
import com.home.firstapp.Routes._
/**
 *
 * @author Antanas Bastys <antanas.bastys@tieto.com>
 */
trait ChatController extends JValueResult
with JacksonJsonSupport
with AtmosphereSupport {
  self: ForumServlet =>

  implicit protected val jsonFormats: Formats = DefaultFormats

  atmosphere(CHAT_WS) {
    println("something happening")
    new AtmosphereClient {
      println(s"new instance ${user.username}" )
      //username can be retrieved only on initial websocket connection
      val username = user.username;


      override def receive = {
        case Connected => broadcast(("user" -> "SYSTEM") ~ ("message" -> s"$username entered chat..."))
        case Disconnected(disconnector, Some(error)) => { println("disconnecting"); broadcast(("user" -> "SYSTEM") ~ ("message" -> s"$username left chat...")) }
        case Error(Some(error)) => error.printStackTrace
        case TextMessage(text) => {
          broadcast(("user" -> username) ~ ("message" -> text), Everyone)
        }//broadcast(s"[${username}]: $text", Everyone)
        //case JsonMessage(content) => content
      }
    }
  }

  get(CHAT) {
    contentType="text/html"
    layoutTemplate("/WEB-INF/templates/views/chat.ssp", "user" -> user)
  }
}
