package com.bovilexics.javaph.qi

import com.bovilexics.javaph.qi.ServerFactoryImpl.PORT_ERROR
import org.jetbrains.annotations.Contract

import scala.util.Try

object ServerFactoryImpl
{
  private val PORT_ERROR = "Error: invalid port value passed into QiServer, must be a an integer between 0 and 65,535"

  @Contract(pure = true)
  private def isValidPort(port: Int): Boolean =
    port >= 0 && port <= 65535

  private def toPort(port: String): Option[Int] =
    Try(port.toInt).toOption.flatMap(toPort)

  private def toPort(port: Int): Option[Int] =
    Some(port).filter(isValidPort)
}

final class ServerFactoryImpl(val fieldFactory: FieldFactory, val lineFactory: LineFactory) extends ServerFactory
{
  @throws[IllegalArgumentException]
  override def create(name: String, server: String, port: Int): Server =
  {
    ServerFactoryImpl.toPort(port) match
    {
      case Some(value) => new QiServer(fieldFactory, lineFactory, name, server, value)
      case None => throw new IllegalArgumentException(PORT_ERROR)
    }
  }

  @throws[IllegalArgumentException]
  override def create(name: String, server: String, port: String) : Server =
  {
    ServerFactoryImpl.toPort(port) match
    {
      case Some(value) => new QiServer(fieldFactory, lineFactory, name, server, value)
      case None => throw new IllegalArgumentException(PORT_ERROR)
    }
  }
}