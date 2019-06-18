package com.bovilexics.javaph.qi

import java.io.{FileNotFoundException, IOException}
import java.nio.file.{Files, Paths}
import java.util
import java.util.Collections

import com.bovilexics.javaph.logging.ErrLogger

object QiServerFileReader
{
  val SEPARATOR = "::"
}

final class QiServerFileReader(val serverFactory: ServerFactory)
{
  @throws[FileNotFoundException]
  @throws[IOException]
  def readServers(fileName: String): util.List[Server] =
  {
    val path = Paths.get(fileName)
    val lines = Files.readAllLines(path)
    val results = new util.ArrayList[Server](lines.size)
    val separator = QiServerFileReader.SEPARATOR
    for
    {
      i <- 0 to lines.size()
      if !lines.get(i).startsWith("#") // Ignore comment lines
    }
    {
      val line = lines.get(i)
      val items = line.split(separator)
      if (items.length != 3)
      {
        ErrLogger.instance.println("Error: Invalid server entry in " + fileName + " on line " + i + " --> " + line)
      }
      else
      {
        val server = serverFactory.create(items(0), items(1), items(2))
        results.add(server)
      }
    }
    Collections.unmodifiableList(results)
  }
}