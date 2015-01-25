package com.aramirez

import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._

class MyServiceSpec extends Specification with Specs2RouteTest with LoginService {
  def actorRefFactory = system

  "LoginService" should {

    "return a greeting for GET requests to the root path" in {
      Get() ~> loginRoute ~> check {
        responseAs[String] must contain("<h1>Homepage</h1>")
      }
    }

    "return 200 and form page when GET /login page" in {
      Get("/login") ~> loginRoute ~> check {
        status === OK
        responseAs[String] must contain("<form action=\"/login\" method=\"post\">")
      }
    }
  }
}