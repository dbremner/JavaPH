package com.bovilexics.javaph.logging;

import org.jetbrains.annotations.NotNull;

public final class OutLogger implements Logger
{
    public static final @NotNull Logger instance = new OutLogger();

    @Override
    public void printStackTrace(final @NotNull Exception exception)
    {
        exception.printStackTrace(System.out);
    }

    @Override
    public void println(final @NotNull String text)
    {
        System.out.println(text);
    }
}
