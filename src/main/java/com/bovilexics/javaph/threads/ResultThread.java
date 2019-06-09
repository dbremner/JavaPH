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

import com.bovilexics.javaph.JavaPH;
import com.bovilexics.javaph.qi.Connection;
import com.bovilexics.javaph.qi.Line;
import com.bovilexics.javaph.qi.QiAPI;
import com.bovilexics.javaph.qi.QiCommand;
import com.bovilexics.javaph.qi.QiConnection;
import com.bovilexics.javaph.qi.QiLine;
import com.bovilexics.javaph.qi.QiProtocolException;
import com.bovilexics.javaph.qi.Server;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.SwingUtilities;
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
public class ResultThread extends Thread
{
	private boolean error = false;
	private boolean finished = false;
	private boolean halted = false;
	private boolean headersDone = false;
	private boolean valuesDone = false;
	
	private int entryIndex;	
	private int lastCode = QiAPI.LR_OK;
	private @NotNull ResultThreadState state = ResultThreadState.RS_START;

	private final @Nullable JavaPH parent;

	private @Nullable Object[] headers = null;
	private @Nullable Object[][] values = null;

	private Connection connection;
	private Line qiLine;

	private @Nullable String command;
	private @Nullable String commandLine;
	private @NotNull String epilogue = "";
	private @NotNull String prologue = "";
	private String readFromServer;

	// Separator used in the display of queries which matched multiple records.
	private static final @NotNull String recordSeparator = "------------------------------------------------------------\n";

	private StringBuffer rawResult;

	private final List<List<Line>> records = new ArrayList<>();
	private @NotNull List<Line> record = new ArrayList<>();

	public ResultThread(final String command, final @NotNull Connection connection)
	{
		this(null, command, connection);
	}

	public ResultThread(final String command, final @NotNull Server server)
	{
		this(null, command, new QiConnection(server));
	}


	public ResultThread(final @NotNull JavaPH javaph)
	{
		parent = javaph;
		connect(parent.getCommand(), parent.getConnection());
	}

	private ResultThread(final @Nullable JavaPH javaph, final String command, final @NotNull Connection connection)
	{
		parent = javaph;
		connect(command, connection);
	}

	/**
	 * Return the value for a field of a particular result.
	 *
	 * @param record index from which we want the field.
	 * @param field we want from that record.
	 * @param replaceNewlines which will cause newlines to be replaced by spaces if set to true.
	 *
	 * @return The String value associated with the field from the record.
	 *          null if we've asked for a field from a record which doesn't exist.
	 *
	 * e.g. fieldValue(0, email, false) will return the contents of the "email"
	 *      field of the first record in our result with all newlines intact.
	 */
	public synchronized @Nullable String fieldValue(final int record, final String field, final boolean replaceNewlines)
	{

		final @NotNull StringBuilder out = new StringBuilder();

		final List<Line> records = this.records.get(record);
		if (records == null) {
			return null;
		}

		boolean gotField = false;
		for (int i = 0; i < records.size(); i++)
		{
			final @Nullable Line qiLine = records.get(i);

			// Skip empty and encrypted fields.
			if ((!gotField && !qiLine.getTrimmedField().equals(field)) || qiLine.getCode() != -QiAPI.LR_OK || qiLine.getTrimmedValue().isEmpty()) {
				continue;
			}
				
			gotField = true;

			// We're done.
			if (!qiLine.getTrimmedField().equals(field) && !qiLine.getTrimmedField().isEmpty()) {
				break;
			}

			out.append(qiLine.getTrimmedValue());
			out.append(replaceNewlines ? "\n" : " ");
		}
		return out.toString();
	}

	public synchronized @Nullable String getCommand()
	{
		return command;
	}

	public synchronized @Nullable String getEpilogue()
	{
		return isOk() ? epilogue : null;
	}

	private boolean isOk() {
		return state == ResultThreadState.RS_OK;
	}

