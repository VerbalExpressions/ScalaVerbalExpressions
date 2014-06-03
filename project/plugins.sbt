resolvers ++= Seq(
  Classpaths.sbtPluginReleases
)

addSbtPlugin("org.scoverage" %% "sbt-scoverage" % "0.99.5")

addSbtPlugin("com.sksamuel.scoverage" %% "sbt-coveralls" % "0.0.5")

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.1.6")
