package com.aramirez

/**
 * A class to represent sessions.
 */
class Session(val token: String, val user_name: String) {
  var time_of_last_activity: Long = System.currentTimeMillis
}