package com.github.verbalexpressions

import java.util.regex.Pattern
import com.github.verbalexpressions.VerbalExpression._

final case class VerbalExpression(prefix: String = "", expression: String = "", suffix: String = "", modifiers: Int = 0) {

  private[verbalexpressions] def add(value: String) = copy(expression = expression + value)

  def andThen(value: StringOrVerbalExp) = add(s"($value)")
  def `then` = andThen _
  def find = andThen _

  def maybe(value: StringOrVerbalExp) = add(s"($value)?")

  def zeroOrMore(value: StringOrVerbalExp) = add(s"($value)*")

  def oneOrMore(value: StringOrVerbalExp) = add(s"($value)+")
  def multiple = oneOrMore _
  def many = oneOrMore _

  def anything = add("(.*)")
  def anythingBut(value: StringOrVerbalExp) = add(s"([^$value]*)")

  def something = add("(.+)")
  def somethingBut(value: StringOrVerbalExp) = add(s"([^$value]+)")

  def whitespaces(enable: Boolean = true) = add(if (enable) "\\s+" else "\\S+")
  def words(enable: Boolean = true) = add(if (enable) "\\w+" else "\\W+")
  def digits(enable: Boolean = true) = add(if (enable) "\\d+" else "\\D+")

  def anyOf(value: StringOrVerbalExp) = add(s"[$value]")
  def any = anyOf _

  def lineBreak = add("(\\n|(\\r\\n))")
  def startOfLine(enable: Boolean = true) = copy(prefix = if (enable) "^" else "")
  def endOfLine(enable: Boolean = true) = copy(suffix = if (enable) "$" else "")

  def or(value: StringOrVerbalExp) = copy("(" + prefix, expression + ")|(" + value, ")" + suffix)

  def range(args: (Any, Any)*) = add(s"[${(args map {case (a, b) => s"$a-$b"}).mkString}]")

  def addModifier(modifier: Modifier) = copy(modifiers = modifiers | modifier.mask)
  def removeModifier(modifier: Modifier) = copy(modifiers = modifiers ^ modifier.mask)

  def modify(modifier: Modifier, add: Boolean) = if (add) addModifier(modifier) else removeModifier(modifier)

  def withAnyCase(enable: Boolean = true) = modify(CaseInsensitive, enable)
  def searchOneLine(enable: Boolean = true) = modify(MultiLine, enable)

  def repeat(n: Int) = add(s"{$n}")
  def repeat(atleast: Int, atmost: Int) = add(s"{$atleast,$atmost}")
  def repeatAtleast(n: Int) = add(s"{$n,}")

  def beginCapture = add("(")
  def endCapture = add(")")

  def reluctant = add("?")
  def possessive = add("+")

  def replace(source: String, value: String) = source.replaceAll(toString, value)

  lazy val compile = Pattern.compile(prefix + expression + suffix, modifiers)
  def regexp = compile.pattern()

  def test = Pattern.matches(toString, _: String)
  def check = test
  def notMatch(test: String) = !check(test)

  override def toString = regexp
}

object VerbalExpression {
  sealed trait StringOrVerbalExp
  implicit def lift(s: String): StringOrVerbalExp = new StringOrVerbalExp {override def toString = Pattern.quote(s)}
  implicit def lift(v: VerbalExpression): StringOrVerbalExp = new StringOrVerbalExp {override def toString = v.regexp}

  sealed abstract class Modifier(val mask: Int)
  object UnixLines extends Modifier(Pattern.UNIX_LINES)
  object CaseInsensitive extends Modifier(Pattern.CASE_INSENSITIVE)
  object Comments extends Modifier(Pattern.COMMENTS)
  object MultiLine extends Modifier(Pattern.MULTILINE)
  object Dotall extends Modifier(Pattern.DOTALL)
  object UnixCase extends Modifier(Pattern.UNICODE_CASE)
  //object UnicodeCharacterClass extends(Pattern.UNICODE_CHARACTER_CLASS)

  implicit class StringMatchUtils(s: String) {
    def is(expr: VerbalExpression) = expr check s
    def matches = is _
    def isNot(expr: VerbalExpression) = expr notMatch s
  }

  val $ = VerbalExpression()
}
