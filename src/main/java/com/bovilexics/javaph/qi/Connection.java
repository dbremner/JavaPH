package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public interface Connection
{
    @Contract(pure = true)
    boolean authenticated();

    /** Establish a connection to Qi.
      *
      */
    void connect() throws IOException;

    @Contract(pure = true)
    boolean connected();

    /**
     * Drop the connection to Qi.
     *
    */
    void disconnect() throws IOException;

    @Contract(pure = true)
    @NotNull String getHost();

    @Contract(pure = true)
    int getPort();

    @Contract(pure = true)
    @NotNull LineFactory getLineFactory();

    /**
     * All threads intending to use shared connection to Qi should use the locking
     * mechanism provided by lock() and unlock(). This avoids a situation in which
     * multiple conversations with Qi occur over a single connection.
     */
    void lock();

    /**
     * Login to  QI.
     *
     * @param alias alias
     * @param password password
     *
     * @exception QiProtocolException upon an unexpected response from Qi
     * @exception IOException upon a socket error.
     */
    void login(@NotNull String alias, @NotNull String password) throws IOException, QiProtocolException;

    /**
     * Logout from QI.
     *
     * @exception QiProtocolException upon an unexpected response from Qi
     * @exception IOException upon a socket error.
     */
    void logout() throws QiProtocolException, IOException;

    @Nullable String readQI() throws IOException;

    /**
     * All threads intending to use shared connection to Qi should use
     * the locking mechanism provided by lock() and unlock(). This avoids
     * a situation in which multiple conversations with Qi occur over a single
     * connection. This is bad.
     */
    void unlock();

    void writeQI(@NotNull String string) throws IOException;
}
