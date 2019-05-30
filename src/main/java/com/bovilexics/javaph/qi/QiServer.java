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

import com.bovilexics.javaph.threads.ResultThread;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public class QiServer
{

	private static final int QUERY_RUNTIME = 10;

	private static final String PORT_ERROR = "Error: invalid port value passed into QiServer, must be a an integer between 0 and 65,535";

	@NotNull
	private final String name;
	@NotNull
	private final String server;
	@Nullable
    private final Integer port;
	@NotNull
	private Vector<QiField> fields = new Vector<>();
	@NotNull
	private QiFieldState fieldState = QiFieldState.FIELD_LOAD_FALSE;
	@NotNull
	private String fieldStateMessage = "";

	public QiServer(@NotNull String name, @NotNull String server, int port)
	{
		if (!isValidPort(port)) {
			throw new IllegalArgumentException(PORT_ERROR);
		}

		this.name = name;
		this.server = server;
		this.port = port;
	}

	public QiServer(@NotNull String aName, @NotNull String aServer, @NotNull Integer aPortInteger)
	{
		if (!isValidPort(aPortInteger)) {
			throw new IllegalArgumentException(PORT_ERROR);
		}
		
		name = aName;
		server = aServer;
		port = aPortInteger;
	}

	public QiServer(@NotNull String aName, @NotNull String aServer, @NotNull String aPort)
	{
		@Nullable final Integer aPortInteger;
		
		try
		{
			aPortInteger = new Integer(aPort);
			
			if (!isValidPort(aPortInteger)) {
				throw new IllegalArgumentException(PORT_ERROR);
			}
		}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException(PORT_ERROR);
		}
		
		name = aName;
		server = aServer;
		port = aPortInteger;
	}


	private void convertRecordsToFields(@NotNull Vector<Vector<QiLine>> records)
	{
		fields = new Vector<>();

		for (int i = 0; i < records.size(); i++)
		{
			final List<QiLine> record = records.get(i);

			// record should contain pairs of field property/description lines 
			for (int j = 0; j < record.size() - 1; j += 2)
			{
				final QiLine propsLine = record.get(j);
				final QiLine descLine = record.get(j + 1);

				@NotNull final String propsField = propsLine.getTrimmedField();
				@NotNull final String propsValue = propsLine.getTrimmedValue();
				@NotNull final String descField = descLine.getTrimmedField();
				@NotNull final String descValue = descLine.getTrimmedValue();

				if (propsField.equals(descField))
				{
					// Do not add this field if it is one of the special fields already handled elsewhere
					if (propsField.equalsIgnoreCase("any") || propsField.equalsIgnoreCase("all")) {
						continue;
					}

					try
					{
						fields.add(new QiField(propsField, propsValue, descValue));
					}
					catch (QiProtocolException e)
					{
						fieldState = QiFieldState.FIELD_LOAD_ERROR;
						System.err.println("Error: QiProtocolException received when trying to add field to " + getExpandedName());
						System.err.println(" --> Message:     " + e.getMessage());
						System.err.println(" --> Properties:  " + propsLine.toString());
						System.err.println(" --> Description: " + descLine.toString());
					}
				}
				else
				{
					fieldState = QiFieldState.FIELD_LOAD_ERROR;
					System.err.println("Error: property and description lines do not refer to the same field for " + getExpandedName());
					System.err.println(" --> " + propsLine.toString());
					System.err.println(" --> " + descLine.toString());
				}
			}
		}
	}
	
	@NotNull
    public String getExpandedName()
	{
		@NotNull final StringBuffer out = new StringBuffer();
		
		out.append(name);
		out.append(" (");
		out.append(server);
		out.append(":");
		out.append(port.toString());
		out.append(")");
		
		return out.toString();
	}

	@NotNull
    public List<QiField> getFields()
	{
		@NotNull final List<QiField> results = new ArrayList<>(fields);
		return results;
	}

	@NotNull
	public QiFieldState getFieldState()
	{
		return fieldState;
	}

	public String getFieldStateMessage()
	{
		if (fieldStateMessage.isEmpty()) {
			fieldStateMessage = "Fields not yet loaded for " + getExpandedName();
		}
			
		return fieldStateMessage;
	}

	@NotNull
	public String getName()
	{
		return name;
	}
	
	@Nullable
    public Integer getPort()
	{
		return port;
	}
	
	@NotNull
	public String getServer()
	{
		return server;
	}

	private boolean isValidPort(@NotNull Integer port)
	{
		final int unboxed = port.intValue();
		return isValidPort(unboxed);
	}

	private boolean isValidPort(final int port)
	{
		return (port >= 0 && port <= 65535);
	}

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
			catch(InterruptedException ie)
			{
				ie.printStackTrace();
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
	public String toString()
	{
		return name;
	}
}
