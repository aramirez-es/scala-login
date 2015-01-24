package com.aramirez

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._

object ReactLoginApp extends App {

  implicit val system = ActorSystem("on-spray-can")

  val service = system.actorOf(Props[ReactLoginActor], "login-service")

  implicit val timeout = Timeout(5.seconds)

  val host = "localhost"
  val port = 8080
  println("Starting application on " + port)

  IO(Http) ? Http.Bind(service, host, port)
}