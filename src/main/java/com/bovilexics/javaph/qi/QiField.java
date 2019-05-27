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
import org.jetbrains.annotations.Nullable;

import java.util.StringTokenizer;
import java.util.Vector;

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

	private String description;
	private String name;

	@NotNull
    private final Vector properties = new Vector();

	/**
	 * Construct a QiField with a given property list and description.
	 *
	 * @param properties a list of field properties.
	 * @param description a field description.
	 *
	 * @exception QiProtocolException in the event of an error parsing the data.
	 *
	 */
	public QiField(String name, @NotNull String someProperties, String description) throws QiProtocolException
	{
		setName(name);
		setProperties(someProperties);
		setDescription(description);
	}

	public String getDescription()
	{
		return description;
	}

	public int getLength()
	{
		return length;
	}

	public String getName()
	{
		return name;
	}

	@NotNull
    public Vector getProperties()
	{
		return properties;
	}

	public boolean hasProperty(String property)
	{
		return properties.indexOf(property) != -1;
	}

	public int hashCode()
	{
		return name.hashCode();
	}

	private void setDescription(String aDescription)
	{
		description = aDescription;
	}

	private void setName(String aName)
	{
		name = aName;
	}

	/**
	 * Parse a QI field property.
	 *
	 * @param The properties string which Qi returns in response to a
	 *         "fields" command. The protocol stuff should be stripped
	 *         leaving just the field description
	 *  	   (e.g. "max 64 Indexed Lookup Public Default Any")
	 *
	 * @exception QiProtocolException in the event of an error parsing the data.
	 *
	 */
	private void setProperties(@NotNull String someProperties) throws QiProtocolException
	{
		@Nullable String lengthString = null;
		@Nullable String token = null;
		@Nullable StringTokenizer tokenizer = null;

		tokenizer = new StringTokenizer(someProperties);
		token = (String) tokenizer.nextElement();
		
		if (token.startsWith("max"))
		{	
			lengthString = (String) tokenizer.nextElement();
			try
			{
				length = Integer.valueOf(lengthString).intValue();
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

		// Okay, here come the properties...
		while (tokenizer.hasMoreElements())
		{
			this.properties.addElement((String) tokenizer.nextElement());
		}
	}

	@NotNull
    public String toString()
	{
		return getName() + " - " + getDescription();
	}
}
