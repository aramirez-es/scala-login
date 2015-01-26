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
    new User("user_1", "123456"),
    new User("user_2", "123456"),
    new User("user_3", "123456")
  )

  def checkLogin(user_name: String, user_pass: String): Boolean = {
    userRepository.exists(user => user.name == user_name && user.password == user_pass)
  }

  val loginRoute =
    get {
      respondWithMediaType(`text/html`) {
        path("") {
          optionalCookie("logged") {
            case Some(loggin_cookie) => complete {
              SessionManagement.findUserLogged(loggin_cookie.content) match {
                case Some(session) => html.index.render(session.user_name).toString()
                case None => html.index.render("").toString()
              }
            }
            case None => complete {
              html.index.render("").toString()
            }
          }
        } ~
        path("login") {
          complete {
            html.login.render().toString()
          }
        } ~
        path("logout") {
          cookie("logged") { loggin_cookie =>
            SessionManagement.findUserLogged(loggin_cookie.content) match {
              case Some(session) => deleteCookie("logged") {
                SessionManagement.logUserOut(session.user_name)
                // Redirect to "" does not work. Hardcoded until find a fix to that.
                redirect("http://localhost:8080", StatusCodes.MovedPermanently)
              }
              case None =>
                // Redirect to "" does not work. Hardcoded until find a fix to that.
                redirect("http://localhost:8080", StatusCodes.MovedPermanently)
            }
          }
        } ~
        path("page1") {
          optionalCookie("logged") {
            case Some(loggin_cookie) => SessionManagement.findUserLogged(loggin_cookie.content) match {
              case Some(session) => complete {
                html.page.render(session.user_name).toString()
              }
              case None => redirect("/login", StatusCodes.MovedPermanently)
            }
            case None => redirect("/login", StatusCodes.MovedPermanently)
          }
        } ~
        path("page2") {
          optionalCookie("logged") {
            case Some(loggin_cookie) => SessionManagement.findUserLogged(loggin_cookie.content) match {
              case Some(session) => complete {
                html.page.render(session.user_name).toString()
              }
              case None => redirect("/login", StatusCodes.MovedPermanently)
            }
            case None => redirect("/login", StatusCodes.MovedPermanently)
          }
        } ~
        path("page3") {
          optionalCookie("logged") {
            case Some(loggin_cookie) => SessionManagement.findUserLogged(loggin_cookie.content) match {
              case Some(session) => complete {
                html.page.render(session.user_name).toString()
              }
              case None => redirect("/login", StatusCodes.MovedPermanently)
            }
            case None => redirect("/login", StatusCodes.MovedPermanently)
          }
        }
      }
    } ~
    post {
      path("login") {
        formFields('user_name, 'user_password) { (user_name, user_password) =>
          validate(checkLogin(user_name, user_password), "Email or password not valid") {
            setCookie(SessionManagement.logUserIn(user_name, user_password)) {
              // Redirect to "" does not work. Hardcoded until find a fix to that.
              redirect("http://localhost:8080", StatusCodes.MovedPermanently)
            }
          }
        }
      }
    }
}