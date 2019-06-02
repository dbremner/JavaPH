package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public interface Connection
{
    boolean authenticated();

    /** Establish a connection to Qi.
      *
      * @exception IOException
     */
    void connect() throws IOException;

    /**
     * Establish a connection to Qi.
     *
     * @param aHost host to connect to.
     * @param aPort port to connect to.
     *
     */
    void connect(@NotNull String aHost, int aPort) throws IOException;

    boolean connected();

    /**
     * Drop the connection to Qi.
     *
    */
    void disconnect() throws IOException;

    @NotNull String getHost();

    int getPort();

    @NotNull QiServer getServer();

    /**
     * All threads intending to use shared connection to Qi should use the locking
     * mechanism provided by lock() and unlock(). This avoids a situation in which
     * multiple conversations with Qi occur over a single connection.
     */
    void lock();

    /**
     * Login to  QI.
     *
     * @param anAlias
     * @param aPassword
     *
     * @exception QiProtocolException upon an unexpected response from Qi
     * @exception IOException upon a socket error.
     */
    void login(@NotNull String anAlias, @NotNull String aPassword) throws IOException, QiProtocolException;

    /**
     * Logout from QI.
     *
     * @exception QiProtocolException upon an unexpected response from Qi
     * @exception IOException upon a socket error.
     */
    void logout() throws QiProtocolException, IOException;

    String readQI() throws IOException;

    /**
     * All threads intending to use shared connection to Qi should use
     * the locking mechanism provided by lock() and unlock(). This avoids
     * a situation in which multiple conversations with Qi occur over a single
     * connection. This is bad.
     */
    void unlock();

    void writeQI(@NotNull String string) throws IOException;
}
