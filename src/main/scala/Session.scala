package com.aramirez

/**
 * A class to represent sessions.
 */
case class Session(token: String, user: User, time_of_last_activity: Long)