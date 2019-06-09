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

/**
 *
 * Break a Qi response down into its constituent parts.
 * 
 * This is what we're trying to deal with:
 *
 * <B>result code:[entry index:][field name:]text</B>
 *
 * @author Robert Fernandes robert@bovilexics.com
 *
 */
public final class QiLine implements Line
{
	// The line as read from Qi, unmodified
	private final String verbatim;

	// The code which Qi prepended to this line (e.g. -200)
	private int code = 0;

	// If more than one record was returned, that is a (0 based)
	// index representing the record that this line belongs to
	private int index = 0;

	// Set for those lines which are not field value responses (e.g. '200:Ok.')
    private @NotNull String response = "";
	
	//  The field, if defined, (e.g. '    email') in this line
    private @NotNull String field = "";
	
	// The value, if defined, (e.g. '  myemail') in this line
    private @NotNull String value = "";
	
	// The field, if defined, in this line with leading and
	// trailing white space trimmed (e.g. 'email').
    private @NotNull String trimmedField = "";

	// The value, if defined, in this line with leading and
	// trailing white space trimmed (e.g. 'myemail').
    private @NotNull String trimmedValue = "";

	/** Given a line as read from Qi, construct a Line.
	  *
	  * @param line as read from Qi, unaltered.
	  *
	  * @exception QiProtocolException if the line can't be parsed.
	 */
	public QiLine(final String line) throws QiProtocolException
	{
		verbatim = line;
		parse();
	}

	@Override
	public int getCode()
	{
		return code;
	}

	@Override
	public @NotNull String getField()
	{
		return field;
	}

	@Override
	public int getIndex()
	{
		return index;
	}
	
	@Override
	public @NotNull String getResponse()
	{
		return response;
	}

	@Override
	public @NotNull String getTrimmedField()
	{
		return trimmedField;
	}

	@Override
	public @NotNull String getTrimmedValue()
	{
		return trimmedValue;
	}
	
	@Override
	public @NotNull String getValue()
	{
		return value;
	}

	/*
		TODO : parse() doesn't deal with all of the scenarios listed here

		ph myname return what
		-507:what:unknown field.
		500:Did not understand ph.
		
		:
		598:::Command not recognized.
		
		what:
		598:what::Command not recognized.
		
		what
		598:what:Command not recognized.
		
		ph myname return email
		102:There was 1 match to your request.
		-200:1:    email: myemail
		200:Ok.
		
		quit
		200:Bye!
	*/
	private void parse() throws QiProtocolException
	{
		// Get the result code.
		// Index of first colon --> -200:
		final int colon1Index = verbatim.indexOf(':');
		if (colon1Index == -1) {
            throw new QiProtocolException(verbatim);
        }

		try
		{
			code = Integer.parseInt(verbatim.substring(0, colon1Index));
		}
		catch (final @NotNull NumberFormatException e)
		{
			throw new QiProtocolException(verbatim);
		}

		// Get the index count, if there is one.
		// Index of second colon --> -200:1:
		final int colon2Index = verbatim.indexOf(':', colon1Index + 1);
		if (colon2Index == -1)
		{
			// This isn't a field:value response but rather a one line description.
			// Just record it and return.
			response = verbatim.substring(colon1Index + 1);
			return;
		}
		else
		{
			try
			{
				index = Integer.parseInt(verbatim.substring(colon1Index + 1, colon2Index));
			}
			catch (final @NotNull NumberFormatException e)
			{
				// This isn't a field:value response but rather a one line description.
				// Just record it and return.
				response = verbatim.substring(colon1Index + 1);
				return;
			}
		}
		
		// This should be a field:value response.
		// Get field, value and return.
		// Index of third colon --> -200:1:    email:
		final int colon3Index = verbatim.indexOf(':', colon2Index + 1);
		if (colon3Index == -1) {
            throw new QiProtocolException(verbatim);
        }
			
		field = verbatim.substring(colon2Index + 1, colon3Index);
		value = verbatim.substring(colon3Index + 1);
		trimmedField = field.trim();
		trimmedValue = value.trim();
	}
	
	@Override
    public @NotNull String toString()
	{
		final @NotNull StringBuilder out = new StringBuilder();

		out.append(verbatim);
		out.append(" --> ");
		out.append("{");
		out.append(code);
		out.append("} {");
		out.append(index);
		out.append("} {");
		out.append(response);
		out.append("} {");
		out.append(field);
		out.append("} {");
		out.append(value);
		out.append("}");

		return out.toString();
	}
}
