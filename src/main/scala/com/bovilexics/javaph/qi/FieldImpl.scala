/*
 * Copyright (C) 2003  Robert Fernandes  robert@bovilexics.com
 * http://www.bovilexics.com/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * See COPYING.TXT for details.
 *//*
 * Copyright (C) 2003  Robert Fernandes  robert@bovilexics.com
 * http://www.bovilexics.com/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * See COPYING.TXT for details.
 */
package com.bovilexics.javaph.qi

import java.util

import com.google.common.collect.ImmutableList

/**
  * Represents a Qi field (as described in the output of the "fields" command.
  *
  * Here's what Qi returns in response to the "fields" command:
  *
  * -200:2:email:max 64 Indexed Lookup Public Default Any
  * -200:2:email:Preferred email alias.
  *
  * Here's what should be passed in setProperties() and setDescription():
  *
  * max 64 Indexed Lookup Public Default Any
  * Preferred email alias.
  *
  * @author Robert Fernandes robert@bovilexics.com
  *
  */
class FieldImpl private[qi](val name: String, val length: Int, val properties: ImmutableList[String], val description: String) extends Field
{
  override def getDescription: String = description

  override def getLength: Int = length

  override def getName: String = name

  override def getProperties: util.List[String] = properties

  override def hasProperty(property: String): Boolean = properties.contains(property)

  override def hashCode: Int = name.hashCode

  override def toString: String = name + " - " + description
}