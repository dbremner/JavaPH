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
public final class QiLine
{
	// The line as read from Qi, unmodified
	private final String verbatim;

	// The code which Qi prepended to this line (e.g. -200)
	private int code = 0;

	// If more than one record was returned, that is a (0 based)
	// index representing the record that this line belongs to
	private int index = 0;

	// Set for those lines which are not field value responses (e.g. '200:Ok.')
	@NotNull
    private String response = "";
	
	//  The field, if defined, (e.g. '    email') in this line
	@NotNull
    private String field = "";
	
	// The value, if defined, (e.g. '  myemail') in this line
	@NotNull
    private String value = "";
	
	// The field, if defined, in this line with leading and
	// trailing white space trimmed (e.g. 'email').
	@NotNull
    private String trimmedField = "";

	// The value, if defined, in this line with leading and
	// trailing white space trimmed (e.g. 'myemail').
	@NotNull
    private String trimmedValue = "";
	
	// Index of first colon --> -200:
	private int colon1Index = -1;
	
	// Index of second colon --> -200:1:
	private int colon2Index = -1;
	
	// Index of third colon --> -200:1:    email:
	private int colon3Index = -1;
	
	/** Given a line as read from Qi, construct a Line.
	  *
	  * @param line as read from Qi, unaltered.
	  *
	  * @exception QiProtocolException if the line can't be parsed.
	 */
	public QiLine(String line) throws QiProtocolException
	{
		verbatim = line;
		parse();
	}

	public int getCode()
	{
		return code;
	}

	@NotNull
    public String getField()
	{
		return field;
	}

	public int getIndex()
	{
		return index;
	}
	
	@NotNull
    public String getResponse()
	{
		return response;
	}

	@NotNull
    public String getTrimmedField()
	{
		return trimmedField;
	}

	@NotNull
    public String getTrimmedValue()
	{
		return trimmedValue;
	}
	
	@NotNull
    public String getValue()
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
		if ((colon1Index = verbatim.indexOf(':')) == -1) {
            throw new QiProtocolException(verbatim);
        }

		try
		{
			code = Integer.parseInt(verbatim.substring(0, colon1Index));
		}
		catch (NumberFormatException e)
		{
			throw new QiProtocolException(verbatim);
		}

		// Get the index count, if there is one.
		if ((colon2Index = verbatim.indexOf(':', colon1Index + 1)) == -1)
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
			catch (NumberFormatException e)
			{
				// This isn't a field:value response but rather a one line description.
				// Just record it and return.
				response = verbatim.substring(colon1Index + 1);
				return;
			}
		}
		
		// This should be a field:value response.
		// Get field, value and return.
		if ((colon3Index = verbatim.indexOf(':', colon2Index + 1)) == -1) {
            throw new QiProtocolException(verbatim);
        }
			
		field = verbatim.substring(colon2Index + 1, colon3Index);
		value = verbatim.substring(colon3Index + 1);
		trimmedField = field.trim();
		trimmedValue = value.trim();
	}
	
	@Override
    @NotNull
    public String toString()
	{
		@NotNull final StringBuffer out = new StringBuffer();
		
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
