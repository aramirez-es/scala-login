package com.aramirez

import spray.http.HttpCookie

/**
 * This should be handled with an Akka Agent instead.
 */
object SessionManagement {

  /**
   * Time that a user could remain inactive (in milli-seconds), beyond this value, user is logged out.
   */
  private val max_inactivity_time = 300000
  private val secure_prefix = "3240dmiv00%$xsmo!a8730"
  private val md = java.security.MessageDigest.getInstance("SHA-1")
  private val loggedInUsers = scala.collection.concurrent.TrieMap.empty[String,Session]

  private def tokenFormat(user: User): String =
    secure_prefix.concat(user.name).concat(System.currentTimeMillis.toString)

  private def token(user: User): String =
    md.digest(tokenFormat(user).getBytes).toString

  private def isSessionEnabled(user_token: String, current_time: Long): (((String,Session)) => Boolean) =
    (row) => row._1 == user_token && row._2.time_of_last_activity + max_inactivity_time > current_time

  def logUserIn(user: User): HttpCookie = {
    val token = this.token(user)
    loggedInUsers += (token -> new Session(token, user, System.currentTimeMillis))
    println("new user logged in " + user.name + " with token " + token)
    HttpCookie("logged", content = token)
  }

  def logUserOut(token: String): Unit = {
    println("log out user with token " + token)
    loggedInUsers -= token
  }

  def findUserLogged(token: String): Option[User] = {
    loggedInUsers find isSessionEnabled(token, System.currentTimeMillis) match {
      case Some((token,session)) => {
        loggedInUsers replace (token, new Session(token, session.user, System.currentTimeMillis))
        Some(session.user)
      }
      case None => None
    }
  }
}