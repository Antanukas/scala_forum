package com.home.firstapp.forum

import com.home.firstapp.{Routes, ForumServlet}

/**
 *
 * @author Antanas Bastys
 */
trait ForumController {
  self: ForumServlet =>

  get(Routes.TOPIC + ":id") {

  }
}
