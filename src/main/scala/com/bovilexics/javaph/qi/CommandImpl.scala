package com.bovilexics.javaph.qi

import com.bovilexics.javaph.qi.QiCommand.{FIELDS, HELP, QUERY, SITEINFO, STATUS}
import com.google.common.collect.ImmutableList

object CommandImpl
{
  val commands: ImmutableList[Command] = ImmutableList.of(
    new CommandImpl(QUERY, "Query", true, true),
    new CommandImpl(FIELDS, "Fields", false, false),
    new CommandImpl(STATUS, "Status", false, false),
    new CommandImpl(SITEINFO, "SiteInfo", false, false),
    new CommandImpl(HELP, "Help", true, false))
}

final case class CommandImpl(
                              command : String,
                              name : String,
                              description: String,
                              canEditList : Boolean,
                              canEditText: Boolean)
  extends Command
{
  def this(name: String, description: String, canEditText: Boolean, canEditList: Boolean)
  {
    this(name + " ", name, description, canEditList, canEditText)
  }

  override def getCommand: String = command

  override def getDescription: String = description

  override def getName: String = name

  override def isListEditable: Boolean = canEditList

  override def isTextEditable: Boolean = canEditText

  override def toString: String = description
}