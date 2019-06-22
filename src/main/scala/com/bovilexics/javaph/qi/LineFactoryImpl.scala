package com.bovilexics.javaph.qi

final class LineFactoryImpl extends LineFactory
{
  @throws[QiProtocolException]
  override def create(buffer: String): Line =
  {
    val verbatim = buffer
    /*
        TODO : this code doesn't deal with all of the scenarios listed here

        ph myname return what
        -507:what:unknown field.
        500:Did not understand ph.

        :
        598:::Command not recognized.

        what:
        598:what::Command not recognized.

        what
        598:what:Command not recognized.

        ph myname return email
        102:There was 1 match to your request.
        -200:1:    email: myemail
        200:Ok.

        quit
        200:Bye!
        */
    // Get the result code.
    // Index of first colon --> -200:
    val colon1Index = verbatim.indexOf(':')
    if (colon1Index == -1)
    {
      throw new QiProtocolException(verbatim)
    }
    val code = getCode(verbatim, colon1Index)
    // Get the index count, if there is one.
    // Index of second colon --> -200:1:
    val colon2Index = verbatim.indexOf(':', colon1Index + 1)
    if (colon2Index == -1)
    { // This isn't a field:value response but rather a one line description.
      // Just record it and return.
      val description = verbatim.substring(colon1Index + 1)
      return new LineImpl(code, verbatim, description)
    }
    var index = 0
    try
      {
        index = verbatim.substring(colon1Index + 1, colon2Index).toInt
      }
    catch
    {
      case _: NumberFormatException =>
      {
        val description = verbatim.substring(colon1Index + 1)
        return new LineImpl(code, verbatim, description)
      }
    }
    // This should be a field:value response.
    // Get field, value and return.
    // Index of third colon --> -200:1:    email:
    val colon3Index = verbatim.indexOf(':', colon2Index + 1)
    if (colon3Index == -1)
    {
      throw new QiProtocolException(verbatim)
    }
    val field = verbatim.substring(colon2Index + 1, colon3Index)
    val value = verbatim.substring(colon3Index + 1)
    new LineImpl(code, verbatim, field, value, "", index)
  }

  @throws[QiProtocolException]
  private def getCode(verbatim: String, colon1Index: Int) = try
    {
      verbatim.substring(0, colon1Index).toInt
    }
  catch
  {
    case _: NumberFormatException =>
    throw new QiProtocolException(verbatim)
  }
}