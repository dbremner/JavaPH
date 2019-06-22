package com.bovilexics.javaph.qi

class LineImpl(val code: Int, val verbatim: String, val field: String, val index: Int, val response: String, val trimmedField : String, val trimmedValue : String, val value: String) extends Line
{
  def this(code: Int, verbatim: String, field: String, value: String, response: String, index: Int)
  {
    this(code, verbatim, field, index, response, field.trim, value.trim, value)
  }

  def this(code : Int, verbatim : String, response : String, index : Int)
  {
    this(code, verbatim, "", "", response, index)
  }

  def this(code : Int, verbatim : String, response: String)
  {
    this(code, verbatim, response, 0)
  }

  override def getCode: Int = code

  override def getField: String = field

  override def getIndex: Int = index

  override def getResponse: String = response

  override def getTrimmedField: String = trimmedField

  override def getTrimmedValue: String = trimmedValue

  override def getValue: String = value

  override def getFieldValue: String = field + ":" + value
}
