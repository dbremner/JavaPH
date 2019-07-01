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

import com.bovilexics.javaph.JavaPHConstants;
import com.bovilexics.javaph.logging.ErrLoggerImpl;
import com.bovilexics.javaph.logging.Logger;
import com.bovilexics.javaph.logging.StatusErrorLogger;
import com.bovilexics.javaph.threads.ResultThread;
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

/**
 * 
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public final class QiServer implements Server
{
	private final @NotNull FieldFactory factory;
	private final @NotNull LineFactory lineFactory;

	private static final int QUERY_RUNTIME = 10;

	private final @NotNull String name;
	private final @NotNull String server;
	private final int port;
	private final @NotNull String expandedName;
	private @NotNull ImmutableList<Field> fields = ImmutableList.of();
	private @NotNull FieldState fieldState = FieldState.FIELD_LOAD_FALSE;
	private @NotNull String fieldStateMessage = "";

	public QiServer(final @NotNull FieldFactory factory, final @NotNull LineFactory lineFactory, final @NotNull String name, final @NotNull String server, final int port)
	{
		this.lineFactory = lineFactory;
		if (name.isEmpty())
		{
			throw new IllegalArgumentException(JavaPHConstants.BLANK_NAME_IS_NOT_PERMITTED);
		}
		this.name = name;
		if (server.isEmpty())
		{
			throw new IllegalArgumentException(JavaPHConstants.BLANK_SERVER_IS_NOT_PERMITTED);
		}
		this.server = server;
		this.port = port;
		this.factory = factory;
		expandedName = String.format(Locale.US, "%s (%s:%d)", name, server, port);
	}

	private @NotNull ImmutableList<Field> convertRecordsToFields(final @NotNull List<List<Line>> records)
	{
		assert records.size() % 2 == 0;
		final @NotNull ImmutableList.Builder<Field> builder = new ImmutableList.Builder<>();
		for (final @NotNull List<Line> record : records)
		{
			// record should contain pairs of field property/description lines
			for (int j = 0; j < record.size() - 1; j += 2)
			{
				final Line propsLine = record.get(j);
				final Line descLine = record.get(j + 1);

				final @NotNull String propsField = propsLine.getField();
				final @NotNull String propsValue = propsLine.getValue();
				final @NotNull String descField = descLine.getField();
				final @NotNull String descValue = descLine.getValue();

				if (propsField.equals(descField))
				{
					// Do not add this field if it is one of the special fields already handled elsewhere
					if (propsField.equalsIgnoreCase("any") || propsField.equalsIgnoreCase("all"))
					{
						continue;
					}

					try
					{
						final @NotNull Field field = factory.create(propsField, propsValue, descValue);
						builder.add(field);
					}
					catch (final @NotNull QiProtocolException e)
					{
						fieldState = FieldState.FIELD_LOAD_ERROR;
						final @NotNull Logger instance = ErrLoggerImpl.instance;
						instance.log(String.format(JavaPHConstants.ERROR_QI_PROTOCOL_EXCEPTION_RECEIVED_WHEN_TRYING_TO_ADD_FIELD_TO_S, getExpandedName()));
						instance.log(String.format(" --> Message:     %s", e.getMessage()));
						instance.log(String.format(" --> Properties:  %s", propsLine.toString()));
						instance.log(String.format(" --> Description: %s", descLine.toString()));
					}
				}
				else
				{
					fieldState = FieldState.FIELD_LOAD_ERROR;
					final @NotNull Logger instance = ErrLoggerImpl.instance;
					instance.log(String.format(JavaPHConstants.ERROR_PROPERTY_AND_DESCRIPTION_LINES_DO_NOT_REFER_TO_THE_SAME_FIELD_FOR_S, getExpandedName()));
					instance.log(String.format(" --> %s", propsLine.toString()));
					instance.log(String.format(" --> %s", descLine.toString()));
				}
			}
		}
		return builder.build();
	}
	
	@Override
	public @NotNull String getExpandedName()
	{
		return expandedName;
	}

	@Override
	public @NotNull List<Field> getFields()
	{
		return fields;
	}

	@Override
	public @NotNull FieldState getFieldState()
	{
		return fieldState;
	}

	@Override
	public @NotNull String getFieldStateMessage()
	{
		if (fieldStateMessage.isEmpty()) {
			fieldStateMessage = String.format(JavaPHConstants.FIELDS_NOT_YET_LOADED_FOR_S, getExpandedName());
		}
			
		return fieldStateMessage;
	}

	@Override
	public @NotNull String getName()
	{
		return name;
	}
	
	@Override
	public int getPort()
	{
		return port;
	}
	
	@Override
	public @NotNull String getServer()
	{
		return server;
	}

	@Override
	public void loadFields()
	{

		@Nullable ResultThread resultThread = new ResultThread(new StatusErrorLogger(), QiCommand.FIELDS, new QiConnection(getExpandedName(), getServer(), getPort(), lineFactory));
		resultThread.start();

		int seconds = 0;
		while (seconds < QUERY_RUNTIME && resultThread.isAlive())
		{
			try
			{
				Thread.sleep(1000);
			}
			catch(final @NotNull InterruptedException ie)
			{
				ErrLoggerImpl.instance.printStackTrace(ie);
			}
			
			seconds++;
		}
			
		if (seconds == QUERY_RUNTIME)
		{
			resultThread.interrupt();
			fieldState = FieldState.FIELD_LOAD_ERROR;
			fieldStateMessage = String.format(JavaPHConstants.TIMEOUT_TRYING_TO_LOAD_FIELDS_FOR_S, getExpandedName());
		}
		else if (resultThread.getRecordCount() < 0)
		{
			fieldState = FieldState.SERVER_ERROR;
			fieldStateMessage = String.format(JavaPHConstants.SERVER_ERROR_TRYING_TO_LOAD_FIELDS_FOR_S, getExpandedName());
		}
		else if (resultThread.getRecordCount() == 0)
		{
			fieldState = FieldState.FIELD_LOAD_ERROR;
			fieldStateMessage = String.format(JavaPHConstants.NO_FIELDS_AVAILABLE_FOR_S, getExpandedName());
		}
		else
		{
			fields = convertRecordsToFields(resultThread.getRecords());
			// TODO this is vulgar
			// convertRecordsToFields assigns fieldState
			// so we test for failure before reassigning it
			if (fieldState != FieldState.FIELD_LOAD_ERROR)
			{
				fieldState = FieldState.FIELD_LOAD_TRUE;
				fieldStateMessage = String.format(JavaPHConstants.SUCCESSFULLY_LOADED_FIELDS_FOR_S, getExpandedName());
			}
			else
			{
				fieldStateMessage = String.format(JavaPHConstants.CORRUPT_FIELD_RESULTS_FOR_S, getExpandedName());
			}
		}

		//noinspection UnusedAssignment,ReuseOfLocalVariable
		resultThread = null;
	}

	@Override
	public @NotNull String toString()
	{
		return name;
	}
}
