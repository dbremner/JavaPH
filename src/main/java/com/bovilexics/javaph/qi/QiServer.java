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

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.net.URL;
import java.util.Vector;

import com.bovilexics.javaph.threads.ResultThread;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public class QiServer
{
	public static final int FIELD_LOAD_ERROR = 0;
	public static final int FIELD_LOAD_FALSE = 1;
	public static final int FIELD_LOAD_TRUE  = 2;
	public static final int SERVER_ERROR     = 3;

	private static final int QUERY_RUNTIME = 10;
	
	private static final String SEPARATOR = "::";
	private static final String SERVER_FILE = "javaph.servers";

	private static final String PORT_ERROR = "Error: invalid port value passed into QiServer, must be a an integer between 0 and 65,535";

	private static QiServer defaultServer;
	@NotNull
    private static final Vector servers = new Vector();

	private final String name;
	private final String server;
	@Nullable
    private final Integer port;
	@NotNull
    private Vector fields = new Vector();
	private int fieldState = FIELD_LOAD_FALSE;
	private String fieldStateMessage;
	
	public QiServer(String aName, String aServer, @NotNull Integer aPortInteger)
	{
		if (!isValidPort(aPortInteger))
			throw new IllegalArgumentException(PORT_ERROR);
		
		name = aName;
		server = aServer;
		port = aPortInteger;
	}

	private QiServer(String aName, String aServer, @NotNull String aPort)
	{
		@Nullable Integer aPortInteger;
		
		try
		{
			aPortInteger = new Integer(aPort);
			
			if (!isValidPort(aPortInteger))
				throw new IllegalArgumentException(PORT_ERROR);
		}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException(PORT_ERROR);
		}
		
		name = aName;
		server = aServer;
		port = aPortInteger;
	}


	public static void addServer(@Nullable QiServer server)
	{
		int whereToAdd = -1;
		
		if (server == null)
			return;

		if (servers.size() == 0)
		{
			whereToAdd = 0;
		}
		else
		{
			String newElement = server.toString();
			String oldElement;

			for (int i = 0; i < servers.size(); i++)
			{
				oldElement = servers.elementAt(i).toString();

				if (newElement.compareTo(oldElement) == 0)
				{
					return;
				}
				else if (newElement.compareTo(oldElement) < 0)
				{
					whereToAdd = i;
					break;
				}
			}

			if (whereToAdd == -1)
				whereToAdd = servers.size();
		}
		servers.insertElementAt(server, whereToAdd);
	}

	@NotNull
    public static QiServer getDefaultServer()
	{
		return (defaultServer == null) ? new QiServer("undefined", "undefined", "0") : defaultServer;
	}

	@NotNull
    public static String getFileHeader()
	{
		@NotNull StringBuffer out = new StringBuffer();
		
		out.append("# JavaPH Server File\n");
		out.append("# \n");
		out.append("# Manually add servers to this list using the following format\n");
		out.append("#   <name / description> :: <host / ip address> :: <port (0-65535)>\n");
		out.append("# \n");

		return out.toString();
	}

	@NotNull
    public static Vector getServers()
	{
		return servers;
	}

	public static void loadAllFields()
	{
		QiServer server;
		
		for (int i = 0; i < servers.size(); i++)
		{
			server = (QiServer)servers.elementAt(i);
			
			if (server.getFieldState() != FIELD_LOAD_ERROR)
				server.loadFields();
		}
	}

	public static void loadAllServers()
	{
		loadAllServers(null);
	}

	private static void loadAllServers(@Nullable URL useURL)
	{
		servers.removeAllElements();
		
		QiServer server;
		
		try
		{
			Reader in;
			
			if (useURL != null)
				in = new InputStreamReader((new URL(useURL.toString() + SERVER_FILE)).openStream());
			else
				in = new FileReader(SERVER_FILE);
			
			@NotNull LineNumberReader lr = new LineNumberReader(in);
			
			String line;
			String[] items;

			while ((line = lr.readLine()) != null)
			{
				// Ignore comment lines
				if (line.startsWith("#"))
					continue;
					
				items = line.split(SEPARATOR);
				
				if (items.length != 3)
				{
					System.err.println("Error: Invalid server entry in " + SERVER_FILE + " on line " + lr.getLineNumber() + " --> " + line);
				}
				else
				{
					server = new QiServer(items[0], items[1], items[2]);
					addServer(server);
				}
			}
			
			defaultServer = (QiServer)servers.elementAt(0);
		}
		catch (FileNotFoundException e)
		{
			System.err.println("Error: FileNotFoundException received when trying to read file " + SERVER_FILE);
			e.printStackTrace();
		}
		catch (IOException e)
		{
			System.err.println("Error: IOException received when trying to read file " + SERVER_FILE);
			e.printStackTrace();
		}
	}

	public static void removeServer(QiServer server)
	{
		servers.remove(server);
	}

	public static void saveServers()
	{
		QiServer server;
		StringBuffer toWrite;
		
		try
		{
			@NotNull BufferedWriter writer = new BufferedWriter(new FileWriter(SERVER_FILE));

			writer.write(getFileHeader());
			writer.flush();

			for (int i = 0; i < servers.size(); i++)
			{
				server = (QiServer)servers.elementAt(i);
								
				toWrite = new StringBuffer();
				toWrite.append(server.getName());
				toWrite.append(SEPARATOR);
				toWrite.append(server.getServer());
				toWrite.append(SEPARATOR);
				toWrite.append(server.getPort());
				
				if (i < servers.size() - 1)
					toWrite.append("\n");
				
				writer.write(toWrite.toString());
				writer.flush();
			}
			writer.close();
		}
		catch (IOException e)
		{
			System.err.println("Error: IOException received when trying to write file " + SERVER_FILE);
			e.printStackTrace();
		}
	}

	public static void setDefaultServer(@Nullable String server)
	{
		if (server == null)
			return;
			
		for (int i = 0; i < servers.size(); i++)
		{
			if (((QiServer)servers.elementAt(i)).toString().equals(server))
			{
				defaultServer = (QiServer)servers.elementAt(i);
				return;
			}
		}
	}
	
	private void convertRecordsToFields(@NotNull Vector records)
	{
		fields = new Vector();
		
		QiLine descLine;
		QiLine propsLine;
		String descField;
		String descValue;
		String propsField;
		String propsValue;
		Vector record;
		
		for (int i = 0; i < records.size(); i++)
		{
			record = (Vector)records.elementAt(i);
			
			// record should contain pairs of field property/description lines 
			for (int j = 0; j < record.size() - 1; j += 2)
			{
				propsLine = (QiLine)record.elementAt(j);
				descLine = (QiLine)record.elementAt(j + 1);
				
				propsField = propsLine.getTrimmedField();
				propsValue = propsLine.getTrimmedValue();
				descField = descLine.getTrimmedField();
				descValue = descLine.getTrimmedValue();
				
				if (propsField.equals(descField))
				{
					// Do not add this field if it is one of the special fields already handled elsewhere
					if (propsField.equalsIgnoreCase("any") || propsField.equalsIgnoreCase("all"))
						continue;

					try
					{
						fields.addElement(new QiField(propsField, propsValue, descValue));
					}
					catch (QiProtocolException e)
					{
						fieldState = FIELD_LOAD_ERROR;
						System.err.println("Error: QiProtocolException received when trying to add field to " + getExpandedName());
						System.err.println(" --> Message:     " + e.getMessage());
						System.err.println(" --> Properties:  " + propsLine.toString());
						System.err.println(" --> Description: " + descLine.toString());
					}
				}
				else
				{
					fieldState = FIELD_LOAD_ERROR;
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
		@NotNull StringBuffer out = new StringBuffer();
		
		out.append(name);
		out.append(" (");
		out.append(server);
		out.append(":");
		out.append(port.toString());
		out.append(")");
		
		return out.toString();
	}

	@NotNull
    public Vector getFields()
	{
		return (Vector)fields.clone();
	}

	public int getFieldState()
	{
		return fieldState;
	}

	public String getFieldStateMessage()
	{
		if (fieldStateMessage == null)
			fieldStateMessage = "Fields not yet loaded for " + getExpandedName();
			
		return fieldStateMessage;
	}

	public String getName()
	{
		return name;
	}
	
	@Nullable
    public Integer getPort()
	{
		return port;
	}
	
	public String getServer()
	{
		return server;
	}

	private boolean isValidPort(@NotNull Integer port)
	{
		return (port.intValue() >= 0 && port.intValue() <= 65535);
	}

	public void loadFields()
	{
		int seconds = 0;

		@Nullable ResultThread resultThread = new ResultThread(QiCommand.FIELDS, this);
		resultThread.start();

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
			fieldState = FIELD_LOAD_ERROR;
			fieldStateMessage = "Timeout trying to load fields for " + getExpandedName();
		}
		else if (resultThread.getRecordCount() < 0)
		{
			fieldState = SERVER_ERROR;
			fieldStateMessage = "Server error trying to load fields for " + getExpandedName();
		}
		else if (resultThread.getRecordCount() == 0)
		{
			fieldState = FIELD_LOAD_ERROR;
			fieldStateMessage = "No fields available for " + getExpandedName();
		}
		else
		{
			convertRecordsToFields(resultThread.getRecords());
			fieldState = FIELD_LOAD_TRUE;
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
