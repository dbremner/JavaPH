package com.bovilexics.javaph.main

import com.bovilexics.javaph.JavaPH

object Main {

  def main(args: Array[String]): Unit = {
    val javaPh = new JavaPH()
    javaPh.getQueryComboBox.getEditor.getEditorComponent.requestFocus()
  }
}
