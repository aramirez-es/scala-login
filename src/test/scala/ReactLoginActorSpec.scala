package com.aramirez

import org.specs2.mutable.Specification
import spray.routing.ValidationRejection
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._

class LoginServiceSpec extends Specification with Specs2RouteTest with LoginService {
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

    "return Bad Request when POST to /login without mandatory fields" in {
      Post("/login") ~> sealRoute(loginRoute) ~> check {
        status === StatusCodes.BadRequest
      }
    }

    "return ValidationRejection when POST to /login with invalid credentials" in {
      Post("/login", FormData(Seq("user_name" -> "aramirez_", "user_password" -> "123456"))) ~> loginRoute ~> check {
        rejection === ValidationRejection("Email or password not valid.")
      }
    }

    "return redirection and set Cookie when POST to /login with valid credentials" in {
      Post("/login", FormData(Seq("user_name" -> "user_1", "user_password" -> "123456"))) ~> loginRoute ~> check {
        status === StatusCodes.MovedPermanently
      }
    }
  }
}