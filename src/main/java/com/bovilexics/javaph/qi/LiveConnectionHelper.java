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

final class LiveConnectionHelper implements ConnectionHelper
{
    private final @NotNull Socket socket;
    private final @NotNull BufferedReader fromServer;
    private final @NotNull BufferedWriter toServer;

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
        fromServer.close();
        toServer.close();
        socket.close();
    }

    @Override
    public @Nullable String readLine() throws IOException
    {
        return fromServer.readLine();
    }

    @Override
    public void writeLine(final @NotNull String string) throws IOException
    {
        toServer.write(string);
        toServer.flush();
    }

    @Override
    public boolean connected()
    {
        return true;
    }

}
