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

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.SwingUtilities;

import com.bovilexics.javaph.JavaPH;
import com.bovilexics.javaph.qi.QiAPI;
import com.bovilexics.javaph.qi.QiCommand;
import com.bovilexics.javaph.qi.QiConnection;
import com.bovilexics.javaph.qi.QiLine;
import com.bovilexics.javaph.qi.QiProtocolException;
import com.bovilexics.javaph.qi.QiServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 
 * A <B>ResultThread</B> builds an entire reponse from a Qi query.
 * 
 * A response from Qi as defined by this class is composed of three
 * distinct parts; prologue, multiline response and epilogue. There are
 * accessor methods to retrieve each of these 3 parts. For any given QI
 * reponse, any or all of the parts may be empty/null and this should be
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
	// States
	private final static int RS_START		= 0; // Gettin' goin'.
	private final static int RS_INPROGRESS	= 1; // Multiline response.
	private final static int RS_OK			= 2; // We're done.
	private final static int RS_UNKNOWN		= 3; // Oops!
	private final static int RS_ERROR		= 4; // Qi error response.
	
	private boolean error = false;
	private boolean finished = false;
	private boolean halted = false;
	private boolean headersDone = false;
	private boolean valuesDone = false;
	
	private int entryIndex;	
	private int lastCode = QiAPI.LR_OK;
	private int state = RS_START;

	private final JavaPH parent;

	@Nullable
	private Object[] headers = null;
	@Nullable
	private Object[][] values = null;

	private QiConnection qiConnection;
	private QiLine qiLine;

	@Nullable
	private String command;
	private String commandLine;
	@NotNull
	private String epilogue = "";
	@NotNull
	private String prologue = "";
	private String readFromServer;

	// Seperator used in the display of queries which matched multiple records.
	@NotNull
	private final String recordSeperator = "------------------------------------------------------------\n";

	private StringBuffer rawResult;

	@NotNull
	private final Vector records = new Vector();
	@NotNull
	private Vector record = new Vector();

	public ResultThread(String command, QiConnection connection)
	{
		this(null, command, connection);
	}

	public ResultThread(String command, @NotNull QiServer server)
	{
		this(null, command, new QiConnection(server));
	}


	public ResultThread(JavaPH javaph)
	{
		parent = javaph;
		connect(parent.getCommand(), parent.getConnection());
	}

	private ResultThread(JavaPH javaph, String command, QiConnection connection)
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
	@Nullable
	public synchronized String fieldValue(int record, String field, boolean replaceNewlines)
	{

		@NotNull StringBuffer out = new StringBuffer();

		@Nullable Vector records = null;

		if ((records = (Vector) records.elementAt(record)) == null) {
			return null;
		}

		boolean gotField = false;
		for (int i = 0; i < records.size(); i++)
		{
			@Nullable QiLine qiLine = (QiLine) records.elementAt(i);

			// Skip empty and encrypted fields.
			if ((!gotField && !qiLine.getTrimmedField().equals(field)) || qiLine.getCode() != -QiAPI.LR_OK || qiLine.getTrimmedValue().equals("")) {
				continue;
			}
				
			gotField = true;

			// We're done.
			if (!qiLine.getTrimmedField().equals(field) && !qiLine.getTrimmedField().equals("")) {
				break;
			}

			out.append(qiLine.getTrimmedValue());
			out.append(replaceNewlines ? "\n" : " ");
		}
		return out.toString();
	}

	@Nullable
	public synchronized String getCommand()
	{
		return command;
	}

	@Nullable
	public synchronized String getEpilogue()
	{
		return state == RS_OK ? epilogue : null;
	}

	@Nullable
	public String getErrorString()
	{
		return state == RS_ERROR || state == RS_UNKNOWN ? prologue : null;
	}

	public synchronized int getFieldCount()
	{
		return state == RS_OK ? headers.length : 0;
	}

	@Nullable
	public synchronized Object[] getHeaders()
	{
		return state == RS_OK ? headers : null;
	}

	public int getLastCode()
	{
		return lastCode;
	}

	@Nullable
	public synchronized String getPrologue()
	{
		return state == RS_OK ? prologue : null;
	}

	public synchronized int getRecordCount()
	{
		return state == RS_OK ? records.size() : -1;
	}

	@NotNull
	public synchronized Vector getRecords()
	{
		return state == RS_OK ? (Vector)records.clone() : new Vector();
	}

	@Nullable
	public synchronized String getRawResult()
	{
		return state == RS_OK && rawResult.length() > 0 ? rawResult.toString() : null;
	}

	@Nullable
	public synchronized Object[][] getValues()
	{
		return state == RS_OK ? values : null;
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
		return !error && !halted && (state == RS_OK || state == RS_ERROR);
	}

	@Override
	public void run()
	{
		finished = false;

		if (error)
		{
			state = RS_ERROR;
			showStatus("Error: Invalid connection, query stopped.");
			finished = true;
			return;
		}
		
		qiConnection.lock();

		try
		{
			buildResult();
			
			if (!qiConnection.authenticated()) {
				qiConnection.disconnect();
			}
		}
		catch (QiProtocolException e)
		{
			error = true;
			showStatus("Error: " + e);
		}
		catch (IOException e)
		{
			error = true;
			showStatus("Error: " + e);
		}
		finally
		{
			qiConnection.unlock();
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
			case RS_START :
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
					state = RS_OK;
					lastCode = qiLine.getCode();
					break;
				}
				else if ((qiLine.getCode() < -QiAPI.LR_TEMP || qiLine.getCode() > QiAPI.LR_TEMP) && qiLine.getCode() != -QiAPI.LR_ABSENT)
				{
					// This should be an error
					prologue += (qiLine.getResponse() + "\n");
					state = RS_ERROR;
					lastCode = qiLine.getCode();
					break;
				}
				else
				{
					// This implementation of QI must not give LR_NUMRET.
					state = RS_INPROGRESS;

					// fall-through
				}

			case RS_INPROGRESS :
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
							record = new Vector();
						}
						entryIndex = qiLine.getIndex();
					}
					record.addElement(qiLine);
				}
				else if (qiLine.getCode() >= QiAPI.LR_OK)
				{
					// It must be done so finish it up.
					epilogue += (qiLine.getResponse() + "\n");
					addRecord(record);
					state = RS_OK;

				}
				else
				{
					// If this ever happens then I've misinterpreted the protocol.
					state = RS_UNKNOWN;
					throw new QiProtocolException("Unknown State: " + readFromServer);
				}
				break;

			case RS_ERROR :
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
					state = RS_UNKNOWN;
					throw new QiProtocolException("Unknown State: " + readFromServer);
				}
				break;
				
			case RS_OK :
				// "200:Bye!" is all that should seen here (and that is disposed).
				if (qiLine.getCode() != QiAPI.LR_OK)
				{
					state = RS_UNKNOWN;
					throw new QiProtocolException("Unknown State: " + readFromServer);
				}
				break;
		}
	}

	private synchronized void addRecord(Vector aRecord)
	{
		records.addElement(aRecord);
	}

	private synchronized void buildHeaders()
	{
		if (headersDone) {
			return;
		}

		String lastField = "unknown";
		@NotNull Vector uniqueHeaders = new Vector();

		for (int i = 0; i < records.size(); i++)
		{
			Vector currentQiLine = (Vector) records.elementAt(i);

			for (int j = 0; j < currentQiLine.size(); j++)
			{
				String field = ((QiLine) currentQiLine.elementAt(j)).getTrimmedField();

				if (field.equals(""))
				{
					field = lastField + ".1";
				}
				
				if (!uniqueHeaders.contains(field))
				{
					if (field.endsWith(".1")) {
						uniqueHeaders.insertElementAt(field, uniqueHeaders.indexOf(field.substring(0, field.lastIndexOf(".1"))) + 1);
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
				rawResult.append(recordSeperator);
			}

			index = qiLine.getIndex();
			rawResult.append(qiLine.getResponse().equals("") ? qiLine.getField() + " : " + qiLine.getValue() : qiLine.getResponse());
			rawResult.append("\n");
			
			// If code >= LR_OK, Qi has said all it's going to say.
			if (qiLine.getCode() >= QiAPI.LR_OK)
			{
				if (!qiConnection.authenticated())
				{
					qiConnection.disconnect();
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
			state = RS_ERROR;
			prologue = qiLine.getResponse();
			
			@NotNull String message = "Got error " + qiLine.getCode() + " on line --> " + readFromServer;
			
			if (parent == null) {
				System.err.println(message);
			} else {
				parent.log(message);
			}
		}
		else
		{
			state = RS_OK;
			
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

		int xCoord = -1;
		String lastField = "unknown";

		for (int i = 0; i < records.size(); i++)
		{
			int yCoord = i;
			Vector thisVector = (Vector) records.elementAt(i);

			for (int j = 0; j < thisVector.size(); j++)
			{
				QiLine thisQiLine = (QiLine) thisVector.elementAt(j);
				String field = thisQiLine.getTrimmedField();

				if (field.equals(""))
				{
					field = lastField + ".1";
				}

				boolean found = false;
				for (int k = 0; k < headers.length && !found; k++)
				{
					if ( ((String)headers[k]).equals(field) )
					{
						xCoord = k;
						found = true;
					}
				}
				
				if (found)
				{
					values[yCoord][xCoord] = thisQiLine.getTrimmedValue();
				}
				else
				{
					@NotNull String message = "Couldn't find header for this column: " + thisQiLine.toString();
					
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
			Vector thisVector = (Vector) records.elementAt(i);

			for (int j = 0; j < thisVector.size() - 1; j = j + 2)
			{
				QiLine propsQiLine = (QiLine) thisVector.elementAt(j);
				QiLine descQiLine = (QiLine) thisVector.elementAt(j + 1);

				String field = propsQiLine.getTrimmedField();
				String props = propsQiLine.getTrimmedValue();
				String desc = descQiLine.getTrimmedValue();

				values[i][0] = field;
				values[i][1] = desc;
				values[i][2] = props;
			}	
		}	
			
		valuesDone = true;
	}

	private synchronized void cleanup()
	{
		state = RS_UNKNOWN;
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

			if (!qiConnection.authenticated()) {
				qiConnection.disconnect();
			}
		}
		catch (IOException e)
		{
			error = true;
			showStatus("Error: " + e);
		}
		catch (QiProtocolException e)
		{
			error = true;
			showStatus("Error: " + e);
		}
	}

	/**
	 * 
	 * It's here that the connection is established with the Qi server.
	 *
	 * @param commandQiLine the command to send to the server.
	 * @param qiConnection a QiConnection. Connection does not need to be open.
	 *
	 * @exception IOException in the event of an error with (establishing)
	 *             the connection to the Qi server.
	 *
	 */
	private synchronized void connect(String aCommandLine, QiConnection aQiConnection)
	{
		commandLine = aCommandLine;
		qiConnection = aQiConnection;
		
		command = (commandLine == null	? null : (String) (new StringTokenizer(commandLine).nextElement()));
		
		if (!qiConnection.connected())
		{
			try
			{
				qiConnection.connect();
			}
			catch (IOException e)
			{
				error = true;
				showStatus("Error: " + e);
				showStatus("Error: Could not connect to " + qiConnection.getServer().getExpandedName());
			}
		}
	}

	private String readQi() throws IOException
	{
		return qiConnection.readQI();
	}

	private void showStatus(String status)
	{
		showStatus(status, true);
	}

	private void showStatus(final String status, final boolean logAlso)
	{
		if (parent != null)
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run()
				{
					parent.showStatus(status);
					
					if (logAlso) {
						parent.log(status);
					}
				}
			});
		}
		else
		{
			System.err.println(status);
		}
	}

	private void writeQi(@NotNull String aString) throws IOException
	{
		try
		{
			qiConnection.writeQI(aString);
		}
		catch (IOException e)
		{
			error = true;
			showStatus("Error: " + e);
		}
	}
}
