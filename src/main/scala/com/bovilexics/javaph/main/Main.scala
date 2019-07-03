package com.bovilexics.javaph.main

import java.io.{FileNotFoundException, IOException}

import com.bovilexics.javaph.configuration.PropertyCollectionImpl
import com.bovilexics.javaph.logging.ErrLoggerImpl
import com.bovilexics.javaph.qi.{CommandImpl, FieldFactory, FieldFactoryImpl, LineFactory, LineFactoryImpl, QiServerFileException, QiServerManager, ServerFactory, ServerFactoryImpl, ServerManager}
import com.bovilexics.javaph.ui.{IconProvider, IconProviderImpl}
import com.bovilexics.javaph.{JavaPH, JavaPHConstants}

object Main
{
  def main(args: Array[String]): Unit =
  {
    val defaultProperties = new PropertyCollectionImpl
    val properties = new PropertyCollectionImpl
    val factory : FieldFactory = new FieldFactoryImpl
    val lineFactory : LineFactory = new LineFactoryImpl
    val serverFactory : ServerFactory = new ServerFactoryImpl(factory, lineFactory)
    val serverManager: ServerManager = new QiServerManager(serverFactory)
    val errLogger = new ErrLoggerImpl
    val iconProvider : IconProvider = new IconProviderImpl
    val commands = CommandImpl.commands
    try
    {
      val javaPh = new JavaPH(commands, iconProvider, serverManager, defaultProperties, properties)
      javaPh.getQueryComboBox.getEditor.getEditorComponent.requestFocus()
    }
    catch
    {
      case e: QiServerFileException =>
        errLogger.printStackTrace(e)
        errLogger.log(e.getMessage)
        System.exit(1)
      case e: FileNotFoundException =>
        errLogger.printStackTrace(e)
        errLogger.log(String.format(JavaPHConstants.FILE_NOT_FOUND_OCCURRED_WHEN_TRYING_TO_LOAD_PROPERTIES_FROM_S, JavaPHConstants.PROP_FILE_DEF))
        System.exit(1)
      case e: IOException =>
        errLogger.printStackTrace(e)
        errLogger.log(String.format(JavaPHConstants.IOEXCEPTION_OCCURRED_WHEN_TRYING_TO_LOAD_PROPERTIES_FROM_S, JavaPHConstants.PROP_FILE_DEF))
        System.exit(1)
    }
  }
}
