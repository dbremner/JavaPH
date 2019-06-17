package com.bovilexics.javaph.qi

import org.jetbrains.annotations.Contract

final class ServerFactoryImpl(val fieldFactory: FieldFactory) extends ServerFactory
{
  private val PORT_ERROR = "Error: invalid port value passed into QiServer, must be a an integer between 0 and 65,535"

  override def create(name: String, server: String, port: Int): Server =
  {
    if (!isValidPort(port))
      throw new IllegalArgumentException(PORT_ERROR)
    new QiServer(fieldFactory, name, server, port)
  }

  override def create(name: String, server: String, port: String) : Server =
    new QiServer(fieldFactory, name, server, convertToPort(port))

  private def convertToPort(aPort: String) =
    try
    {
      val aPortInteger = aPort.toInt
      if (!isValidPort(aPortInteger))
        throw new IllegalArgumentException(PORT_ERROR)
      aPortInteger
    }
    catch
    {
      case e: NumberFormatException =>
        throw new IllegalArgumentException(PORT_ERROR)
    }

  @Contract(pure = true) def isValidPort(port: Int): Boolean =
    port >= 0 && port <= 65535
}