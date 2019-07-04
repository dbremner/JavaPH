package com.bovilexics.javaph.qi;

import com.bovilexics.javaph.JavaPHConstants;
import org.jetbrains.annotations.NonNls;
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

final class LiveConnectionHelper implements ConnectionHelper
{
    private final @NotNull Socket socket;
    private final @NotNull BufferedReader fromServer;
    private final @NotNull BufferedWriter toServer;
    private boolean closed = false;

    LiveConnectionHelper(final @NotNull String host, final int port) throws IOException
    {
        socket = new Socket();
        // Timeout after 10 seconds
        final @NotNull SocketAddress socketAddress = new InetSocketAddress(host, port);
        final int TIMEOUT = 10000;
        socket.connect(socketAddress, TIMEOUT);

        fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        toServer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void close() throws IOException
    {
        if (closed)
        {
            return;
        }

        fromServer.close();
        toServer.close();
        socket.close();
        closed = true;
    }

    @Override
    public @Nullable String readLine() throws IOException
    {
        if (closed)
        {
            throw new IOException(JavaPHConstants.CONNECTION_HAS_BEEN_CLOSED);
        }

        return fromServer.readLine();
    }

    @Override
    public void writeLine(final @NotNull String string) throws IOException
    {
        if (closed)
        {
            throw new IOException(JavaPHConstants.CONNECTION_HAS_BEEN_CLOSED);
        }

        toServer.write(string);
        toServer.flush();
    }

    @Override
    public boolean connected()
    {
        return !closed;
    }

    @NonNls
    @Override
    public @NotNull String toString()
    {
        return String.format("LiveConnectionHelper{socket=%s, fromServer=%s, toServer=%s, closed=%s}", socket, fromServer, toServer, closed);
    }
}
