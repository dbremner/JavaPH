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
package com.bovilexics.javaph.threads;

import com.bovilexics.javaph.JavaPHConstants;
import com.bovilexics.javaph.logging.StatusLogger;
import com.bovilexics.javaph.qi.Connection;
import com.bovilexics.javaph.qi.Line;
import com.bovilexics.javaph.qi.LineFactory;
import com.bovilexics.javaph.qi.QiAPI;
import com.bovilexics.javaph.qi.QiCommand;
import com.bovilexics.javaph.qi.QiProtocolException;
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 
 * A <B>ResultThread</B> builds an entire response from a Qi query.
 * 
 * A response from Qi as defined by this class is composed of three
 * distinct parts; prologue, multiline response and epilogue. There are
 * accessor methods to retrieve each of these 3 parts. For any given QI
 * response, any or all of the parts may be empty/null and this should be
 * tested for. These methods will also return null if used before a QI
 * response has been returned in it's entirety.
 *
 * <p>
 * A prologue is typically a single line
 *   e.g. 102:There was 1 match to your request
 * A multiline response represents a sequence of lines prefixed with a negative
 *   QI result code as described in the QI documentation.
 * An epilogue are closing comments from the QI session (e.g. 200:Ok.)
 *   (and could easily be disregarded)
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public final class ResultThread extends Thread
{
	@NonNls
	private static final @NotNull String DUPLICATE_FIELD_SUFFIX = ".1";
	// TODO Should these fields be volatile?

	private volatile boolean error = false;
	private boolean halted = false;

	private int entryIndex = 0;
	private volatile int lastCode = QiAPI.LR_OK;
	private volatile @NotNull ResultThreadState state = ResultThreadState.Starting;

	// Empty arrays are immutable according to Effective Java.
	private final @NotNull Object[][] emptyValues = new Object[0][0];

	private @NotNull ImmutableList<Object> headers = ImmutableList.of();
	private @NotNull Object[][] values = emptyValues;

	private final @NotNull Connection connection;
	private final @NotNull LineFactory lineFactory;
	private @Nullable Line qiLine;

	private final @NotNull String command;
	private final @NotNull String commandLine;
	private final @NotNull StatusLogger logger;
	private @NotNull String epilogue = "";
	private @NotNull String prologue = "";

	// Separator used in the display of queries which matched multiple records.
	private static final @NotNull String recordSeparator = "------------------------------------------------------------\n";

	private @NotNull StringBuilder rawResult = new StringBuilder();

	private final @NotNull List<List<Line>> records = new ArrayList<>();
	private @NotNull List<Line> record = new ArrayList<>();

	public ResultThread(final @NotNull StatusLogger logger, final @NotNull String command,
						final @NotNull Connection connection, final @NotNull LineFactory lineFactory) throws IOException
	{
		this.logger = logger;
		this.connection = connection;
		this.lineFactory = lineFactory;
		commandLine = command;
		this.command = new StringTokenizer(commandLine).nextToken();

		connect();
	}

	/**
	 * Return the value for a field of a particular result.
	 *
	 * @param recordIndex index from which we want the field.
	 * @param field we want from that record.
	 * @param replaceNewlines which will cause newlines to be replaced by spaces if set to true.
	 *
	 * @return The String value associated with the field from the record.
	 *          null if we've asked for a field from a record which doesn't exist.
	 *
	 * e.g. fieldValue(0, email, false) will return the contents of the "email"
	 *      field of the first record in our result with all newlines intact.
	 */
	private synchronized @Nullable String fieldValue(final int recordIndex, final @NotNull String field, final boolean replaceNewlines)
	{

		final @NotNull StringBuilder out = new StringBuilder();

		final List<Line> records = this.records.get(recordIndex);
		if (records == null) {
			return null;
		}

		boolean gotField = false;
		for (final @NotNull Line line : records)
		{
			// Skip empty and encrypted fields.
			if ((!gotField && !line.getField().equals(field)) || line.getCode() != -QiAPI.LR_OK || line.getValue().isEmpty())
			{
				continue;
			}

			gotField = true;

			// We're done.
			if (!line.getField().equals(field) && !line.getField().isEmpty())
			{
				break;
			}

			out.append(line.getValue());
			out.append(replaceNewlines ? "\n" : " ");
		}
		return out.toString();
	}

	private synchronized @NotNull String getEpilogue()
	{
		switch(state)
		{
			case Ok:
			{
				return epilogue;
			}
			case InProgress:
			case Starting:
			case Unknown:
			case Error:
			{
				return "";
			}
			case Stopped:
			default:
			{
				assert false;
				return "";
			}
		}
	}

	private @NotNull String getErrorString()
	{
		switch (state)
		{
			case Error:
			case Unknown:
			{
				return prologue;
			}
			case Ok:
			case Starting:
			case InProgress:
			{
				return "";
			}
			case Stopped:
			default:
			{
				assert false;
				return "";
			}
		}
	}

	private synchronized int getFieldCount()
	{
		switch (state)
		{
			case Ok:
			{
				return headers.size();
			}
			case Error:
			case Unknown:
			case Starting:
			case InProgress:
			{
				return 0;
			}
			case Stopped:
			default:
			{
				assert false;
				return -1;
			}
		}
	}

	private @NotNull ImmutableList<Object> getHeaders()
	{
		switch (state)
		{
			case Ok:
			{
				return headers;
			}
			case InProgress:
			case Starting:
			case Unknown:
			case Error:
			{
				return ImmutableList.of();
			}
			case Stopped:
			default:
			{
				assert false;
				return ImmutableList.of();
			}
		}
	}

	private @NotNull String getPrologue()
	{
		switch (state)
		{
			case Ok:
			{
				synchronized (this)
				{
					return prologue;
				}
			}
			case Error:
			case Unknown:
			case Starting:
			case InProgress:
			{
				return "";
			}
			case Stopped:
			default:
			{
				assert false;
				return "";
			}
		}
	}

	public int getRecordCount()
	{
		switch (state)
		{
			case Ok:
			{
				synchronized (this)
				{
					return records.size();
				}
			}
			case InProgress:
			case Starting:
			case Unknown:
			case Error:
			{
				return -1;
			}
			case Stopped:
			default:
			{
				assert false;
				return -1;
			}
		}
	}

	public @NotNull List<List<Line>> getRecords()
	{
		switch (state)
		{
			case Ok:
			{
				synchronized (this)
				{
					final @NotNull List<List<Line>> results = ImmutableList.copyOf(records);
					return results;
				}
			}
			case Error:
			case Unknown:
			case Starting:
			case InProgress:
			{
				return ImmutableList.of();
			}
			case Stopped:
			default:
			{
				assert false;
				return ImmutableList.of();
			}
		}
	}

	synchronized @NotNull ResultThreadResult getResult()
	{
		assert state == ResultThreadState.Ok;
		final @NotNull String rawResultLocal = getRawResult();
		final @NotNull ImmutableList<Object> headersLocal = getHeaders();
		final @NotNull Object[][] valuesLocal = getValues();

		final ResultThreadResult result = new ResultThreadResult(rawResultLocal, headersLocal, valuesLocal);
		return result;
	}

	private @NotNull String getRawResult()
	{
		switch (state)
		{
			case Ok:
			{
				return rawResult.toString();
			}
			case Error:
			case Unknown:
			case Starting:
			case InProgress:
			{
				return "";
			}
			case Stopped:
			default:
			{
				assert false;
				return "";
			}
		}
	}

	private @NotNull Object[][] getValues()
	{
		switch (state)
		{
			case Ok:
			{
				return values.clone();
			}
			case InProgress:
			case Starting:
			case Unknown:
			case Error:
			{
				//noinspection AssignmentOrReturnOfFieldWithMutableType
				return emptyValues;
			}
			case Stopped:
			default:
			{
				assert false;
				//noinspection AssignmentOrReturnOfFieldWithMutableType
				return emptyValues;
			}
		}
	}

	@Override
	public synchronized void interrupt()
	{
		if (!isAlive())
		{
			return;
		}
		halted = true;
		try
		{
			cleanup();
		}
		catch (@NotNull IOException | QiProtocolException e)
		{
			error = true;
			logger.showStatus(String.format(JavaPHConstants.ERROR_S, e));
		}
		super.interrupt();
	}

	private synchronized boolean isValidQiResponse()
	{
		return !error && !halted && (state == ResultThreadState.Ok || state == ResultThreadState.Error);
	}

	@Override
	public void run()
	{
		// TODO dead branch?
		if (error)
		{
			state = ResultThreadState.Error;
			logger.showStatus(JavaPHConstants.ERROR_INVALID_CONNECTION_QUERY_STOPPED);
			return;
		}
		
		connection.lock();

		try
		{
			buildResult();
			
			if (!connection.authenticated()) {
				connection.disconnect();
			}
		}
		catch (@NotNull QiProtocolException | IOException e)
		{
			error = true;
			logger.showStatus(String.format(JavaPHConstants.ERROR_S, e));
		} finally
		{
			connection.unlock();
		}
	}

	/** 
	 * This routine is called for each line that's read from the Qi server. Its
	 * mission in life is to process each line, build the prologue, multiline
	 * response, and epilogue. In addition it'll throw an exception if we find
	 * ourselves in a weird state.
	 *
	 * It does however expect to be given a query response to build. It will get
	 * all confused if given the response to a login request for example.
	 *
	 * @exception QiProtocolException in the event of Qi throwing us a curve ball.
	 */
	private void add(final @NotNull String readFromServer, final @NotNull Line line) throws QiProtocolException
	{
		final int code = line.getCode();
		@NonNls final @NotNull String response = line.getResponse();
		switch (state)
		{
			case Starting:
				if (code >= QiAPI.LR_PROGRESS && code < QiAPI.LR_OK)
				{
					// Some implementations of qi return a 102 response before the
					// actual entries, giving the number of entries found; be prepared
					// to see or not see this response.
					prologue += response + "\n";
					break;
				}
				else if (code == QiAPI.LR_OK || code == QiAPI.LR_RONLY)
				{
					// Single line response.
					prologue += response + "\n";
					state = ResultThreadState.Ok;
					lastCode = code;
					break;
				}
				else if ((code < -QiAPI.LR_TEMP || code > QiAPI.LR_TEMP) && code != -QiAPI.LR_ABSENT)
				{
					// This should be an error
					prologue += response + "\n";
					state = ResultThreadState.Error;
					lastCode = code;
					break;
				}
				else
				{
					// This implementation of QI must not give LR_NUMRET.
					state = ResultThreadState.InProgress;
				}

				// fall through

			case InProgress:
				if (code == -QiAPI.LR_OK
					|| code == -QiAPI.LR_RONLY
					|| code == -QiAPI.LR_AINFO
					|| code == -QiAPI.LR_ABSENT
					|| code == -QiAPI.LR_ISCRYPT)
				{
					// Is this a new record?
					final int index = line.getIndex();
					if (index != entryIndex)
					{
						// It is a new record; append this record to the result and
						// instantiate a new Vector object to hold the new record.
						if (entryIndex != 0)
						{
							records.add(record);
							record = new ArrayList<>();
						}
						entryIndex = index;
					}
					record.add(line);
				}
				else if (code >= QiAPI.LR_OK)
				{
					// It must be done so finish it up.
					epilogue += response + "\n";
					records.add(record);
					state = ResultThreadState.Ok;

				}
				else
				{
					// If this ever happens then I've misinterpreted the protocol.
					state = ResultThreadState.Unknown;
					throw new QiProtocolException(String.format(JavaPHConstants.UNKNOWN_STATE_S, readFromServer));
				}
				break;

			case Unknown:
				throw new QiProtocolException(String.format(JavaPHConstants.UNKNOWN_STATE_S, readFromServer));
			case Error:
				if (code >= QiAPI.LR_TEMP)
				{
					// End of error response.
					prologue += response + "\n";

					// Make a record of what the last code was.
					lastCode = code;
				}
				else if (code < -QiAPI.LR_TEMP)
				{
					// This should be a multiline error description.
					prologue += response + "\n";
				}
				else
				{
					state = ResultThreadState.Unknown;
					throw new QiProtocolException(String.format(JavaPHConstants.UNKNOWN_STATE_S, readFromServer));
				}
				break;
				
			case Ok:
				// "200:Bye!" is all that should seen here (and that is disposed).
				if (code != QiAPI.LR_OK)
				{
					state = ResultThreadState.Unknown;
					throw new QiProtocolException(String.format(JavaPHConstants.UNKNOWN_STATE_S, readFromServer));
				}
				break;
			case Stopped:
			{
				//This shouldn't be possible.
				assert false;
			}
		}
	}

	private static @NotNull ImmutableList<Object> buildHeaders(final @NotNull List<List<Line>> list)
	{
		@NotNull String lastField = JavaPHConstants.UNKNOWN;
		final @NotNull List<Object> uniqueHeaders = new ArrayList<>();

		for (final @NotNull List<Line> currentQiLine : list)
		{
			for (final @NotNull Line line : currentQiLine)
			{
				@NotNull String field = line.getField();

				if (field.isEmpty())
				{
					field = lastField + DUPLICATE_FIELD_SUFFIX;
				}

				if (!uniqueHeaders.contains(field))
				{
					if (field.endsWith(DUPLICATE_FIELD_SUFFIX))
					{
						final int index = uniqueHeaders.indexOf(field.substring(0, field.lastIndexOf(DUPLICATE_FIELD_SUFFIX))) + 1;
						uniqueHeaders.add(index, field);
					}
					else
					{
						uniqueHeaders.add(field);
					}
				}

				lastField = field;
			}
		}
		
		return ImmutableList.copyOf(uniqueHeaders);
	}

	/**
	 * This is the main routine that the Result thread executes.
	 * When it exits, we should have a valid result.
	 *
	 * Quite simply, all that's done here is
	 *  1. Connect to the server.
	 *  2. Send the command that we were given.
	 *  3. Read the response, line by line and building a complete result
	 *     by calling the add() routine for each line.
	 *  4. When we determine that we have a complete response, close the
	 *     connection and signal that we're done.
	 */
	private synchronized void buildResult() throws IOException, QiProtocolException
	{

		if (halted)
		{
			cleanup();
			return;
		}

		// Send query.
		connection.writeQI(commandLine + "\n");

		// Read the server's response, line by line.
		rawResult = new StringBuilder();
		int index = 0;
		@Nullable String readFromServer;
		while ((readFromServer = connection.readQI()) != null)
		{
			qiLine = lineFactory.create(readFromServer);

			if (halted)
			{
				cleanup();
				return;
			}

			add(readFromServer, qiLine);

			if (qiLine.getIndex() != index) {
				rawResult.append(recordSeparator);
			}

			index = qiLine.getIndex();
			rawResult.append(qiLine.getResponse().isEmpty() ? qiLine.getFieldValue() : qiLine.getResponse());
			rawResult.append("\n");
			
			// If code >= LR_OK, Qi has said all it's going to say.
			if (qiLine.getCode() >= QiAPI.LR_OK)
			{
				if (!connection.authenticated())
				{
					connection.disconnect();
				}
				break;
			}
			
			if (qiLine != null) {
				lastCode = qiLine.getCode();
			}
		}
		
		if (qiLine != null) {
			lastCode = qiLine.getCode();
		}

		if (lastCode >= QiAPI.LR_TEMP)
		{
			state = ResultThreadState.Error;
			assert qiLine != null;
			prologue = qiLine.getResponse();
			
			final @NotNull String message = String.format(JavaPHConstants.GOT_ERROR_D_ON_LINE_S, qiLine.getCode(), readFromServer);

			logger.log(message);
		}
		else
		{
			state = ResultThreadState.Ok;
			if (command.equals(QiCommand.FIELDS))
			{
				headers = ResultThread.buildHeadersForFields();
				values = ResultThread.buildValuesForFields(headers, records);
			}
			else
			{
				headers = ResultThread.buildHeaders(records);
				values = buildValues(headers, records);
			}
		}
	}

	private @NotNull Object[][] buildValues(final @NotNull ImmutableList<Object> headersList,
											 final @NotNull List<List<Line>> lists)
	{
		final Object[][] results = new Object[lists.size()][headersList.size()];

		int xCoordinate = -1;
		@NonNls @NotNull String lastField = JavaPHConstants.UNKNOWN;

		for (int i = 0; i < lists.size(); i++)
		{
			final int yCoordinate = i;
			final List<Line> list = lists.get(i);

			for (final @NotNull Line line : list)
			{
				@NotNull String field = line.getField();

				if (field.isEmpty())
				{
					field = lastField + DUPLICATE_FIELD_SUFFIX;
				}

				boolean found = false;
				for (int k = 0; k < headersList.size() && !found; k++)
				{
					final @NotNull Object value = headersList.get(k);
					if (value.equals(field))
					{
						xCoordinate = k;
						found = true;
					}
				}

				if (found)
				{
					results[yCoordinate][xCoordinate] = line.getValue();
				}
				else
				{
					final @NotNull String message = String.format(JavaPHConstants.COULDN_T_FIND_HEADER_FOR_THIS_COLUMN_S, line.toString());

					logger.log(message);
				}

				lastField = field;
			}	
		}
		return results;
	}

	private static @NotNull ImmutableList<Object> buildHeadersForFields()
	{
		return ImmutableList.copyOf(new Object[] {JavaPHConstants.NAME, JavaPHConstants.DESCRIPTION,
			JavaPHConstants.PROPERTIES});
	}

	private static @NotNull Object[][] buildValuesForFields(final @NotNull ImmutableList<Object> headersList,
													 final @NotNull List<List<Line>> lists)
	{
		final @NotNull Object[][] results = new Object[lists.size()][headersList.size()];

		for (int i = 0; i < lists.size(); i++)
		{
			final List<Line> list = lists.get(i);

			assert list.size() % 2 == 0;
			for (int j = 0; j < list.size() - 1; j = j + 2)
			{
				final Line propsQiLine = list.get(j);
				final Line descQiLine = list.get(j + 1);

				final @NotNull String field = propsQiLine.getField();
				final @NotNull String props = propsQiLine.getValue();
				final @NotNull String desc = descQiLine.getValue();

				results[i][0] = field;
				results[i][1] = desc;
				results[i][2] = props;
			}	
		}
		return results;
	}

	private void cleanup() throws IOException, QiProtocolException
	{
		state = ResultThreadState.Stopped;
		prologue = JavaPHConstants.STOPPED;

		// Read the remainder of the response from Qi (and dispose).
		if (qiLine != null && qiLine.getCode() < QiAPI.LR_OK)
		{
			@Nullable String buffer;
			while ((buffer = connection.readQI()) != null)
			{
				final @NotNull Line line = lineFactory.create(buffer);
				if (line.getCode() >= QiAPI.LR_OK) {
					break;
				}
			}
		}

		if (!connection.authenticated()) {
			connection.disconnect();
		}
	}

	/**
	 * 
	 * It's here that the connection is established with the Qi server.
	 *
	 *
	 */
	private synchronized void connect() throws IOException
	{
		if (!connection.connected())
		{
			connection.connect();
		}
	}

}
