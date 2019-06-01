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
public class QiField
{
	private int length;

	private final @NotNull String description;

	private final @NotNull String name;

	private final @NotNull List<String> properties;

	/**
	 * Construct a QiField with a given property list and description.
	 *
	 * @param someProperties a list of field properties.
	 * @param description a field description.
	 *
	 * @exception QiProtocolException in the event of an error parsing the data.
	 *
	 */
	public QiField(@NotNull String name, @NotNull String someProperties, @NotNull String description) throws QiProtocolException
	{
		this.name = name;
		properties = setProperties(someProperties);
		this.description = description;
	}

	public @NotNull String getDescription()
	{
		return description;
	}

	public int getLength()
	{
		return length;
	}

	public @NotNull String getName()
	{
		return name;
	}

	public @NotNull List<String> getProperties()
	{
		return Collections.unmodifiableList(properties);
	}

	public boolean hasProperty(@NotNull String property)
	{
		return properties.contains(property);
	}

	@Override
	public int hashCode()
	{
		return name.hashCode();
	}

	/**
	 * Parse a QI field property.
	 *
	 * @param someProperties The properties string which Qi returns in response to a
	 *         "fields" command. The protocol stuff should be stripped
	 *         leaving just the field description
	 *  	   (e.g. "max 64 Indexed Lookup Public Default Any")
	 *
	 * @exception QiProtocolException in the event of an error parsing the data.
	 *
	 * @return properties collection
	 */
	private @NotNull List<String> setProperties(@NotNull String someProperties) throws QiProtocolException
	{
		final @NotNull StringTokenizer tokenizer = new StringTokenizer(someProperties);
		final @NotNull String token = (String) tokenizer.nextElement();

		if (token.startsWith("max"))
		{
			final @NotNull String lengthString = (String) tokenizer.nextElement();
			try
			{
				final @NotNull Integer boxed = Integer.valueOf(lengthString);
				length = boxed;
			}
			catch (NumberFormatException e)
			{
				throw new QiProtocolException("Invalid value for max length property: " + someProperties);
			}
		}
		else
		{
			length = -1;
		}

		final @NotNull List<String> props = new ArrayList<>();
		// Okay, here come the properties...
		while (tokenizer.hasMoreElements())
		{
			props.add((String) tokenizer.nextElement());
		}
		return props;
	}

	@Override
	public @NotNull String toString()
	{
		return getName() + " - " + getDescription();
	}
}
