[![Build Status](https://travis-ci.org/pathikrit/ScalaVerbalExpressions.png?branch=master)](http://travis-ci.org/pathikrit/ScalaVerbalExpressions) [![Coverage Status](https://coveralls.io/repos/pathikrit/ScalaVerbalExpressions/badge.png)](https://coveralls.io/r/pathikrit/ScalaVerbalExpressions)

ScalaVerbalExpressions
=====================

```scala
import com.github.verbalexpressions.VerbalExpression
import VerbalExpression._

val validUrl = $.startOfLine()
                .andThen("http")
                .maybe("s")
                .andThen("://")
                .maybe("www.")
                .anythingBut(" ")
                .endOfLine()

assert("https://www.google.com" is validUrl)
assert("ftp://home.comcast.net" isNot validUrl)

// VerbalExpressions can be nested within each other
val fraction = $.andThen(".").digits()
val number = $.maybe("-").digits().maybe(fraction)

assert(Seq("3", "-4", "-0.458") forall number.check)
assert(Seq("0.", "hello", "4.3.2") forall number.notMatch)
```

For more methods, checkout the [wiki](https://github.com/VerbalExpressions/JSVerbalExpressions/wiki) and the [source](src/main/scala/com/github/verbalexpressions/VerbalExpression.scala)

sbt
===
Add the following to your `build.sbt`:
```scala
resolvers += "Sonatype releases" at "http://oss.sonatype.org/content/repositories/releases/"

libraryDependency += "com.github.verbalexpressions" %% "ScalaVerbalExpressions" % "1.0.0"
```
