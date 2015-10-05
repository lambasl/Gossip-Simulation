name := """gossip"""

version := "1.0"
scalaVersion := "2.10.1"

mainClass in (Compile, run) := Some("main.Application")
resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.1.1",
  "com.typesafe.akka" %% "akka-remote" % "2.1.1"
)