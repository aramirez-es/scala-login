package com.aramirez

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._

class ReactLoginActor extends Actor with LoginService {

  def actorRefFactory = context

  def receive = runRoute(loginRoute)

}

trait LoginService extends HttpService {

  val userRepository = Seq(
    ("user_1", "123456"),
    ("user_2", "123456"),
    ("user_3", "123456")
  )

  def checkLogin(user_name: String, user_pass: String): Boolean = {
    userRepository.exists(tuple => tuple._1 == user_name && tuple._2 == user_pass)
  }

  val user = "Alberto"
  val loginRoute =
    get {
      respondWithMediaType(`text/html`) {
        path("") {
          complete {
            html.index.render().toString()
          }
        } ~
        path("login") {
          complete {
            html.login.render().toString()
          }
        } ~
        path("page1") {
          complete {
            html.page.render(user).toString()
          }
        } ~
        path("page2") {
          complete {
            html.page.render(user).toString()
          }
        } ~
        path("page3") {
          complete {
            html.page.render(user).toString()
          }
        }
      }
    } ~
    post {
      path("login") {
        formFields('user_name, 'user_password) { (user_name, user_password) =>
          validate(checkLogin(user_name, user_password), "Email and password not valid") {
            setCookie(HttpCookie("logged", user_name)) {
              redirect("http://localhost:8080", StatusCodes.MovedPermanently)
            }
          }
        }
      }
    }
}