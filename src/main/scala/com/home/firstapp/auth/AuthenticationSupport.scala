package com.home.firstapp.auth

import org.scalatra.auth.{ScentryConfig, ScentrySupport}
import org.scalatra.ScalatraBase
import com.home.firstapp.domain.User

/**
 *
 * @author Antanas Bastys
 */
trait AuthenticationSupport extends ScentrySupport[User]{
  self: ScalatraBase =>

  protected def fromSession = { case username: String => User(username)  }
  protected def toSession   = { case usr: User => usr.username }

  protected val scentryConfig = (new ScentryConfig {}).asInstanceOf[ScentryConfiguration]


  override protected def configureScentry = {
    scentry.unauthenticated {
      scentry.strategies("Password").unauthenticated()
    }
  }

  override protected def registerAuthStrategies = {
    scentry.register("Password", app => new PasswordStrategy(app))
  }

  def authenticate = scentry.authenticate("Password")
}
