package com.bovilexics.javaph.main

import com.bovilexics.javaph.JavaPH
import com.bovilexics.javaph.configuration.PropertyCollectionImpl
import com.bovilexics.javaph.qi.{ConnectionFactory, FieldFactory, QiConnectionFactory, QiFieldFactory, QiServerManager, ServerFactory, ServerFactoryImpl, ServerManager}

object Main {

  def main(args: Array[String]): Unit = {
    val defaultProperties = new PropertyCollectionImpl
    val properties = new PropertyCollectionImpl
    val factory : FieldFactory = new QiFieldFactory
    val serverFactory : ServerFactory = new ServerFactoryImpl(factory)
    val connectionFactory : ConnectionFactory = new QiConnectionFactory(serverFactory)
    val serverManager: ServerManager = new QiServerManager(connectionFactory, factory, serverFactory)
    val javaPh = new JavaPH(serverManager, defaultProperties, properties)
    javaPh.getQueryComboBox.getEditor.getEditorComponent.requestFocus()
  }
}
