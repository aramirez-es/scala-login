package com.aramirez

import spray.http.HttpCookie

object SessionManagement {

  /**
   * Time that a user could remain inactive (in milli-seconds), beyond this value, user is logged out.
   */
  private val max_inactivity_time = 300000
  private val secure_prefix = "3240dmiv00%$xsmo!a8730"

  var loggedInUsers = Seq[Session]()

  def logUserIn(user: User): HttpCookie = {
    val md = java.security.MessageDigest.getInstance("SHA-1");
    val token = md.digest((secure_prefix.concat(user.name).concat(System.currentTimeMillis.toString)).getBytes).toString
    loggedInUsers = loggedInUsers :+ new Session(token, user)
    println("new user logged in " + user.name + " with token " + token)
    HttpCookie("logged", content = token)
  }

  def logUserOut(token: String): Unit = {
    println("log out user with token " + token)
    loggedInUsers = loggedInUsers.filterNot(session => session.token == token)
  }

  def findUserLogged(token: String): Option[Session] = {
    val current_time = System.currentTimeMillis
    loggedInUsers.find((session => session.token == token && session.time_of_last_activity + max_inactivity_time > current_time)) match {
      case Some(session) => {
        session.time_of_last_activity = current_time
        Some(session)
      }
      case None => None
    }
  }
}