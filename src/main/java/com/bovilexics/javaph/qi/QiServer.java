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

import com.bovilexics.javaph.logging.ErrLogger;
import com.bovilexics.javaph.logging.Logger;
import com.bovilexics.javaph.threads.ResultThread;
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public class QiServer implements Server
{
	private static final @NotNull FieldFactory factory = new QiFieldFactory();

	private static final int QUERY_RUNTIME = 10;

	private static final String PORT_ERROR = "Error: invalid port value passed into QiServer, must be a an integer between 0 and 65,535";

	private final @NotNull String name;
	private final @NotNull String server;
	private final @NotNull Integer port;
	private @NotNull List<Field> fields = new ArrayList<>();
	private @NotNull QiFieldState fieldState = QiFieldState.FIELD_LOAD_FALSE;
	private @NotNull String fieldStateMessage = "";

	public QiServer(final @NotNull String name, final @NotNull String server, final int port)
	{
		if (!QiServer.isValidPort(port)) {
			throw new IllegalArgumentException(PORT_ERROR);
		}

		this.name = name;
		this.server = server;
		this.port = port;
	}

	public QiServer(final @NotNull String aName, final @NotNull String aServer, final @NotNull String aPort)
	{
		this(aName, aServer, QiServer.convertToPort(aPort));
	}

	private static int convertToPort(final @NotNull String aPort)
	{
		try
		{
			final int aPortInteger = Integer.parseInt(aPort);

			if (!isValidPort(aPortInteger))
			{
				throw new IllegalArgumentException(PORT_ERROR);
			}
			return aPortInteger;
		}
		catch (final @NotNull NumberFormatException e)
		{
			throw new IllegalArgumentException(PORT_ERROR);
		}
	}

	private void convertRecordsToFields(final @NotNull List<List<Line>> records)
	{
		fields = new ArrayList<>();

		for (final @NotNull List<Line> record : records)
		{
			// record should contain pairs of field property/description lines 
			for (int j = 0; j < record.size() - 1; j += 2)
			{
				final Line propsLine = record.get(j);
				final Line descLine = record.get(j + 1);

				final @NotNull String propsField = propsLine.getTrimmedField();
				final @NotNull String propsValue = propsLine.getTrimmedValue();
				final @NotNull String descField = descLine.getTrimmedField();
				final @NotNull String descValue = descLine.getTrimmedValue();

				if (propsField.equals(descField))
				{
					// Do not add this field if it is one of the special fields already handled elsewhere
					if (propsField.equalsIgnoreCase("any") || propsField.equalsIgnoreCase("all"))
					{
						continue;
					}

					try
					{
						fields.add(factory.create(propsField, propsValue, descValue));
					}
					catch (final @NotNull QiProtocolException e)
					{
						fieldState = QiFieldState.FIELD_LOAD_ERROR;
						final @NotNull Logger instance = ErrLogger.instance;
						instance.println("Error: QiProtocolException received when trying to add field to " + getExpandedName());
						instance.println(" --> Message:     " + e.getMessage());
						instance.println(" --> Properties:  " + propsLine.toString());
						instance.println(" --> Description: " + descLine.toString());
					}
				}
				else
				{
					fieldState = QiFieldState.FIELD_LOAD_ERROR;
					final @NotNull Logger instance = ErrLogger.instance;
					instance.println("Error: property and description lines do not refer to the same field for " + getExpandedName());
					instance.println(" --> " + propsLine.toString());
					instance.println(" --> " + descLine.toString());
				}
			}
		}
	}
	
	@Override
	public @NotNull String getExpandedName()
	{
		final @NotNull StringBuilder out = new StringBuilder();
		
		out.append(name);
		out.append(" (");
		out.append(server);
		out.append(":");
		out.append(port.toString());
		out.append(")");
		
		return out.toString();
	}

	@Override
	public @NotNull List<Field> getFields()
	{
		final @NotNull List<Field> results = ImmutableList.copyOf(fields);
		return results;
	}

	@Override
	public @NotNull QiFieldState getFieldState()
	{
		return fieldState;
	}

	@Override
	public @NotNull String getFieldStateMessage()
	{
		if (fieldStateMessage.isEmpty()) {
			fieldStateMessage = "Fields not yet loaded for " + getExpandedName();
		}
			
		return fieldStateMessage;
	}

	@Override
	public @NotNull String getName()
	{
		return name;
	}
	
	@Override
	public @NotNull Integer getPort()
	{
		return port;
	}
	
	@Override
	public @NotNull String getServer()
	{
		return server;
	}

	@Contract(pure = true)
	private static boolean isValidPort(final int port)
	{
		return (port >= 0 && port <= 65535);
	}

	@Override
	public void loadFields()
	{

		@Nullable ResultThread resultThread = new ResultThread(QiCommand.FIELDS, this);
		resultThread.start();

		int seconds = 0;
		while (seconds < QUERY_RUNTIME && !resultThread.isFinished())
		{
			try
			{
				Thread.sleep(1000);
			}
			catch(final @NotNull InterruptedException ie)
			{
				ErrLogger.instance.printStackTrace(ie);
			}
			
			seconds++;
		}
			
		if (seconds == QUERY_RUNTIME)
		{
			resultThread.interrupt();
			fieldState = QiFieldState.FIELD_LOAD_ERROR;
			fieldStateMessage = "Timeout trying to load fields for " + getExpandedName();
		}
		else if (resultThread.getRecordCount() < 0)
		{
			fieldState = QiFieldState.SERVER_ERROR;
			fieldStateMessage = "Server error trying to load fields for " + getExpandedName();
		}
		else if (resultThread.getRecordCount() == 0)
		{
			fieldState = QiFieldState.FIELD_LOAD_ERROR;
			fieldStateMessage = "No fields available for " + getExpandedName();
		}
		else
		{
			convertRecordsToFields(resultThread.getRecords());
			fieldState = QiFieldState.FIELD_LOAD_TRUE;
			fieldStateMessage = "Successfully loaded fields for " + getExpandedName();
		}
		
		resultThread = null;
	}

	@Override
	public @NotNull String toString()
	{
		return name;
	}
}
