import twirl.sbt.TwirlPlugin._
import spray.revolver.RevolverPlugin._

organization := "com.aramirez"

name := "react-login"

version := "1.0"

scalaVersion := "2.11.5"

libraryDependencies ++= {
  val akkaV = "2.3.6"
  val sprayV = "1.3.2"
  Seq(
    "io.spray"            %%  "spray-can"     % sprayV,
    "io.spray"            %%  "spray-routing" % sprayV,
    "io.spray"            %%  "spray-testkit" % sprayV  % "test",
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV
  )
}

Revolver.settings

Twirl.settings