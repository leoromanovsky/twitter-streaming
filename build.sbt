name := "twitter-streaming"

version := "1.0"

scalaVersion := "2.10.4"

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.1.1",
  "org.apache.spark" %% "spark-streaming" % "1.1.1",
  "org.apache.spark" %% "spark-streaming-twitter" % "1.1.1")
