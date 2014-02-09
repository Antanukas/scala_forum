package com.home.firstapp.auth

import org.scalatra.ScalatraBase
import org.scalatra.auth.ScentryStrategy
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import com.home.firstapp.domain.User

/**
 *
 * @author Antanas Bastys
 */
class PasswordStrategy(protected override val app: ScalatraBase) extends ScentryStrategy[User]{
  override def isValid(implicit request: HttpServletRequest) =
    app.params.get("login").isDefined && app.params.get("password").isDefined

  def authenticate()(implicit request: HttpServletRequest, response: HttpServletResponse): Option[User] = {
    val (login, password) = (app.params.get("login"), app.params.get("password"))
    (login, password) match {
      case (Some("test"), Some("testp")) => Some(User("test"))
      case _ => None
    }
  }
}
