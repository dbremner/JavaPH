package com.bovilexics.javaph.main

import com.bovilexics.javaph.JavaPH
import com.bovilexics.javaph.configuration.PropertyCollectionImpl

object Main {

  def main(args: Array[String]): Unit = {
    val defaultProperties = new PropertyCollectionImpl
    val properties = new PropertyCollectionImpl
    val javaPh = new JavaPH(defaultProperties, properties)
    javaPh.getQueryComboBox.getEditor.getEditorComponent.requestFocus()
  }
}
