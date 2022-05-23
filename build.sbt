name := """AnnotationRemake"""
organization := "org.forome"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
val sparkVersion = "3.0.1"
val akkaVersion = "2.5.26"
val akkaHttpVersion = "10.1.15"

scalaVersion := "2.12.13"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-streaming" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-core" % sparkVersion,
)

libraryDependencies += "org.mongodb.spark" %% "mongo-spark-connector" % sparkVersion
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.28"
libraryDependencies += "org.forome" % "astorage" % "1.0.0p13"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
)
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "org.forome.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "org.forome.binders._"
