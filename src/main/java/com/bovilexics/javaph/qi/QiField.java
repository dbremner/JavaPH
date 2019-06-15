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
 */
package com.bovilexics.javaph.qi;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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
class QiField implements Field
{
	private final int length;

	private final @NotNull String description;

	private final @NotNull String name;

	private final @NotNull ImmutableList<String> properties;

	QiField(final @NotNull String name, final int length, final @NotNull ImmutableList<String> properties, final @NotNull String description)
	{
		this.name = name;
		this.length = length;
		this.properties = properties;
		this.description = description;
	}

	@Override
	public @NotNull String getDescription()
	{
		return description;
	}

	@Override
	public int getLength()
	{
		return length;
	}

	@Override
	public @NotNull String getName()
	{
		return name;
	}

	@Override
	public @NotNull List<String> getProperties()
	{
		return properties;
	}

	@Override
	public boolean hasProperty(final @NotNull String property)
	{
		return properties.contains(property);
	}

	@Override
	public int hashCode()
	{
		return name.hashCode();
	}

	@Override
	public @NotNull String toString()
	{
		return getName() + " - " + getDescription();
	}
}
