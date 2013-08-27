import java.util.regex.Pattern;

case class VerbalExpression(prefixes:String, expr:String, suffixes:String, modifiers:Int) {

  private def sanitize(value:String) =
      if(value == null) value
      else Pattern.quote(value)

  def add(value:String):VerbalExpression =
    VerbalExpression(prefixes, expr + value, suffixes, modifiers)

  def startOfLine(enable:Boolean):VerbalExpression =
    if(enable) VerbalExpression("^", expr, suffixes, modifiers)
    else VerbalExpression("", expr, suffixes, modifiers)

  def startOfLine():VerbalExpression = startOfLine(true)

  def endOfLine(enable:Boolean):VerbalExpression =
    if(enable) VerbalExpression(prefixes, expr, "$", modifiers)
    else VerbalExpression(prefixes, expr, "", modifiers)

  def endOfLine():VerbalExpression = endOfLine(true)

  def andThen(value:String):VerbalExpression = add(s"(${sanitize(value)})")

  def find(value:String):VerbalExpression = andThen(value)

  def maybe(value:String):VerbalExpression = add(s"(${sanitize(value)})?")

  def anything():VerbalExpression = add("(.*)")

  def anythingBut(value:String):VerbalExpression = add(s"([^${sanitize(value)}]*)")

  def something():VerbalExpression = add("(.+)")

  def somethingBut(value:String):VerbalExpression = add(s"([^${sanitize(value)}]+)")

  def replace(source:String, value:String):String =
    source.replaceAll(toString, value)

  def lineBreak():VerbalExpression = add("(\\n|(\\r\\n))")

  def br():VerbalExpression = lineBreak()

  def tab():VerbalExpression = add("\\t")

  def word():VerbalExpression = add("\\w+")

  def anyOf(value:String):VerbalExpression = add(s"[${sanitize(value)}]")

  def any(value:String):VerbalExpression = anyOf(value)

  def range(from:Any, to:Any):VerbalExpression = add(s"[$from-$to]")

  def range(args:Array[Any]):VerbalExpression = {
    val ranges = (0 to args.length - 1).filter( _ % 2 == 0).foldRight(List[String]()) { (from, res) =>
       args(from) + "-" + args(from+1) :: res
    }
    add(s"[${ranges.mkString}]")
  }

  private def charModToInt(modifier :Char) =
    modifier match {
      case 'd' => Pattern.UNIX_LINES
      case 'i' => Pattern.CASE_INSENSITIVE
      case 'x' => Pattern.COMMENTS
      case 'm' => Pattern.MULTILINE
      case 's' => Pattern.DOTALL
      case 'u' => Pattern.UNICODE_CASE
      case 'U' => Pattern.UNICODE_CHARACTER_CLASS
    }

  def addModifier(modifier :Char):VerbalExpression =
    VerbalExpression(prefixes, expr, suffixes, modifiers | charModToInt(modifier))

  def removeModifier(modifier :Char):VerbalExpression =
    VerbalExpression(prefixes, expr, suffixes, modifiers ^ charModToInt(modifier))

  def withAnyCase(enable:Boolean):VerbalExpression =
    if (enable) addModifier( 'i' )
    else removeModifier( 'i' )

  def withAnyCase():VerbalExpression = withAnyCase(true)

  def searchOneLine(enable:Boolean):VerbalExpression =
      if (enable) removeModifier( 'm' )
      else addModifier( 'm' )

  def multiple(value:String):VerbalExpression = add(s"${sanitize(value)}+")

  def or(value:String):VerbalExpression = {
      val newExpr = add(")|(") add(value)
      VerbalExpression("(" + prefixes, newExpr.expr, ")" + suffixes, modifiers)
  }

  def or(value:VerbalExpression):VerbalExpression = or(value.expr)

  def test(toTest:String) = {
    val p = Pattern.compile(prefixes + expr + suffixes, modifiers)
    Pattern.matches(p.pattern, toTest)
  }

  def beginCapture() = add("(")

  def endCapture() = add(")")

  override def toString() = prefixes + expr + suffixes

}

object VerbalExpression {
  def apply():VerbalExpression = VerbalExpression("", "", "", 0)
}