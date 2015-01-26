package com.aramirez

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import spray.can.Http

import scala.concurrent.duration._
import scala.util.Try

object ReactLoginApp extends App {

  implicit val system = ActorSystem("on-spray-can")

  val service = system.actorOf(Props[ReactLoginActor], "login-service")

  implicit val timeout = Timeout(5.seconds)

  val config = ConfigFactory.load()
  lazy val host = Try(config.getString("service.host")).getOrElse("localhost")
  lazy val port = Try(config.getInt("service.port")).getOrElse(8080)

  println("Starting application on " + port)

  IO(Http) ? Http.Bind(service, host, port)
}