package com.aramirez

import spray.http.HttpCookie

object SessionManagement {

  val secure_prefix = "3240dmiv00%$xsmo!a8730"

  var loggedInUsers = Seq[Session]()

  def logUserIn(user_name: String, user_password: String): HttpCookie = {
    val md = java.security.MessageDigest.getInstance("SHA-1");
    val token = md.digest((secure_prefix.concat(user_name).concat(System.currentTimeMillis.toString)).getBytes).toString
    loggedInUsers = loggedInUsers :+ new Session(token, user_name)
    println("new user logged in " + user_name + " with token " + token)
    HttpCookie("logged", content = token)
  }

  def logUserOut(token: String): Unit = {
    println("log out user with token " + token)
    loggedInUsers = loggedInUsers.filterNot(session => session.token == token)
  }

  def findUserLogged(token: String): Option[Session] = {
    loggedInUsers.find((session => session.token == token))
  }
}