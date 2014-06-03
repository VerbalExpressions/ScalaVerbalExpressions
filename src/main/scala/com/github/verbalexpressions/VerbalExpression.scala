package com.github.verbalexpressions

case class VerbalExpression(prefixes: String = "", expression: String = "", suffixes: String = "", modifiers: Int = 0) {
  import java.util.regex.Pattern

  private[this] def sanitize(value: String) = if(value == null) value else Pattern.quote(value)

  def add(value: String) = VerbalExpression(prefixes, expression + value, suffixes, modifiers)

  def startOfLine(enable: Boolean = true) = VerbalExpression(if (enable) "^" else "", expression, suffixes, modifiers)

  def endOfLine(enable: Boolean = true) = VerbalExpression(prefixes, expression, if (enable) "$" else "", modifiers)

  def andThen(value: String) = add(s"(${sanitize(value)})")

  def find = andThen _

  def maybe(value: String) = add(s"(${sanitize(value)})?")

  def anything = add("(.*)")

  def anythingBut(value: String) = add(s"([^${sanitize(value)}]*)")

  def something = add("(.+)")

  def somethingBut(value: String) = add(s"([^${sanitize(value)}]+)")

  def replace(source: String, value: String) = source.replaceAll(toString, value)

  def lineBreak = add("(\\n|(\\r\\n))")

  def br = lineBreak

  def tab = add("\\t")

  def word = add("\\w+")

  def anyOf(value: String) = add(s"[${sanitize(value)}]")

  def any = anyOf _

  def range(from: Any, to: Any) = add(s"[$from-$to]")

  def range(args: Array[Any]) = {
    val ranges = (0 to args.length - 1).filter( _ % 2 == 0).foldRight(List[String]()) { (from, res) =>
       args(from) + "-" + args(from+1) :: res
    }
    add(s"[${ranges.mkString}]")
  }

  private[this] val charModToInt = Map(
    'd' -> Pattern.UNIX_LINES,
    'i' -> Pattern.CASE_INSENSITIVE,
    'x' -> Pattern.COMMENTS,
    'm' -> Pattern.MULTILINE,
    's' -> Pattern.DOTALL,
    'u' -> Pattern.UNICODE_CASE,
    'U' -> Pattern.UNICODE_CHARACTER_CLASS
  )

  def modify(modifier: Char, add: Boolean) = VerbalExpression(prefixes, expression, suffixes, if (add) modifiers | charModToInt(modifier) else modifiers ^ charModToInt(modifier))

  def addModifier(modifier: Char) = modify(modifier, true)

  def removeModifier(modifier: Char) = modify(modifier, false)

  def withAnyCase(enable: Boolean = true) = modify('i', enable)

  def searchOneLine(enable: Boolean = true) = modify('m', enable)

  def multiple(value: String) = add(s"${sanitize(value)}+")

  def or(value: String) = VerbalExpression("(" + prefixes, add(")|(").add(value).expression, ")" + suffixes, modifiers)

  def or(value: VerbalExpression): VerbalExpression = or(value.expression)

  def test = Pattern.matches(compile.pattern, _)

  def compile = Pattern.compile(toString, modifiers)

  def beginCapture = add("(")

  def endCapture = add(")")

  override def toString = prefixes + expression + suffixes
}
