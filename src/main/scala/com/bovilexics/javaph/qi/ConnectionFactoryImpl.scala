package com.bovilexics.javaph.qi

final class ConnectionFactoryImpl(val serverFactory: ServerFactory, val lineFactory: LineFactory) extends ConnectionFactory
{
  /**
    * Creates a QiConnection from a QiServer object which must then be initialized using
    * <b>connect(host, port)</b>
    */
  override def create(server: Server): Connection =
    new QiConnection(server.getExpandedName, server.getServer, server.getPort, lineFactory)
}
