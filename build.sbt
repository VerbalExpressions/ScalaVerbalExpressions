name := "ScalaVerbalExpression"

version := "0.0.1"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "2.3.12" % "test"
)

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

instrumentSettings

CoverallsPlugin.coverallsSettings

CoverallsPlugin.CoverallsKeys.coverallsToken := Some("KaV31nMn63pJSb6hSLsbT97lfgBtB8VEL")

autoCompilerPlugins := true
