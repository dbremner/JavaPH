package com.bovilexics.javaph.logging;

import org.jetbrains.annotations.NotNull;

public final class OutLogger implements Logger
{
    public static final Logger instance = new OutLogger();

    @Override
    public void println(final @NotNull String text)
    {
        System.out.println(text);
    }
}
