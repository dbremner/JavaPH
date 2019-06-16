package com.bovilexics.javaph.logging;

import org.jetbrains.annotations.NotNull;

public final class ErrLogger implements Logger
{
    public static final @NotNull Logger instance = new ErrLogger();

    @Override
    public void printStackTrace(final @NotNull Exception exception)
    {
        exception.printStackTrace(System.err);
    }

    @Override
    public void println(final @NotNull String text)
    {
        System.err.println(text);
    }
}
