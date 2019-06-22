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
	private final @NotNull String verbatim;

	// The code which Qi prepended to this line (e.g. -200)
	private final int code;

	// If more than one record was returned, that is a (0 based)
	// index representing the record that this line belongs to
	private int index;

	// Set for those lines which are not field value responses (e.g. '200:Ok.')
	private final @NotNull String response;
	
	//  The field, if defined, (e.g. '    email') in this line
	private final @NotNull String field;
	
	// The value, if defined, (e.g. '  myemail') in this line
	private final @NotNull String value;
	
	// The field, if defined, in this line with leading and
	// trailing white space trimmed (e.g. 'email').
	private final @NotNull String trimmedField;

	// The value, if defined, in this line with leading and
	// trailing white space trimmed (e.g. 'myemail').
	private final @NotNull String trimmedValue;

	public QiLine(
			final @NotNull String verbatim,
			final @NotNull String field,
			final @NotNull String value,
			final @NotNull String response,
			final int code,
			final int index)
	{
		this.verbatim = verbatim;
		this.field = field;
		this.value = value;
		trimmedValue = value.trim();
		trimmedField = field.trim();
		this.response = response;
		this.code = code;
		this.index = index;
	}

	public QiLine(
			final @NotNull String verbatim,
			final @NotNull String response,
			final int code,
			final int index)
	{
		this(verbatim, "", "", response, code, index);
	}

	public QiLine(final @NotNull String verbatim, final @NotNull String response, final int code)
	{
		this(verbatim, response, code, 0);
	}

	private int getCode(final int colon1Index) throws QiProtocolException
	{
		try
		{
			return Integer.parseInt(verbatim.substring(0, colon1Index));
		}
		catch (final @NotNull NumberFormatException e)
		{
			throw new QiProtocolException(verbatim);
		}
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
