package com.bovilexics.javaph.main

import com.bovilexics.javaph.JavaPH
import com.bovilexics.javaph.configuration.PropertyCollectionImpl
import com.bovilexics.javaph.qi.{FieldFactory, QiFieldFactory, QiServerManager, ServerManager}

object Main {

  def main(args: Array[String]): Unit = {
    val defaultProperties = new PropertyCollectionImpl
    val properties = new PropertyCollectionImpl
    val factory : FieldFactory = new QiFieldFactory
    val serverManager: ServerManager = new QiServerManager(factory)
    val javaPh = new JavaPH(serverManager, defaultProperties, properties)
    javaPh.getQueryComboBox.getEditor.getEditorComponent.requestFocus()
  }
}
