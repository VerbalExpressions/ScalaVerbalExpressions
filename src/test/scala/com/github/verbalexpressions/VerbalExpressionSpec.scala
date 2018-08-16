package com.github.verbalexpressions

import org.specs2.mutable._

final class VerbalExpressionSpec extends Specification {

  "The 'VerbalExpression' class" should {
    "have a simple constructor" in {
      VerbalExpression().toString must beEmpty
    }

    "have take a prefix in the constructor" in {
      VerbalExpression("prefix").toString mustEqual "prefix"
    }

    "have take an expression in the constructor" in {
      VerbalExpression(expression = "test").toString mustEqual "test"
    }

    "have take a suffix in the constructor" in {
      VerbalExpression(suffix = "suffix").toString mustEqual "suffix"
    }
  }

  "add" should {
    "add a random string to expression" in {
      VerbalExpression().add("add").toString mustEqual "add"
    }

    "append to expression" in {
      VerbalExpression(expression = "first")
        .add("second")
        .toString mustEqual "firstsecond"
    }
  }

  "startOfLine" should {
    "add a 'start of line' character" in {
      VerbalExpression().startOfLine().toString mustEqual "^"
    }

    "add the character to the beginning" in {
      VerbalExpression(expression = "expr").startOfLine().toString mustEqual "^expr"
    }

    "toogle a start of line character" in {
      VerbalExpression()
        .startOfLine(true)
        .startOfLine(false)
        .toString mustEqual ""
    }

    "have a startOfLine(false) method to remove a start of line character" in {
      VerbalExpression("^", "expr")
        .startOfLine(false)
        .toString mustEqual "expr"
    }

    "be turned on with 'mustStartLine'" in {
      VerbalExpression().mustStartLine().toString mustEqual "^"
    }

    "be turned off with 'lineStartNotMandatory'" in {
      VerbalExpression("^", "expr")
        .lineStartNotMandatory()
        .toString mustEqual "expr"
    }
  }

  "endOfLine" should {
    "add a end of line character" in {
      VerbalExpression()
        .endOfLine()
        .toString mustEqual "$"
    }

    "have a endOfLine(boolean) method to toggle a start of line character" in {
      VerbalExpression()
        .endOfLine(true)
        .endOfLine(false)
        .toString mustEqual ""
    }

    "have a endOfLine(false) method to remove a start of line character" in {
      VerbalExpression()
        .endOfLine(false)
        .toString mustEqual ""
    }
  }

  "andThen" should {
    "add quoted string to expression" in {
      VerbalExpression()
        .andThen("andThen")
        .toString mustEqual "(\\QandThen\\E)"
    }

    "append to expression" in {
       VerbalExpression("", "before")
        .andThen("andThen")
        .toString mustEqual "before(\\QandThen\\E)"
    }

    "be aliased to find" in {
       VerbalExpression()
        .find("find")
        .toString mustEqual "(\\Qfind\\E)"
    }
  }

  "maybe" should {
    "add a quoted string with question mark to expression" in {
      VerbalExpression()
        .maybe("maybe")
        .toString mustEqual "(\\Qmaybe\\E)?"
    }

    "append to the expression" in {
      VerbalExpression("", "before")
        .maybe("maybe")
        .toString mustEqual "before(\\Qmaybe\\E)?"
    }
  }

  "anything" should {
    "add 'catch all' to expression" in {
       VerbalExpression()
        .anything
        .toString mustEqual "(.*)"
    }
  }

  "anythingBut" should {
    "add expression for block characters" in {
       VerbalExpression()
        .anythingBut("this")
        .toString mustEqual "([^\\Qthis\\E]*)"
     }
  }

  "something" should {
    "add expression for any one character" in {
       VerbalExpression()
        .something
        .toString mustEqual "(.+)"
     }
  }

  "somethingBut" should {
    "add expression for block any one character" in {
       VerbalExpression()
        .somethingBut("this")
        .toString mustEqual "([^\\Qthis\\E]+)"
     }
  }

  "replace" should {
    "replace with one character" in {
       VerbalExpression()
        .add("a")
        .replace("Magnus", "u") mustEqual "Mugnus"
     }

     "replace source based on the expression" in {
       VerbalExpression()
        .range("a" -> "n")
        .replace("Magnus", "u") mustEqual "Muuuus"
     }
  }


  "range" should {
    "accept to and from" in {
      VerbalExpression()
        .range(0 -> 3)
        .toString mustEqual "[0-3]"
    }

    "accept array of ranges" in {
      VerbalExpression()
        .range(0 -> 3, 4 -> 6)
        .toString mustEqual "[0-34-6]"
    }
  }

  "or" should {
    "append a new expression after an |" in {
      VerbalExpression()
        .range(0 -> 3)
        .or(VerbalExpression().range(6 -> 9))
        .toString mustEqual "([0-3])|([6-9])"
    }
    "append a new expression after an |" in {
      VerbalExpression()
        .add("aa")
        .or(VerbalExpression().add("bb"))
        .toString mustEqual "(aa)|(bb)"
    }

    "append a new expression after an |" in {
      VerbalExpression()
        .add("aa")
        .or("bb")
        .toString mustEqual "(aa)|(\\Qbb\\E)"
    }
  }

  "test" should {
    "match a given string to the build expression" in {
      VerbalExpression()
        .startOfLine()
        .multiple("a")
        .endOfLine()
        .test("baaaa") must beFalse
    }
    "match a given string to the build expression" in {
      VerbalExpression()
        .startOfLine()
        .multiple("a")
        .endOfLine()
        .test("aaaa") must beTrue
    }
  }

  "capturing" should {
    "allow for freetyping between parentensis" in {
      VerbalExpression()
        .beginCapture
        .range(0 -> 3)
        .endCapture
        .toString mustEqual "([0-3])"
    }
  }

  "README examples" should {
    "work" in {
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

      ok
    }
  }


  "zeroOrMore" should {
    "add asterisk with the given value" in {
      VerbalExpression()
        .zeroOrMore("v")
        .toString mustEqual "(\\Qv\\E)*"

      VerbalExpression()
        .find("abc")
        .zeroOrMore("d")
        .toString mustEqual "(\\Qabc\\E)(\\Qd\\E)*"

    }
  }

  "oneOrMore" should {
    "add a + with the given value" in {
      VerbalExpression()
        .oneOrMore("4")
        .toString mustEqual "(\\Q4\\E)+"
    }

  }

}
