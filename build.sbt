name := "akka-streams-tcp"

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies ++= {
  val akkaVersion = "2.5.1"

  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion
  )
}