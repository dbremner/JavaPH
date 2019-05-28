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
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/** 
 * This class represents a connection to Qi. Use this class instead of
 * opening a direct connection to Qi. <b>QiConnection</b> handles
 * locking issues etc.
 *
 * @author Robert Fernandes robert@bovilexics.com
 *
 */
public class QiConnection
{
	private boolean authenticated;
	private boolean connected;
	private boolean locked;

	private int port;
	
	private String alias;
	private String host;
	
	private QiLine qiQiLine;
	@NotNull
    private final QiServer qiServer;
	private Socket socket;
	@Nullable
	private Thread locker;
		
	private BufferedReader fromServer;
	private BufferedWriter toServer;

	/**
	 * Creates a QiConnection from a QiServer object which must then be initialized using
	 * <b>connect(host, port)</b>
	 */
	public QiConnection(@NotNull QiServer server)
	{
		host = server.getServer();
		port = server.getPort().intValue();
		qiServer = server;
	}

	/**
	 * Creates a QiConnection with an which will connect to host:port
	 * once the <b>connect</b> method is called.
	 * <i>connect(host, port)</i>
	 *
	 * @param aHost to connect to
	 * @param aPort to connect to.
	 */
	public QiConnection(String aHost, int aPort)
	{
		host = aHost;
		port = aPort;
		qiServer = new QiServer("unspecified", aHost, new Integer(aPort));
	}

	public boolean authenticated()
	{
		return authenticated;
	}

	/** Establish a connection to Qi.
	  *
	  * @exception IOException
	 */
	public synchronized void connect() throws IOException
	{
		if (host == null || host.equals("")) {
			throw new IOException("No host specified, cannot connect");
		}
			
		@NotNull final SocketAddress sockaddr = new InetSocketAddress(host, port);
		socket = new Socket();
		
		// Timeout after 10 seconds
		socket.connect(sockaddr, 10000);
		
		fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		toServer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		connected = true;
	}

	/**
	 * Establish a connection to Qi.
	 *
	 * @param aHost host to connect to.
	 * @param aPort port to connect to.
	 *
	 */
	public synchronized void connect(String aHost, int aPort) throws IOException
	{
		host = aHost;
		port = aPort;
		connect();
	}

	public boolean connected()
	{
		return connected;
	}

	/**
	 * Drop the connection to Qi.
	 *
	*/
	public synchronized void disconnect() throws IOException
	{
		if (!connected) {
			return;
		}

		try
		{
			writeQI(QiCommand.QUIT + "\n");
		}
		catch (IOException e)
		{
			throw e;
		}
		finally
		{
			try
			{
				toServer.close();
				socket.close();
			}
			catch (IOException e)
			{
				throw e;
			}
			finally
			{
				connected = false;
			}
		}
	}

	public String getHost()
	{
		return host;
	}

	public int getPort()
	{
		return port;
	}

	@NotNull
    public QiServer getServer()
	{
		return qiServer;
	}

	/**
	 * All threads intending to use shared connection to Qi should use the locking
	 * mechanism provided by lock() and unlock(). This avoids a situation in which
	 * multiple conversations with Qi occur over a single connection.
	 */
	public synchronized void lock()
	{
		// Safeguard against deadlock.
		// (whereby a thread calls lock() more than once).
		if (locker == Thread.currentThread()) {
			return;
		}

		while (locked)
		{
			try
			{
				wait();
			}
			catch (InterruptedException e)
			{
			}
		}
		locked = true;
		locker = Thread.currentThread();
	}
	
