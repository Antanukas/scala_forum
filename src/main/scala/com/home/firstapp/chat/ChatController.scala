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
import org.atmosphere.cpr.{AtmosphereResource, FrameworkConfig}
import java.util.concurrent.atomic.AtomicInteger
import scala.collection.mutable
import com.home.firstapp.domain.User
import collection.JavaConverters._
import grizzled.slf4j.Logger

/**
 *
 * @author Antanas Bastys <antanas.bastys@tieto.com>
 */
trait ChatController extends JValueResult
with JacksonJsonSupport
with AtmosphereSupport {
  self: ForumServlet =>
  private lazy val log = Logger[this.type]

  implicit protected val jsonFormats: Formats = DefaultFormats
  //for chat instance counting
  val userChats = new mutable.WeakHashMap[User, AtomicInteger] with mutable.SynchronizedMap[User, AtomicInteger]

  atmosphere(CHAT_WS) {
    new AtmosphereClient {
      //username can be retrieved only on initial websocket connection
      val user = ChatController.this.user
      val transportInUse  = request.getAttribute(FrameworkConfig.ATMOSPHERE_RESOURCE) match {
        case resource: AtmosphereResource => resource.transport()
        case _ => throw new IllegalStateException("Could not retrieve atmosphere resource from request")
      }

      override def receive = {
        case Connected => {
          val chatCount = getChatInstanceCount(user).incrementAndGet();
          log.debug(s"Connecting ${user.username} $chatCount")
          if (chatCount == 1) {
            AtmosphereClient.broadcast(requestUri, Message("SYSTEM", s"${user.username} entered chat..."), Others)
          }
        }
        case Disconnected(disconnector, _) => {
          val chatCount = getChatInstanceCount(user).decrementAndGet();
          log.debug(s"Disconnecting ${user.username} $request $chatCount $transportInUse")
          //workaround Long-polling causes endless loop on disconnect when broadcasting.
          if (chatCount < 1 && transportInUse == AtmosphereResource.TRANSPORT.WEBSOCKET) {
            AtmosphereClient.broadcast(requestUri, Message("SYSTEM", s"${user.username} left chat..."), Others)
          }
        }
        case Error(Some(error)) => error.printStackTrace
        case TextMessage(text) => broadcast(Message(user.username, text), Everyone)
      }

      def getChatInstanceCount(user: User) = userChats.getOrElseUpdate(user, new AtomicInteger(0))
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
