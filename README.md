[![Build Status](https://travis-ci.org/pathikrit/ScalaVerbalExpressions.png?branch=master)](http://travis-ci.org/pathikrit/ScalaVerbalExpressions) [![Coverage Status](https://coveralls.io/repos/pathikrit/ScalaVerbalExpressions/badge.png)](https://coveralls.io/r/pathikrit/ScalaVerbalExpressions)

ScalaVerbalExpressions
=====================

```scala
import com.github.verbalexpressions.VerbalExpression
import VerbalExpression._

val fraction = $.andThen(".").digits()
val number = $.maybe("-").digits().maybe(fraction)

assert("3" is number)
assert("-4" is number)
assert("-4.58" is number, number.regexp)
assert("0." isNot number)
assert("hello" isNot number)
assert("4.3.2" isNot number)

val urlTester = $.startOfLine()
                 .andThen("http")
                 .maybe("s")
                 .andThen("://")
                 .maybe("www.")
                 .anythingBut(" ")
                 .endOfLine()

val someUrl = "https://www.google.com"

assert(urlTester test someUrl)
```

For more methods, checkout the [wiki](https://github.com/VerbalExpressions/JSVerbalExpressions/wiki) and the [source](src/main/scala/com/github/verbalexpressions/VerbalExpression.scala)

sbt
===
Add the following to your `build.sbt`:
```scala
resolvers += "Sonatype releases" at "http://oss.sonatype.org/content/repositories/releases/"

libraryDependency += "com.github.verbalexpressions" %% "ScalaVerbalExpressions" % "0.0.1"
```
