package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.NotNull;

public interface ConnectionFactory
{
    /**
     * Creates a QiConnection with an which will connect to host:port
     * once the <b>connect</b> method is called.
     * <i>connect(host, port)</i>
     *
     * @param aHost to connect to
     * @param aPort to connect to.
     */
    @NotNull Connection create(@NotNull String aHost, int aPort);

    /**
     * Creates a QiConnection from a QiServer object which must then be initialized using
     * <b>connect(host, port)</b>
     */
    @NotNull Connection create(@NotNull Server server);
}
