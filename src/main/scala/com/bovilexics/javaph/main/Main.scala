package com.bovilexics.javaph.main

import com.bovilexics.javaph.JavaPH
import com.bovilexics.javaph.configuration.PropertyCollectionImpl
import com.bovilexics.javaph.qi.{ConnectionFactory, ConnectionFactoryImpl, FieldFactory, LineFactory, QiFieldFactory, QiLineFactory, QiServerManager, ServerFactory, ServerFactoryImpl, ServerManager}
import com.bovilexics.javaph.ui.{IconProvider, IconProviderImpl}

object Main
{
  def main(args: Array[String]): Unit =
  {
    val defaultProperties = new PropertyCollectionImpl
    val properties = new PropertyCollectionImpl
    val factory : FieldFactory = new QiFieldFactory
    val lineFactory : LineFactory = new QiLineFactory
    val serverFactory : ServerFactory = new ServerFactoryImpl(factory, lineFactory)
    val connectionFactory : ConnectionFactory = new ConnectionFactoryImpl(serverFactory, lineFactory)
    val serverManager: ServerManager = new QiServerManager(connectionFactory, serverFactory)
    val iconProvider : IconProvider = new IconProviderImpl
    val javaPh = new JavaPH(iconProvider, serverManager, defaultProperties, properties)
    javaPh.getQueryComboBox.getEditor.getEditorComponent.requestFocus()
  }
}
