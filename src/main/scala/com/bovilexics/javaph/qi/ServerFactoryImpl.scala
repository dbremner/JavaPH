package com.bovilexics.javaph.qi

final class ServerFactoryImpl(val fieldFactory: FieldFactory) extends ServerFactory
{
  override def create(name: String, server: String, port: Int) = new QiServer(fieldFactory, name, server, port)

  override def create(name: String, server: String, port: String) = new QiServer(fieldFactory, name, server, port)
}