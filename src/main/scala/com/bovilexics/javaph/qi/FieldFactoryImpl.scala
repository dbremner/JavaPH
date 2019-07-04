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
    @throws[IllegalArgumentException]
    def parseProperties(someProperties: String): (Int, ImmutableList[String]) =
    {
      val tokenizer = new StringTokenizer(someProperties)
      val token = tokenizer.nextToken
      var length = 0
      if (token.startsWith("max"))
      {
        val lengthString = tokenizer.nextToken
        try
        {
          length = Integer.valueOf(lengthString)
        }
        catch
        {
          case e: NumberFormatException =>
          throw new java.lang.IllegalArgumentException("Invalid value for max length property: " + someProperties, e)
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
        builder.add(tokenizer.nextToken)
      }
      val list = builder.build
      (length, list)
    }
}

final class FieldFactoryImpl extends FieldFactory
{
  @Contract(pure = true)
  override def create(name: String, someProperties: String, description: String): Field =
  {
    // TODO literal strings
    if (name.isEmpty)
    {
      throw new IllegalArgumentException("name may not be empty")
    }

    if (someProperties.isEmpty)
    {
      throw new IllegalArgumentException("someProperties may not be empty")
    }

    if (description.isEmpty)
    {
      throw new IllegalArgumentException("description may not be empty")
    }

    val parseResult = FieldFactoryImpl.parseProperties(someProperties)
    new FieldImpl(name, parseResult._1, parseResult._2, description)
  }
}