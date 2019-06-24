package com.bovilexics.javaph.configuration

import java.io.{IOException, InputStream, OutputStream}
import java.util.{Optional, Properties}

final class PropertyCollectionImpl extends PropertyCollection
{
  final private val properties = new Properties

  override def size: Int = properties.size

  override def isEmpty: Boolean = properties.isEmpty

  override def toString: String = properties.toString

  override def equals(o: Any): Boolean = properties == o

  override def hashCode: Int = properties.hashCode

  override def setProperty(key: String, value: String): Unit = properties.setProperty(key, value)

  override def setProperty(key: String, value: Int): Unit = properties.setProperty(key, String.valueOf(value))

  override def setProperty(key: String, value: Boolean): Unit = properties.setProperty(key, String.valueOf(value))

  @throws[IOException]
  override def load(inStream: InputStream): Unit = properties.load(inStream)

  @throws[IOException]
  override def store(out: OutputStream, comments: String): Unit = properties.store(out, comments)

  override def getProperty(key: String): Optional[String] = Optional.ofNullable(properties.getProperty(key))

  override def getProperty(key: String, defaultValue: String): String = properties.getProperty(key, defaultValue)

  override def getProperty(key: String, defaultValue: Int): Int = try
  {
    // TODO get this working
    val option = getProperty(key)
    val opt = if (option.isPresent) Some(option.get()) else None
    opt.map(x => Integer.parseInt(x)).getOrElse(defaultValue)
  }
  catch
  {
    case _: NumberFormatException => defaultValue
  }
}