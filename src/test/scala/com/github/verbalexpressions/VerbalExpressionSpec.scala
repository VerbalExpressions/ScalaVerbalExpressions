package com.github.verbalexpressions

import org.specs2.mutable._

class VerbalExpressionSpec extends Specification {

  "The 'VerbalExpression' class" should {

    "have a simple contructor" in {
      val e = VerbalExpression()
      e.toString must be empty
    }

    "have take a prefix in the contructor" in {
      val e = VerbalExpression("prefix", "", "", 0)
      e.toString must beEqualTo("prefix")
    }

    "have take an expression in the contructor" in {
      val e = VerbalExpression("", "expr", "", 0)
      e.toString must beEqualTo("expr")
    }

    "have take a suffix in the contructor" in {
      val e = VerbalExpression("", "", "suffix", 0)
      e.toString must beEqualTo("suffix")
    }
  }

  "add()" should {
    "add a random string to expression" in {
      VerbalExpression().add("add").toString must beEqualTo("add")
    }

    "append to expression" in {
      VerbalExpression("", "first", "", 0)
        .add("second")
        .toString must beEqualTo("firstsecond")
    }
  }

  "startOfLine()" should {

    "add a 'start of line' character" in {
      VerbalExpression().startOfLine().toString must beEqualTo("^")
    }

    "add the character to the beginning" in {
      VerbalExpression("", "expr", "", 0)
        .startOfLine()
        .toString must beEqualTo("^expr")
    }

    "toogle a start of line character" in {
      VerbalExpression()
        .startOfLine(true)
        .startOfLine(false)
        .toString must beEqualTo("")
    }

    "have a startOfLine(false) method to remove a start of line character" in {
      VerbalExpression("^", "expr", "", 0)
        .startOfLine(false)
        .toString must beEqualTo("expr")
    }
  }

  "endOfLine()" should {

    "add a end of line character" in {
      VerbalExpression()
        .endOfLine()
        .toString must beEqualTo("$")
    }

    "have a endOfLine(boolean) method to toogle a start of line character" in {
      VerbalExpression()
        .endOfLine(true)
        .endOfLine(false)
        .toString must beEqualTo("")
    }

    "have a endOfLine(false) method to remove a start of line character" in {
      VerbalExpression()
        .endOfLine(false)
        .toString must beEqualTo("")
    }
  }

  "andThen()" should {

    "add quoted string to expression" in {
      VerbalExpression()
        .andThen("andThen")
        .toString must beEqualTo("(\\QandThen\\E)")
    }

    "append to expression" in {
       VerbalExpression("", "before", "", 0)
        .andThen("andThen")
        .toString must beEqualTo("before(\\QandThen\\E)")
    }

    "be aliased to find()" in {
       VerbalExpression()
        .find("find")
        .toString must beEqualTo("(\\Qfind\\E)")
    }
  }

  "maybe()" should {

    "add a quoted string with question mark to expression" in {
      VerbalExpression()
        .maybe("maybe")
        .toString must beEqualTo("(\\Qmaybe\\E)?")
    }

    "append to the expression" in {
      VerbalExpression("", "before", "", 0)
        .maybe("maybe")
        .toString must beEqualTo("before(\\Qmaybe\\E)?")
    }
  }

  "anything()" should {
    "add 'catch all' to expression" in {
       VerbalExpression()
        .anything()
        .toString must beEqualTo("(.*)")
    }
  }

  "anythingBut()" should {
    "add expression for block characters" in {
       VerbalExpression()
        .anythingBut("this")
        .toString must beEqualTo("([^\\Qthis\\E]*)")
     }
  }

  "something()" should {
    "add expression for any one character" in {
       VerbalExpression()
        .something()
        .toString must beEqualTo("(.+)")
     }
  }

  "somethingBut()" should {
    "add expression for block any one character" in {
       VerbalExpression()
        .somethingBut("this")
        .toString must beEqualTo("([^\\Qthis\\E]+)")
     }
  }

  "replace()" should {

    "replace with one character" in {
       VerbalExpression()
        .add("a")
        .replace("Magnus", "u")
        .toString must beEqualTo("Mugnus")
     }

     "replace source based on the expression" in {
       VerbalExpression()
        .range("a", "n")
        .replace("Magnus", "u")
        .toString must beEqualTo("Muuuus")
     }
  }


  "range()" should {

    "accept to and from" in {
      VerbalExpression()
        .range(0, 3)
        .toString must beEqualTo("[0-3]")
    }

    "accept array of ranges" in {
      VerbalExpression()
        .range(Array(0, 3))
        .toString must beEqualTo("[0-3]")
    }

    "accept array of ranges" in {
      VerbalExpression()
        .range(Array(0, 3, 4, 6))
        .toString must beEqualTo("[0-34-6]")
    }
  }

  "or()" should {

    "append a new expression after an |" in {
      VerbalExpression()
        .range(0, 3)
        .or(VerbalExpression().range(6, 9))
        .toString must beEqualTo("([0-3])|([6-9])")
    }
    "append a new expression after an |" in {
      VerbalExpression()
        .add("aa")
        .or(VerbalExpression().add("bb"))
        .toString must beEqualTo("(aa)|(bb)")
    }

    "append a new expression after an |" in {
      VerbalExpression()
        .add("aa")
        .or("bb")
        .toString must beEqualTo("(aa)|(bb)")
    }
  }

  "test()" should {

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
        .beginCapture()
        .range(Array(0, 3))
        .endCapture()
        .toString must beEqualTo("([0-3])")
    }
  }

  "URL https://www.google.com" should {

    "be a valid url" in {
      val testMe = "https://www.google.com"
      VerbalExpression()
        .startOfLine()
        .andThen( "http" )
        .maybe( "s" )
        .andThen( "://" )
        .maybe( "www." )
        .anythingBut( " " )
        .endOfLine()
        .test( testMe ) must beTrue
    }
  }
}
