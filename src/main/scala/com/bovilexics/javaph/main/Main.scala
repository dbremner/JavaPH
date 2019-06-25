package com.bovilexics.javaph.main

import com.bovilexics.javaph.JavaPH
import com.bovilexics.javaph.configuration.PropertyCollectionImpl
import com.bovilexics.javaph.qi.{CommandImpl, ConnectionFactory, ConnectionFactoryImpl, FieldFactory, FieldFactoryImpl, LineFactory, LineFactoryImpl, QiServerManager, ServerFactory, ServerFactoryImpl, ServerManager}
import com.bovilexics.javaph.ui.{IconProvider, IconProviderImpl}

object Main
{
  def main(args: Array[String]): Unit =
  {
    val defaultProperties = new PropertyCollectionImpl
    val properties = new PropertyCollectionImpl
    val factory : FieldFactory = new FieldFactoryImpl
    val lineFactory : LineFactory = new LineFactoryImpl
    val serverFactory : ServerFactory = new ServerFactoryImpl(factory, lineFactory)
    val connectionFactory : ConnectionFactory = new ConnectionFactoryImpl(serverFactory, lineFactory)
    val serverManager: ServerManager = new QiServerManager(connectionFactory, serverFactory)
    val iconProvider : IconProvider = new IconProviderImpl
    val commands = CommandImpl.commands
    val javaPh = new JavaPH(commands, iconProvider, serverManager, defaultProperties, properties)
    javaPh.getQueryComboBox.getEditor.getEditorComponent.requestFocus()
  }
}
