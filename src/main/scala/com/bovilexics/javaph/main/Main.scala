package com.bovilexics.javaph.main

import com.bovilexics.javaph.JavaPH
import com.bovilexics.javaph.configuration.PropertyCollectionImpl
import com.bovilexics.javaph.qi.{ConnectionFactory, ConnectionFactoryImpl, FieldFactory, QiFieldFactory, QiServerManager, ServerFactory, ServerFactoryImpl, ServerManager}

object Main {

  def main(args: Array[String]): Unit = {
    val defaultProperties = new PropertyCollectionImpl
    val properties = new PropertyCollectionImpl
    val factory : FieldFactory = new QiFieldFactory
    val serverFactory : ServerFactory = new ServerFactoryImpl(factory)
    val connectionFactory : ConnectionFactory = new ConnectionFactoryImpl(serverFactory)
    val serverManager: ServerManager = new QiServerManager(connectionFactory, serverFactory)
    val javaPh = new JavaPH(serverManager, defaultProperties, properties)
    javaPh.getQueryComboBox.getEditor.getEditorComponent.requestFocus()
  }
}
