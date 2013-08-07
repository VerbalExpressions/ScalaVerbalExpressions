
name := "ScalaVerbalExpression"

version := "0.0.1"

scalaVersion := "2.10.2"


libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "2.1.1" % "test"
)

scalacOptions in Test ++= Seq("-Yrangepos")

resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
                  "releases"  at "http://oss.sonatype.org/content/repositories/releases")