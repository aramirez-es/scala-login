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
    }
}