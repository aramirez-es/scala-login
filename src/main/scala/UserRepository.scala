package com.aramirez

trait UserRepository {
  private val userRepository = Seq(
    new User("user_1", "123456", List("PAG_1")),
    new User("user_2", "123456", List("PAG_2")),
    new User("user_3", "123456", List("PAG_3")),
    // User with more than one role
    new User("user_13", "123456", List("PAG_1", "PAG_3")),
    new User("user_23", "123456", List("PAG_2", "PAG_3")),
    new User("user_12", "123456", List("PAG_1", "PAG_2"))
  )

  def checkLogin(user_name: String, user_pass: String): Option[User] =
    userRepository.find(user => user.name == user_name && user.password == user_pass)

  def hasPrivilege(user: User, privilege: String): Boolean =
    user.role.contains(privilege)
}