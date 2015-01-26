package com.aramirez

/**
 * A class to represent sessions.
 */
case class Session(val token: String, val user: User, val time_of_last_activity: Long) {
}