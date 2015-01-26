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

  val secure_prefix = "3240dmiv00%$xsmo!a8730"

  val userRepository = Seq(
    ("user_1", "123456"),
    ("user_2", "123456"),
    ("user_3", "123456")
  )

  var loggedInUsers = Seq[Session]()

  def checkLogin(user_name: String, user_pass: String): Boolean = {
    userRepository.exists(tuple => tuple._1 == user_name && tuple._2 == user_pass)
  }

  def logUserIn(user_name: String, user_password: String): HttpCookie = {
    val md = java.security.MessageDigest.getInstance("SHA-1");
    val token = md.digest((secure_prefix.concat(user_name).concat(System.currentTimeMillis.toString)).getBytes).toString
    loggedInUsers = loggedInUsers :+ new Session(token, user_name)
    println("new user logged in " + user_name + " with token " + token)
    HttpCookie("logged", content = token)
  }

  def logUserOut(token: String): Unit = {
    println("log out user with token " + token)
    loggedInUsers = loggedInUsers.filterNot(session => session.token == token)
  }

  val loginRoute =
    get {
      respondWithMediaType(`text/html`) {
        path("") {
          optionalCookie("logged") {
            case Some(nameCookie) => complete {
              loggedInUsers.find((session => session.token == nameCookie.content)) match {
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
          cookie("logged") { nameCookie =>
            loggedInUsers.find((session => session.token == nameCookie.content)) match {
              case Some(session) => deleteCookie("logged") {
                logUserOut(session.user_name)
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
            case Some(nameCookie) => loggedInUsers.find((session => session.token == nameCookie.content)) match {
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
            case Some(nameCookie) => loggedInUsers.find((session => session.token == nameCookie.content)) match {
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
            case Some(nameCookie) => loggedInUsers.find((session => session.token == nameCookie.content)) match {
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
            setCookie(logUserIn(user_name, user_password)) {
              // Redirect to "" does not work. Hardcoded until find a fix to that.
              redirect("http://localhost:8080", StatusCodes.MovedPermanently)
            }
          }
        }
      }
    }
}