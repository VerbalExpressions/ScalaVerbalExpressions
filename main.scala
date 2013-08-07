

object Application extends App {

  val tester = VerbalExpression()
            .startOfLine()
            .andThen( "http" )
            .maybe( "s" )
            .andThen( "://" )
            .maybe( "www." )
            .anythingBut( " " )
            .endOfLine()

  val testMe = "https://www.google.com"

  if( tester.test( testMe ) ) println( "We have a correct URL ") // This output will fire
  else println( "The URL is incorrect" )

  println( tester )
}