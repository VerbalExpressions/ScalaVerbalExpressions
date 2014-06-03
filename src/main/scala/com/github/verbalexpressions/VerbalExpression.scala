package com.github.verbalexpressions

case class VerbalExpression(prefixes: String, expr: String, suffixes: String, modifiers: Int) {
  import java.util.regex.Pattern

  private[this] def sanitize(value: String) = if(value == null) value else Pattern.quote(value)

  def add(value: String) = VerbalExpression(prefixes, expr + value, suffixes, modifiers)

  def startOfLine(enable: Boolean = true) = VerbalExpression(if (enable) "^" else "", expr, suffixes, modifiers)

  def endOfLine(enable: Boolean = true) = VerbalExpression(prefixes, expr, if (enable) "$" else "", modifiers)

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

  private def charModToInt(modifier: Char) = modifier match {
    case 'd' => Pattern.UNIX_LINES
    case 'i' => Pattern.CASE_INSENSITIVE
    case 'x' => Pattern.COMMENTS
    case 'm' => Pattern.MULTILINE
    case 's' => Pattern.DOTALL
    case 'u' => Pattern.UNICODE_CASE
    case 'U' => Pattern.UNICODE_CHARACTER_CLASS
  }

  def addModifier(modifier: Char) = VerbalExpression(prefixes, expr, suffixes, modifiers | charModToInt(modifier))

  def removeModifier(modifier: Char) = VerbalExpression(prefixes, expr, suffixes, modifiers ^ charModToInt(modifier))

  def withAnyCase(enable: Boolean = true) = if (enable) addModifier('i') else removeModifier('i')

  def searchOneLine(enable: Boolean):VerbalExpression = if (enable) removeModifier('m') else addModifier('m')

  def multiple(value: String) = add(s"${sanitize(value)}+")

  def or(value: String) = VerbalExpression("(" + prefixes, add(")|(").add(value).expr, ")" + suffixes, modifiers)

  def or(value: VerbalExpression): VerbalExpression = or(value.expr)

  def test(toTest: String) = compile.matcher(toTest).matches()

  def compile = Pattern.compile(toString, modifiers)

  def beginCapture = add("(")

  def endCapture = add(")")

  override def toString = prefixes + expr + suffixes
}

object VerbalExpression {
  def apply():VerbalExpression = VerbalExpression("", "", "", 0)
}
