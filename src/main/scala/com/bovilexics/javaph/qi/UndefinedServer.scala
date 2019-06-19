package com.bovilexics.javaph.qi

import java.util

object UndefinedServer
{
  val UNDEFINED = "undefined"
}

class UndefinedServer extends Server
{
  override def getExpandedName: String =
  {
    val out = new StringBuilder

    out.append(UndefinedServer.UNDEFINED)
    out.append(" (")
    out.append(UndefinedServer.UNDEFINED)
    out.append(":")
    out.append("0")
    out.append(")")

    return out.toString
  }

  override def getFields: util.List[Field] = util.Collections.EMPTY_LIST[Field];

  override def getFieldState: QiFieldState = QiFieldState.FIELD_LOAD_ERROR

  override def getFieldStateMessage: String = "Undefined Server, fields cannot be loaded."

  override def getName: String = UndefinedServer.UNDEFINED

  override def getPort: Integer = 0

  override def getServer: String = UndefinedServer.UNDEFINED

  override def loadFields(): Unit = throw new IllegalStateException()
}