	public @Nullable String getErrorString()
	{
		return state == ResultThreadState.RS_ERROR || state == ResultThreadState.RS_UNKNOWN ? prologue : null;
	}

	public synchronized int getFieldCount()
	{
		return isOk() ? headers.length : 0;
	}

	public synchronized @Nullable Object[] getHeaders()
	{
		return isOk() ? headers : null;
	}

	public int getLastCode()
	{
		return lastCode;
	}

	public synchronized @Nullable String getPrologue()
	{
		return isOk() ? prologue : null;
	}

	public synchronized int getRecordCount()
	{
		return isOk() ? records.size() : -1;
	}

	public synchronized @NotNull List<List<Line>> getRecords()
	{
		if (!isOk())
		{
			return new ArrayList<>();
		}

		final @NotNull List<List<Line>> results = new ArrayList<>(records);
		return results;
	}

	public synchronized @Nullable String getRawResult()
	{
		return isOk() && rawResult.length() > 0 ? rawResult.toString() : null;
	}

	public synchronized @Nullable Object[][] getValues()
	{
		return isOk() ? values : null;
	}

	@Override
	public synchronized void interrupt()
	{
		if (!finished)
		{
			halted = true;
			cleanup();
			super.interrupt();
		}
	}

	public synchronized boolean isFinished()
	{
		return finished;
	}

	public boolean isValidQiResponse()
	{
		return !error && !halted && (isOk() || state == ResultThreadState.RS_ERROR);
	}

