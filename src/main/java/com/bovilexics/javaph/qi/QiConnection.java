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

import com.bovilexics.javaph.logging.OutLogger;
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
public class QiConnection implements Connection
{
	private static final @NotNull String QI_SOCKET_UNINITIALIZED = "Qi socket uninitialized";

	// TODO does this support authentication?
	private boolean authenticated = false;
	private boolean connected = false;
	private boolean locked = false;

	private final int port;
	private final @NotNull String host;
	
	private final @NotNull Server qiServer;
	private final @NotNull LineFactory lineFactory;
	private @Nullable Socket socket;
	private @Nullable Thread locker;
		
	private @Nullable BufferedReader fromServer;
	private @Nullable BufferedWriter toServer;

	/**
	 * Creates a QiConnection from a QiServer object which must then be initialized using
	 * <b>connect(host, port)</b>
	 */
	public QiConnection(final @NotNull Server server, final @NotNull LineFactory lineFactory)
	{
		host = server.getServer();
		port = server.getPort();
		qiServer = server;
		this.lineFactory = lineFactory;
	}

	@Override
	public boolean authenticated()
	{
		return authenticated;
	}

	@Override
	public synchronized void connect() throws IOException
	{
		if (host.isEmpty()) {
			throw new IOException("No host specified, cannot connect");
		}

		socket = new Socket();
		// Timeout after 10 seconds
		final @NotNull SocketAddress socketAddress = new InetSocketAddress(host, port);
		final int TIMEOUT = 10000;
		socket.connect(socketAddress, TIMEOUT);
		
		fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		toServer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		connected = true;
	}

	@Override
	public boolean connected()
	{
		return connected;
	}

	/**
	 * Drop the connection to Qi.
	 *
	*/
	@Override
	public synchronized void disconnect() throws IOException
	{
		if (!connected) {
			return;
		}

		try
		{
			writeQI(QiCommand.QUIT + "\n");
		}
		finally
		{
			try
			{
				assert toServer != null;
				toServer.close();
				assert socket != null;
				socket.close();
			}
			finally
			{
				connected = false;
			}
		}
	}

	@Override
	public @NotNull String getHost()
	{
		return host;
	}

	@Override
	public int getPort()
	{
		return port;
	}

	@Override
	public @NotNull String getExpandedName()
	{
		return qiServer.getExpandedName();
	}

	@Override
	public @NotNull LineFactory getLineFactory()
	{
		return lineFactory;
	}

	/**
	 * All threads intending to use shared connection to Qi should use the locking
	 * mechanism provided by lock() and unlock(). This avoids a situation in which
	 * multiple conversations with Qi occur over a single connection.
	 */
	@Override
	public synchronized void lock()
	{
		// Safeguard against deadlock.
		// (whereby a thread calls lock() more than once).
		//noinspection ObjectEquality
		if (locker == Thread.currentThread()) {
			return;
		}

		while (locked)
		{
			try
			{
				wait();
			}
			catch (final @NotNull InterruptedException ignored)
			{
			}
		}
		locked = true;
		locker = Thread.currentThread();
	}
	
	@Override
	public synchronized void login(final @NotNull String alias, final @NotNull String password) throws IOException, QiProtocolException
	{
		lock();

		try
		{
			writeQI(QiCommand.LOGIN + " " + alias + "\n");

			// Read server's response.
			// Expecting: "301:"B`":X8Z;9)!CH0/"H\^GQWD-P7TEAD3".G['%W20:"
			@NotNull String blurb = "";
			while (true)
			{
				final @Nullable String buffer = readQI();
				final @NotNull Line qiQiLine = lineFactory.create(buffer);
				
				if (qiQiLine.getCode() == QiAPI.LR_LOGIN) {
					break;
				} else if (qiQiLine.getCode() < QiAPI.LR_OK) {
					blurb += qiQiLine.getResponse() + " ";
				} else {
					throw new QiProtocolException(qiQiLine.getResponse());
				}
			}

			// "No Hostname found for IP address" maybe.
			if (!blurb.isEmpty()) {
				OutLogger.instance.println("Error on Qi login: " + blurb);
			}

			// Send password.
			writeQI(QiCommand.CLEAR + " " + password + "\n");

			// Read server's response.
			// Expecting: "200:myname:Hi how are you?"
			@NotNull String blurb2 = "";
			while (true)
			{
				final @Nullable String buffer = readQI();
				final @NotNull Line qiQiLine = lineFactory.create(buffer);
				if (qiQiLine.getCode() == QiAPI.LR_OK)
				{
					// "No Hostname found for IP address" maybe.
					if (!blurb2.isEmpty())
					{
						OutLogger.instance.println("Error on Qi login: " + blurb2);
					}
						
					authenticated = true;
					break;
				}
				else if (qiQiLine.getCode() < QiAPI.LR_OK)
				{
					blurb2 += qiQiLine.getResponse() + " ";
				}
				else if (qiQiLine.getCode() == QiAPI.LR_ERROR) // "500:Login failed."
				{
					throw new QiProtocolException(qiQiLine.getResponse());
				}
				else
				{
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
	@Override
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
				final @Nullable String buffer = readQI();
				final @NotNull Line qiQiLine = lineFactory.create(buffer);
				if (qiQiLine.getCode() == QiAPI.LR_OK)
				{
					// "No Hostname found for IP address" maybe.
					if (!blurb.isEmpty()) {
						OutLogger.instance.println("Error on Qi logout: " + blurb);
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

	@Override
	public @Nullable String readQI() throws IOException
	{
		if (fromServer != null)
		{
			return fromServer.readLine();
		}
		else
		{
			throw new IOException(QI_SOCKET_UNINITIALIZED);
		}
	}

	@Override
	public @NotNull String toString()
	{
		final @NotNull StringBuilder out = new StringBuilder();
		
		out.append(host);
		out.append(":");
		out.append(port);
		out.append(" {connected=");
		out.append(connected);
		out.append(", authenticated=");
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
	@Override
	public synchronized void unlock()
	{
		locked = false;
		locker = null;
		notify();
	}

	@Override
	public void writeQI(final @NotNull String string) throws IOException
	{
		if (toServer == null)
		{
			throw new IOException(QI_SOCKET_UNINITIALIZED);
		}

		toServer.write(string);
		toServer.flush();
	}
}
