package com.bovilexics.javaph.qi

final class ConnectionFactoryImpl(val serverFactory: ServerFactory, val lineFactory: LineFactory) extends ConnectionFactory
{
  /**
    * Creates a QiConnection with an which will connect to host:port
    * once the <b>connect</b> method is called.
    * <i>connect(host, port)</i>
    *
    * @param aHost to connect to
    * @param aPort to connect to.
    */
  override def create(aHost: String, aPort: Int): Connection =
  {
    val server = serverFactory.create("unspecified", aHost, aPort)
    create(server)
  }

  /**
    * Creates a QiConnection from a QiServer object which must then be initialized using
    * <b>connect(host, port)</b>
    */
  override def create(server: Server): Connection =
    new QiConnection(server.getExpandedName, server.getServer, server.getPort, lineFactory)
}
