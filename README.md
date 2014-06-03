[![Build Status](https://travis-ci.org/pathikrit/ScalaVerbalExpressions.png?branch=master)](http://travis-ci.org/pathikrit/ScalaVerbalExpressions) [![Coverage Status](https://coveralls.io/repos/pathikrit/ScalaVerbalExpressions/badge.png)](https://coveralls.io/r/pathikrit/ScalaVerbalExpressions)

ScalaVerbalExpressions
=====================

```scala
val tester = VerbalExpression()
                .startOfLine()
                .andThen("http")
                .maybe("s")
                .andThen("://")
                .maybe("www.")
                .anythingBut(" ")
                .endOfLine()

val testMe = "https://www.google.com"

val result = if (tester.test(testMe)) "Valid" else "Invalid"

println(s"$testMe is $result url")
```  

For more methods checkout the [wiki](https://github.com/VerbalExpressions/JSVerbalExpressions/wiki) or the [source](src/main/scala/VerbalExpressions.scala)