	@Override
	public void run()
	{
		finished = false;

		if (error)
		{
			state = ResultThreadState.RS_ERROR;
			showStatus("Error: Invalid connection, query stopped.");
			finished = true;
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
			showStatus("Error: " + e);
		} finally
		{
			connection.unlock();
		}
		
		finished = true;
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
	private void add() throws QiProtocolException
	{
		switch (state)
		{
			case RS_START:
				if (qiLine.getCode() >= QiAPI.LR_PROGRESS && qiLine.getCode() < QiAPI.LR_OK)
				{
					// Some implementations of qi return a 102 response before the
					// actual entries, giving the number of entries found; be prepared
					// to see or not see this response.
					prologue += (qiLine.getResponse() + "\n");
					break;
				}
				else if (qiLine.getCode() == QiAPI.LR_OK || qiLine.getCode() == QiAPI.LR_RONLY)
				{
					// Single line response.
					prologue += (qiLine.getResponse() + "\n");
					state = ResultThreadState.RS_OK;
					lastCode = qiLine.getCode();
					break;
				}
				else if ((qiLine.getCode() < -QiAPI.LR_TEMP || qiLine.getCode() > QiAPI.LR_TEMP) && qiLine.getCode() != -QiAPI.LR_ABSENT)
				{
					// This should be an error
					prologue += (qiLine.getResponse() + "\n");
					state = ResultThreadState.RS_ERROR;
					lastCode = qiLine.getCode();
					break;
				}
				else
				{
					// This implementation of QI must not give LR_NUMRET.
					state = ResultThreadState.RS_INPROGRESS;
				}

				// fall through

			case RS_INPROGRESS:
				if (qiLine.getCode() == -QiAPI.LR_OK
					|| qiLine.getCode() == -QiAPI.LR_RONLY
					|| qiLine.getCode() == -QiAPI.LR_AINFO
					|| qiLine.getCode() == -QiAPI.LR_ABSENT
					|| qiLine.getCode() == -QiAPI.LR_ISCRYPT)
				{
					// Is this a new record?
					if (qiLine.getIndex() != entryIndex)
					{
						// It is a new record; append this record to the result and
						// instantiate a new Vector object to hold the new record.
						if (entryIndex != 0)
						{
							addRecord(record);
							record = new ArrayList<>();
						}
						entryIndex = qiLine.getIndex();
					}
					record.add(qiLine);
				}
				else if (qiLine.getCode() >= QiAPI.LR_OK)
				{
					// It must be done so finish it up.
					epilogue += (qiLine.getResponse() + "\n");
					addRecord(record);
					state = ResultThreadState.RS_OK;

				}
				else
				{
					// If this ever happens then I've misinterpreted the protocol.
					state = ResultThreadState.RS_UNKNOWN;
					throw new QiProtocolException("Unknown State: " + readFromServer);
				}
				break;

			case RS_UNKNOWN:
				throw new QiProtocolException("Unknown State: ");
			case RS_ERROR:
				if (qiLine.getCode() >= QiAPI.LR_TEMP)
				{
					// End of error response.
					prologue += (qiLine.getResponse() + "\n");

					// Make a record of what the last code was.
					// Can be retrieved through getLastCode().
					lastCode = qiLine.getCode();
				}
				else if (qiLine.getCode() < -QiAPI.LR_TEMP)
				{
					// This should be a multiline error description.
					prologue += (qiLine.getResponse() + "\n");
				}
				else
				{
					state = ResultThreadState.RS_UNKNOWN;
					throw new QiProtocolException("Unknown State: " + readFromServer);
				}
				break;
				
			case RS_OK:
				// "200:Bye!" is all that should seen here (and that is disposed).
				if (qiLine.getCode() != QiAPI.LR_OK)
				{
					state = ResultThreadState.RS_UNKNOWN;
					throw new QiProtocolException("Unknown State: " + readFromServer);
				}
				break;
		}
	}

	private synchronized void addRecord(final List<Line> aRecord)
	{
		records.add(aRecord);
	}

	private synchronized void buildHeaders()
	{
		if (headersDone) {
			return;
		}

		@NotNull String lastField = "unknown";
		final @NotNull List<String> uniqueHeaders = new ArrayList<>();

		for (int i = 0; i < records.size(); i++)
		{
			final List<Line> currentQiLine = records.get(i);

			for (int j = 0; j < currentQiLine.size(); j++)
			{
				@NotNull String field = currentQiLine.get(j).getTrimmedField();

				if (field.isEmpty())
				{
					field = lastField + ".1";
				}
				
				if (!uniqueHeaders.contains(field))
				{
					if (field.endsWith(".1")) {
						final int index = uniqueHeaders.indexOf(field.substring(0, field.lastIndexOf(".1"))) + 1;
						uniqueHeaders.add(index, field);
					} else {
						uniqueHeaders.add(field);
					}
				}
				
				lastField = field;
			}
		}
		
		headers = uniqueHeaders.toArray();
		
		headersDone = true;
	}

	private synchronized void buildHeadersForFields()
	{
		if (headersDone) {
			return;
		}
		
		headers = new String[] { "name", "description", "properties" };
	
		headersDone = true;
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
		writeQi(commandLine + "\n");

		// Read the server's response, line by line.
		rawResult = new StringBuffer();
		int index = 0;
		while ((readFromServer = readQi()) != null)
		{
			qiLine = new QiLine(readFromServer);

			if (halted)
			{
				cleanup();
				return;
			}

			add();

			if (qiLine.getIndex() != index) {
				rawResult.append(recordSeparator);
			}

			index = qiLine.getIndex();
			rawResult.append(qiLine.getResponse().isEmpty() ? qiLine.getField() + " : " + qiLine.getValue() : qiLine.getResponse());
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
			state = ResultThreadState.RS_ERROR;
			prologue = qiLine.getResponse();
			
			final @NotNull String message = "Got error " + qiLine.getCode() + " on line --> " + readFromServer;
			
			if (parent == null) {
				System.err.println(message);
			} else {
				parent.log(message);
			}
		}
		else
		{
			state = ResultThreadState.RS_OK;
			
			if (command.equals(QiCommand.FIELDS))
			{
				buildHeadersForFields();
				buildValuesForFields();
			}
			else
			{
				buildHeaders();
				buildValues();
			}
		}
	}

	private synchronized void buildValues()
	{
		if (valuesDone) {
			return;
		}

		if (!headersDone) {
			buildHeaders();
		}
			
		values = new Object[records.size()][headers.length];

		int xCoordinate = -1;
		@NotNull String lastField = "unknown";

		for (int i = 0; i < records.size(); i++)
		{
			final int yCoordinate = i;
			final List<Line> thisVector = records.get(i);

			for (int j = 0; j < thisVector.size(); j++)
			{
				final Line thisQiLine = thisVector.get(j);
				@NotNull String field = thisQiLine.getTrimmedField();

				if (field.isEmpty())
				{
					field = lastField + ".1";
				}

				boolean found = false;
				for (int k = 0; k < headers.length && !found; k++)
				{
					if ( headers[k].equals(field) )
					{
						xCoordinate = k;
						found = true;
					}
				}
				
				if (found)
				{
					values[yCoordinate][xCoordinate] = thisQiLine.getTrimmedValue();
				}
				else
				{
					final @NotNull String message = "Couldn't find header for this column: " + thisQiLine.toString();
					
					if (parent == null) {
						System.err.println(message);
					} else {
						parent.log(message);
					}
				}
				
				lastField = field;
			}	
		}	
			
		valuesDone = true;
	}

	private synchronized void buildValuesForFields()
	{
		if (valuesDone) {
			return;
		}

		if (!headersDone) {
			buildHeadersForFields();
		}
			
		values = new Object[records.size()][headers.length];

		for (int i = 0; i < records.size(); i++)
		{
			final List<Line> list = records.get(i);

			for (int j = 0; j < list.size() - 1; j = j + 2)
			{
				final Line propsQiLine = list.get(j);
				final Line descQiLine = list.get(j + 1);

				final @NotNull String field = propsQiLine.getTrimmedField();
				final @NotNull String props = propsQiLine.getTrimmedValue();
				final @NotNull String desc = descQiLine.getTrimmedValue();

				values[i][0] = field;
				values[i][1] = desc;
				values[i][2] = props;
			}	
		}	
			
		valuesDone = true;
	}

	private synchronized void cleanup()
	{
		state = ResultThreadState.RS_UNKNOWN;
		prologue = "Stopped!";

		// Read the remainder of the response from Qi (and dispose).
		try
		{
			if (qiLine != null && qiLine.getCode() < QiAPI.LR_OK)
			{
				String buffer;
				while ((buffer = readQi()) != null)
				{
					qiLine = new QiLine(buffer);
					if (qiLine.getCode() >= QiAPI.LR_OK) {
						break;
					}
				}
			}

			if (!connection.authenticated()) {
				connection.disconnect();
			}
		}
		catch (@NotNull IOException | QiProtocolException e)
		{
			error = true;
			showStatus("Error: " + e);
		}
	}

	/**
	 * 
	 * It's here that the connection is established with the Qi server.
	 *
	 * @param aCommandLine the command to send to the server.
	 * @param aConnection a QiConnection. Connection does not need to be open.
	 *
	 */
	private synchronized void connect(final @Nullable String aCommandLine, final @NotNull Connection aConnection)
	{
		commandLine = aCommandLine;
		connection = aConnection;
		
		command = (commandLine == null	? null : (String) (new StringTokenizer(commandLine).nextElement()));
		
		if (!connection.connected())
		{
			try
			{
				connection.connect();
			}
			catch (final IOException e)
			{
				error = true;
				showStatus("Error: " + e);
				showStatus("Error: Could not connect to " + connection.getServer().getExpandedName());
			}
		}
	}

	private String readQi() throws IOException
	{
		return connection.readQI();
	}

	private void showStatus(final String status)
	{
		if (parent != null)
		{
			SwingUtilities.invokeLater(() -> {
				parent.showStatus(status);
				parent.log(status);
			});
		}
		else
		{
			System.err.println(status);
		}
	}

	private void writeQi(final @NotNull String aString) throws IOException
	{
		try
		{
			connection.writeQI(aString);
		}
		catch (final IOException e)
		{
			error = true;
			showStatus("Error: " + e);
		}
	}
}
