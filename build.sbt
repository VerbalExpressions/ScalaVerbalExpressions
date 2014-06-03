import SonatypeKeys._

name := "ScalaVerbalExpression"

version := "0.0.1"

description := "VerbalExpressions in Scala"

homepage := Some(url("http://github.com/pathikrit/ScalaVerbalExpression"))

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

organization := "com.github.verbalexpressions"

scalaVersion := "2.11.1"

crossScalaVersions := Seq("2.10.4", "2.11.1")

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "2.3.12" % "test"
)

sonatypeSettings

instrumentSettings

CoverallsPlugin.coverallsSettings

CoverallsPlugin.CoverallsKeys.coverallsToken := Some("KaV31nMn63pJSb6hSLsbT97lfgBtB8VEL")

autoCompilerPlugins := true

pomExtra := {
  <url>http://github.com/pathikrit/ScalaVerbalExpression</url>
    <licenses>
      <license>
        <name>The MIT License</name>
        <url>http://www.opensource.org/licenses/MIT</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:pathikrit/ScalaVerbalExpression.git</url>
      <connection>scm:git:git@github.com:pathikrit/ScalaVerbalExpression.git</connection>
    </scm>
    <developers>
      <developer>
        <id>pathikrit</id>
        <name>Pathikrit Bhowmick</name>
        <url>http://github.com/pathikrit</url>
      </developer>
    </developers>
}
