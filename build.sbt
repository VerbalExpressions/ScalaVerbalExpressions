import SonatypeKeys._

name := "ScalaVerbalExpression"

version := "1.0.0"

description := "VerbalExpressions in Scala"

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

organization := "com.github.verbalexpressions"

scalaVersion := "2.11.1"

crossScalaVersions := Seq("2.10.4", "2.11.1")

scalacOptions ++= Seq(
  "-unchecked", "-deprecation", "-feature",
  "-language:postfixOps,implicitConversions,experimental.macros,dynamics,existentials,higherKinds"
)

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "2.3.12" % "test"
)

sonatypeSettings

instrumentSettings

CoverallsPlugin.coverallsSettings

autoCompilerPlugins := true

pomExtra := {
  <url>http://github.com/pathikrit/ScalaVerbalExpressions</url>
  <scm>
    <url>git@github.com:pathikrit/ScalaVerbalExpressions.git</url>
    <connection>scm:git:git@github.com:pathikrit/ScalaVerbalExpressions.git</connection>
  </scm>
  <developers>
    <developer>
      <id>pathikrit</id>
      <name>Pathikrit Bhowmick</name>
      <url>http://github.com/pathikrit</url>
    </developer>
  </developers>
}
