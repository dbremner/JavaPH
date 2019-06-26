package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface ServerFactory
{
    @SuppressWarnings("UnusedReturnValue")
    @Contract(pure = true)
    @NotNull Server create(final @NotNull String name, final @NotNull String server, final int port) throws IllegalArgumentException;

    @Contract(pure = true)
    @NotNull Server create(final @NotNull String name, final @NotNull String server, final @NotNull String port)  throws IllegalArgumentException;
}
