import sbt._
import Keys._
import sbtassembly.Plugin._
import AssemblyKeys._

assemblySettings

name := "twitter-streaming"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.1.1",
  "org.apache.spark" %% "spark-streaming" % "1.1.1",
  "org.apache.spark" %% "spark-streaming-twitter" % "1.1.1")
  .map(_.exclude("javax", "servlet"))

resolvers ++= Seq(
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Akka Repository" at "http://repo.akka.io/releases/")

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
  {
    case PathList("org", "apache", xs @ _*) => MergeStrategy.last
    case PathList("javax", "servlet", xs @ _*) => MergeStrategy.last
    case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.last
    case PathList("project.clj") => MergeStrategy.last
    case PathList("overview.html") => MergeStrategy.last
    case PathList("META-INF", "ECLIPSEF.RSA") => MergeStrategy.discard
    case PathList("META-INF", "mailcap") => MergeStrategy.discard
    case PathList("plugin.properties") => MergeStrategy.last
    case x => old(x)
  }
}

// put all libs in the lib_managed directory, that way we can distribute eclipse project files
retrieveManaged := true

// Avoid generating eclipse source entries for the java directories
(unmanagedSourceDirectories in Compile) <<= (scalaSource in Compile)(Seq(_))

(unmanagedSourceDirectories in Test) <<= (scalaSource in Test)(Seq(_))
