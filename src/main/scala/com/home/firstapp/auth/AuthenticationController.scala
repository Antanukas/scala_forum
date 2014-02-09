package com.home.firstapp.auth

import com.home.firstapp.ForumServlet
import com.home.firstapp.Routes._

/**
 *
 * @author Antanas Bastys
 */
trait AuthenticationController {
  self: ForumServlet =>

  get(NEW_SESSION) {
    contentType="text/html"
    layoutTemplate("/WEB-INF/templates/views/login.ssp", "title" -> "login page", "layout" -> "")
  }

  post(SESSION) {
    authenticate
    redirect(HOME)
  }

  delete(SESSION) {
    logOut
    redirect(HOME)
  }
}
