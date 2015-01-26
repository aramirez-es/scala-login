package com.aramirez

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._

class ReactLoginActor extends Actor with LoginService {

  def actorRefFactory = context

  def receive = runRoute(loginRoute)

}

trait LoginService extends HttpService with UserRepository {

  // Redirect to "/" does not work. Hardcoded until find out a solution.
  lazy val redirectToHome = redirect("http://localhost:8080", StatusCodes.MovedPermanently)
  lazy val redirectToLogin = redirect("/login", StatusCodes.MovedPermanently)

  val loginRoute =
    get {
      respondWithMediaType(`text/html`) {
        path("") {
          optionalCookie("logged") {
            case Some(loggin_cookie) => complete {
              SessionManagement.findUserLogged(loggin_cookie.content) match {
                case Some(session) => html.index.render(session.user.name).toString()
                case None => html.index.render("").toString()
              }
            }
            case None => complete {
              html.index.render("").toString()
            }
          }
        } ~
        path("login") {
          optionalCookie("logged") {
            case Some(loggin_cookie) => SessionManagement.findUserLogged(loggin_cookie.content) match {
              case Some(session) => redirectToHome
              case None => complete {
                html.login.render().toString()
              }
            }
            case None => complete {
              html.login.render().toString()
            }
          }
        } ~
        path("logout") {
          cookie("logged") { loggin_cookie =>
            SessionManagement.findUserLogged(loggin_cookie.content) match {
              case Some(session) => deleteCookie("logged") {
                SessionManagement.logUserOut(session.user.name)
                redirectToHome
              }
              case None => redirectToHome
            }
          }
        } ~
        path("page1") {
          optionalCookie("logged") {
            case Some(loggin_cookie) => SessionManagement.findUserLogged(loggin_cookie.content) match {
              case Some(session) => complete {
                html.page.render(session.user.name, 1).toString()
              }
              case None => redirectToLogin
            }
            case None => redirectToLogin
          }
        } ~
        path("page2") {
          optionalCookie("logged") {
            case Some(loggin_cookie) => SessionManagement.findUserLogged(loggin_cookie.content) match {
              case Some(session) => complete {
                html.page.render(session.user.name, 2).toString()
              }
              case None => redirectToLogin
            }
            case None => redirectToLogin
          }
        } ~
        path("page3") {
          optionalCookie("logged") {
            case Some(loggin_cookie) => SessionManagement.findUserLogged(loggin_cookie.content) match {
              case Some(session) => complete {
                html.page.render(session.user.name, 3).toString()
              }
              case None => redirectToLogin
            }
            case None => redirectToLogin
          }
        }
      }
    } ~
    post {
      path("login") {
        // TODO: check if user is logged in before anything else.
        formFields('user_name, 'user_password) { (user_name, user_password) =>
          checkLogin(user_name, user_password) match {
            case Some(user) => setCookie(SessionManagement.logUserIn(user)) {
              redirectToHome
            }
            case None => reject(ValidationRejection("Email or password not valid."))
          }
        }
      }
    }
}