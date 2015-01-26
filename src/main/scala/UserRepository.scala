package com.aramirez

trait UserRepository {
  private val userRepository = Seq(
    new User("user_1", "123456"),
    new User("user_2", "123456"),
    new User("user_3", "123456")
  )

  def checkLogin(user_name: String, user_pass: String): Option[User] = {
    userRepository.find(user => user.name == user_name && user.password == user_pass)
  }
}