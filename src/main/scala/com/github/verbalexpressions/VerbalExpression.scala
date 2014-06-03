package com.github.verbalexpressions

case class VerbalExpression(prefix: String = "", expression: String = "", suffix: String = "", modifiers: Int = 0) {
  import java.util.regex.Pattern
  import java.util.regex.Pattern.quote

  def replace(source: String, value: String) = source.replaceAll(toString, value)

  def add(value: String) = copy(expression = expression + value)

  def andThen(value: String) = add(s"(${quote(value)})")

  def then = andThen _

  def find = andThen _

  def maybe(value: String) = add(s"(${quote(value)})?")

  def anything = add("(.*)")

  def anythingBut(value: String) = add(s"([^${quote(value)}]*)")

  def something = add("(.+)")

  def somethingBut(value: String) = add(s"([^${quote(value)}]+)")

  def tab = add("\\t")

  def word = add("\\w+")

  def anyOf(value: String) = add(s"[${quote(value)}]")

  def any = anyOf _

  def lineBreak = add("(\\n|(\\r\\n))")

  def br = lineBreak

  def startOfLine(enable: Boolean = true) = copy(prefix = if (enable) "^" else "")

  def endOfLine(enable: Boolean = true) = copy(suffix = if (enable) "$" else "")

  def or(value: String) = copy("(" + prefix, expression + ")|(" + value, ")" + suffix)

  def or(value: VerbalExpression): VerbalExpression = or(value.expression)

  def range(args: Any*) = add(s"[${(args grouped 2 map {_ mkString "-"}).mkString}]")

  private[this] val charModToInt = Map(
    'd' -> Pattern.UNIX_LINES,
    'i' -> Pattern.CASE_INSENSITIVE,
    'x' -> Pattern.COMMENTS,
    'm' -> Pattern.MULTILINE,
    's' -> Pattern.DOTALL,
    'u' -> Pattern.UNICODE_CASE,
    'U' -> Pattern.UNICODE_CHARACTER_CLASS
  )

  def modify(modifier: Char, add: Boolean) =  copy(modifiers = if (add) modifiers | charModToInt(modifier) else modifiers ^ charModToInt(modifier))

  def addModifier(modifier: Char) = modify(modifier, true)

  def removeModifier(modifier: Char) = modify(modifier, false)

  def withAnyCase(enable: Boolean = true) = modify('i', enable)

  def searchOneLine(enable: Boolean = true) = modify('m', enable)

  def stopAtFirst(enable: Boolean = false) = modify('g', enable)

  def repeatPrevious(n: Int) = ???

  def repeatPrevious(atleast: Int, atmost: Int) = ???

  def multiple(value: String) = add(s"${quote(value)}+")

  def beginCapture = add("(")

  def endCapture = add(")")

  def pattern = Pattern.compile(prefix + expression + suffix, modifiers)

  def test(toTest: String) = Pattern.matches(toString, toTest)

  override def toString = pattern.pattern()
}
