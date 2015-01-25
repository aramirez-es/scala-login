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

  val loginRoute =
    path("") {
      get {
        respondWithMediaType(`text/html`) {
          complete {
            html.login.render().toString()
          }
        }
      }
    }
}