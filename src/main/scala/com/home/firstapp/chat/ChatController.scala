package com.home.firstapp.chat

import com.home.firstapp.ForumServlet
import org.scalatra.json.{JValueResult, JacksonJsonSupport}
import org.scalatra.atmosphere._
import org.scalatra.atmosphere.TextMessage
import scala.Some
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import org.json4s.{Formats, DefaultFormats}
import org.json4s.JsonDSL._
import com.home.firstapp.Routes._
import com.github.nscala_time.time.Imports.DateTime
import org.joda.time.format.ISODateTimeFormat
import org.atmosphere.cpr.BroadcasterFactory

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
    new AtmosphereClient {
      //username can be retrieved only on initial websocket connection
      val username = user.username;

      //TODO temp workaround
      private[this] final def broadcast2(msg: OutboundMessage, to: ClientFilter = Others)(implicit executionContext: ExecutionContext) = {
        val br = BroadcasterFactory.getDefault.get(requestUri).asInstanceOf[ScalatraBroadcaster]
        br.broadcast(msg, to)
      }
      override def receive = {
        //this broadcast is ain working yet because first Connected event is published and later broadcaster created.
        //Need similar fix to https://github.com/scalatra/scalatra/commit/0b5cd743325dfceb6ca974a7085fe4631e13e890 in Scalatra.
        //TODO after fix change broadcast2 to broadcast from scalatra
        case Connected => {
          broadcast2(Message("SYSTEM", s"$username entered chat..."), Everyone)
        }
        case Disconnected(disconnector, _) => { println("disconnecting"); broadcast(("user" -> "SYSTEM") ~ ("message" -> s"$username left chat..."), Everyone) }
        case Error(Some(error)) => error.printStackTrace
        case TextMessage(text) => {
          broadcast(Message(username, text), Everyone)
        }//broadcast(s"[${username}]: $text", Everyone)
        //case JsonMessage(content) => content
      }
    }
  }

  get(CHAT) {
    contentType="text/html"
    layoutTemplate("/WEB-INF/templates/views/chat.ssp", "user" -> user)
  }

  object Message {
    def apply(username: String, message: String) =
      ("user" -> username) ~
        ("message" -> message) ~
        ("time" -> DateTime.now.toString(ISODateTimeFormat.timeNoMillis()))
  }
}
