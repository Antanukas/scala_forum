package com.home.firstapp.auth

import org.scalatra.ScalatraFilter
import com.home.firstapp.Routes

/**
 *
 * @author Antanas Bastys
 */
class AuthFilter extends ScalatraFilter with AuthenticationSupport {

  get("/*") {
    val auth = isAuthenticated
    if (request.getRequestURI.startsWith("/stylesheets")
      || request.getRequestURI.startsWith("/js")
      || isAuthenticated) filterChain.doFilter(request, response)
    else servletContext.getRequestDispatcher(Routes.NEW_SESSION).forward(request, response)
  }
}
