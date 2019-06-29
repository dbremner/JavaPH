package com.bovilexics.javaph.logging;

import org.jetbrains.annotations.NotNull;

public final class OutLogger implements ErrLogger
{
    public static final @NotNull ErrLogger instance = new OutLogger();


    @Override
    public void printStackTrace(final @NotNull Exception exception)
    {
        exception.printStackTrace(System.out);
    }

    @Override
    public void log(final @NotNull String text)
    {
        System.out.println(text);
    }
}
