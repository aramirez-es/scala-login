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
          complete {
            html.login.render().toString()
          }
        } ~
        path("logout") {
          cookie("logged") { loggin_cookie =>
            SessionManagement.findUserLogged(loggin_cookie.content) match {
              case Some(session) => deleteCookie("logged") {
                SessionManagement.logUserOut(session.user.name)
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
                html.page.render(session.user.name).toString()
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
                html.page.render(session.user.name).toString()
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
                html.page.render(session.user.name).toString()
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
          checkLogin(user_name, user_password) match {
            case Some(user) => setCookie(SessionManagement.logUserIn(user)) {
              // Redirect to "" does not work. Hardcoded until find a fix to that.
              redirect("http://localhost:8080", StatusCodes.MovedPermanently)
            }
            case None => reject(ValidationRejection("Email or password not valid."))
          }
        }
      }
    }
}