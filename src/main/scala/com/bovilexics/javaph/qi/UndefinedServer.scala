package com.bovilexics.javaph.qi

import java.util

object UndefinedServer
{
  private val UNDEFINED = "undefined"
  private val expandedName = getExpandedName
  private def getExpandedName: String =
  {
    val out = new StringBuilder
    out.append(UndefinedServer.UNDEFINED)
    out.append(" (")
    out.append(UndefinedServer.UNDEFINED)
    out.append(":")
    out.append("0")
    out.append(")")
    out.toString
  }
}

class UndefinedServer extends Server
{
  override def getExpandedName: String = UndefinedServer.expandedName

  override def getFields: util.List[Field] = util.Collections.emptyList()

  override def getFieldState: QiFieldState = QiFieldState.FIELD_LOAD_ERROR

  override def getFieldStateMessage: String = "Undefined Server, fields cannot be loaded."

  override def getName: String = UndefinedServer.UNDEFINED

  override def getPort: Integer = 0

  override def getServer: String = UndefinedServer.UNDEFINED

  override def loadFields(): Unit = throw new IllegalStateException()
}
