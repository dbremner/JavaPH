package com.bovilexics.javaph.logging;

import org.jetbrains.annotations.NotNull;

public final class ErrLoggerImpl implements ErrLogger
{
    public static final @NotNull ErrLogger instance = new ErrLoggerImpl();

    @Override
    public void printStackTrace(final @NotNull Exception exception)
    {
        exception.printStackTrace(System.err);
    }

    @Override
    public void log(final @NotNull String text)
    {
        System.err.println(text);
    }
}
