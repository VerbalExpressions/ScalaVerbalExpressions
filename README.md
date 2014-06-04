[![Build Status](https://travis-ci.org/pathikrit/ScalaVerbalExpressions.png?branch=master)](http://travis-ci.org/pathikrit/ScalaVerbalExpressions) [![Coverage Status](https://coveralls.io/repos/pathikrit/ScalaVerbalExpressions/badge.png)](https://coveralls.io/r/pathikrit/ScalaVerbalExpressions)

ScalaVerbalExpressions
=====================

```scala
import com.github.verbalexpressions.VerbalExpression

val urlTester = VerbalExpression()
                .startOfLine()
                .andThen("http")
                .maybe("s")
                .then("://")
                .maybe("www.")
                .anythingBut(" ")
                .endOfLine()

val someUrl = "https://www.google.com"

val result = if (urlTester test someUrl) "Valid" else "Invalid"

println(s"$someUrl is $result")
```  

For more methods, checkout the [wiki](https://github.com/VerbalExpressions/JSVerbalExpressions/wiki) and the [source](src/main/scala/com/github/verbalexpressions/VerbalExpression.scala)

sbt
===
Add the following to your `build.sbt`:
```scala
resolvers += "Sonatype releases" at "http://oss.sonatype.org/content/repositories/releases/"

libraryDependency += "com.github.verbalexpressions" %% "ScalaVerbalExpressions" % "0.0.1"
```