	/**
	 * Login to  QI.
	 *
	 * @param anAlias
	 * @param aPassword
	 *
	 * @exception QiProtocolException upon an unexpected response from Qi
	 * @exception IOException upon a socket error.
	 */
	public synchronized void login(String anAlias, String aPassword) throws IOException, QiProtocolException
	{
		alias = anAlias;
		lock();

		try
		{
			writeQI(QiCommand.LOGIN + " " + alias + "\n");

			// Read server's response.
			// Expecting: "301:"B`":X8Z;9)!CH0/"H\^GQWD-P7TEAD3".G['%W20:"
			@NotNull String blurb = "";
			@NotNull String buffer;
			while (true)
			{
				buffer = readQI();
				qiQiLine = new QiLine(buffer);
				
				if (qiQiLine.getCode() == QiAPI.LR_LOGIN) {
					break;
				} else if (qiQiLine.getCode() < QiAPI.LR_OK) {
					blurb += qiQiLine.getResponse() + " ";
				} else {
					throw new QiProtocolException(qiQiLine.getResponse());
				}
			}

			// "No Hostname found for IP address" maybe.
			if (!blurb.equals("")) {
				System.out.println("Error on Qi login: " + blurb);
			}

			blurb = "";
			
			// Send password.
			writeQI(QiCommand.CLEAR + " " + aPassword + "\n");

			// Read server's response.
			// Expecting: "200:myname:Hi how are you?"
			while (true)
			{
				buffer = readQI();
				qiQiLine = new QiLine(buffer);
				if (qiQiLine.getCode() == QiAPI.LR_OK)
				{
					// "No Hostname found for IP address" maybe.
					if (!blurb.equals("")) {
						System.out.println("Error on Qi login: " + blurb);
					}
						
					authenticated = true;
					break;
				}
				else if (qiQiLine.getCode() < QiAPI.LR_OK) {
					blurb += qiQiLine.getResponse() + " ";
				} else if (qiQiLine.getCode() == QiAPI.LR_ERROR) // "500:Login failed."
				{
					throw new QiProtocolException(qiQiLine.getResponse());
				} else {
					throw new QiProtocolException(qiQiLine.getResponse());
				}
			}
		}
		finally
		{
			unlock();
		}
	}

	/**
	 * Logout from QI.
	 *
	 * @exception QiProtocolException upon an unexpected response from Qi
	 * @exception IOException upon a socket error.
	 */
	public synchronized void logout() throws QiProtocolException, IOException
	{

		if (!authenticated) {
			return;
		}
			
		lock();

		try
		{
			// Log out.
			writeQI(QiCommand.LOGOUT + "\n");

			// Read server's response.
			// Expecting: "200:Ok."
			@NotNull String blurb = "";
			while (true)
			{
				@NotNull final String buffer = readQI();
				qiQiLine = new QiLine(buffer);
				if (qiQiLine.getCode() == QiAPI.LR_OK)
				{
					// "No Hostname found for IP address" maybe.
					if (!blurb.equals("")) {
						System.out.println("Error on Qi logout: " + blurb);
					}
						
					authenticated = false;
					break;
				}
				else if (qiQiLine.getCode() < QiAPI.LR_OK) {
					blurb += qiQiLine.getResponse() + " ";
				} else if (qiQiLine.getCode() == QiAPI.LR_ERROR) // "500:Login failed."
				{
					throw new QiProtocolException(qiQiLine.getResponse());
				} else {
					throw new QiProtocolException(qiQiLine.getResponse());
				}
			}
		}
		finally
		{
			unlock();
		}
	}	

	public String readQI() throws IOException
	{
		if (fromServer != null)
		{
			return fromServer.readLine();
		}
		else
		{
			throw new IOException("Qi socket uninitialized");
		}
	}

	@Override
	@NotNull
	public String toString()
	{
		@NotNull final StringBuffer out = new StringBuffer();
		
		out.append(host);
		out.append(":");
		out.append(port);
		out.append(" {connected=");
		out.append(connected);
		out.append(", authenticted=");
		out.append(authenticated);
		out.append(", locked=");
		out.append(locked);
		
		if (locked)
		{
			out.append("(");
			out.append(locker);
			out.append(")}");
		}
		else
		{
			out.append("}");
		}
				
		return out.toString();
	}

	/**
	 * All threads intending to use shared connection to Qi should use
	 * the locking mechanism provided by lock() and unlock(). This avoids
	 * a situation in which multiple conversations with Qi occur over a single
	 * connection. This is bad.
	 */
	public synchronized void unlock()
	{
		locked = false;
		locker = null;
		notify();
	}

	public void writeQI(@NotNull String string) throws IOException
	{
		if (toServer != null)
		{
			toServer.write(string);
			toServer.flush();
		}
		else
		{
			throw new IOException("Qi socket uninitialized");
		}
	}
}
