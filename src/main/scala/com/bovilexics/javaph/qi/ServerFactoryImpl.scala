package com.bovilexics.javaph.qi

import com.bovilexics.javaph.qi.ServerFactoryImpl.{PORT_ERROR, isValidPort}
import org.jetbrains.annotations.Contract


object ServerFactoryImpl
{
  private val PORT_ERROR = "Error: invalid port value passed into QiServer, must be a an integer between 0 and 65,535"

  @Contract(pure = true)
  private def isValidPort(port: Int): Boolean =
    port >= 0 && port <= 65535

  @throws[IllegalArgumentException]
  private def convertToPort(aPort: String) =
    try
    {
      val aPortInteger = aPort.toInt
      if (!isValidPort(aPortInteger))
      {
        throw new IllegalArgumentException(PORT_ERROR)
      }
      aPortInteger
    }
    catch
    {
      case _: NumberFormatException =>
        throw new IllegalArgumentException(PORT_ERROR)
    }
}

final class ServerFactoryImpl(val fieldFactory: FieldFactory, val lineFactory: LineFactory) extends ServerFactory
{
  @throws[IllegalArgumentException]
  override def create(name: String, server: String, port: Int): Server =
  {
    if (!isValidPort(port))
      throw new IllegalArgumentException(PORT_ERROR)
    new QiServer(fieldFactory, lineFactory, name, server, port)
  }

  @throws[IllegalArgumentException]
  override def create(name: String, server: String, port: String) : Server =
    new QiServer(fieldFactory, lineFactory, name, server, ServerFactoryImpl.convertToPort(port))
}