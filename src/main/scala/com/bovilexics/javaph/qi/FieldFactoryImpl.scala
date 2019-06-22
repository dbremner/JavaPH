package com.bovilexics.javaph.qi

import java.util.StringTokenizer

import com.google.common.collect.ImmutableList
import org.jetbrains.annotations.Contract

object FieldFactoryImpl
{
  /**
    * Parse a QI field property.
    *
    * @param someProperties The properties string which Qi returns in response to a
    *                       "fields" command. The protocol stuff should be stripped
    *                       leaving just the field description
    *                       (e.g. "max 64 Indexed Lookup Public Default Any")
    * @throws QiProtocolException in the event of an error parsing the data.
    * @return properties collection
    */
    @Contract(pure = true)
    @throws[QiProtocolException]
    def parseProperties(someProperties: String): ParseResult =
    {
      val tokenizer = new StringTokenizer(someProperties)
      val token = tokenizer.nextElement.asInstanceOf[String]
      var length = 0
      if (token.startsWith("max"))
      {
        val lengthString = tokenizer.nextElement.asInstanceOf[String]
        try
        {
          length = Integer.valueOf(lengthString)
        }
        catch
        {
          case _: NumberFormatException =>
          throw new QiProtocolException("Invalid value for max length property: " + someProperties)
        }
      }
      else
      {
        length = -1
      }
      val builder = new ImmutableList.Builder[String]
      // Okay, here come the properties...
      while(tokenizer.hasMoreElements)
      {
        builder.add(tokenizer.nextElement.asInstanceOf[String])
      }
      val list = builder.build
      new ParseResult(length, list)
    }

  final private class ParseResult private[qi](val length: Int, val properties: ImmutableList[String])
  {
    private[qi] def getLength = length

    private[qi] def getProperties = properties
  }

}

class FieldFactoryImpl extends FieldFactory
{
  @Contract(pure = true)
  @throws[QiProtocolException]
  override def create(name: String, someProperties: String, description: String): Field =
  {
    val parseResult = FieldFactoryImpl.parseProperties(someProperties)
    new FieldImpl(name, parseResult.getLength, parseResult.getProperties, description)
  }
}