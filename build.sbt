import sbt.Keys.{libraryDependencies, _}

lazy val commonSettings = Seq(
  organization := "fixsupervisor",
  version := "0.0.1",
  scalaVersion := "2.12.6",

  libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  libraryDependencies += "javax.ws.rs" % "javax.ws.rs-api" % "2.1" artifacts Artifact("javax.ws.rs-api", "jar", "jar"), // seems to be required for kafka-streams
  libraryDependencies += "org.apache.kafka" % "kafka-clients" % "2.0.0" exclude("log4j","log4j-api") exclude("log4j","log4j-core"),
  libraryDependencies += "org.apache.kafka" % "kafka-streams" % "2.0.0" exclude("log4j","log4j-api") exclude("log4j","log4j-core")
)

lazy val fixsupervisor = (project in file(".")).settings(commonSettings: _*).settings(
  name := "fixsupervisor"
)
