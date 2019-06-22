package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.NotNull;

public interface ServerFactory
{
    @SuppressWarnings("UnusedReturnValue")
    @NotNull Server create(final @NotNull String name, final @NotNull String server, final int port) throws IllegalArgumentException;

    @NotNull Server create(final @NotNull String name, final @NotNull String server, final @NotNull String port)  throws IllegalArgumentException;
}
