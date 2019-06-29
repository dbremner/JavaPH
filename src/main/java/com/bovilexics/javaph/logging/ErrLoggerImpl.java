package com.bovilexics.javaph.logging;

import org.jetbrains.annotations.NotNull;

public final class ErrLoggerImpl extends PrintStreamLogger implements ErrLogger
{
    public static final @NotNull ErrLogger instance = new ErrLoggerImpl();

    public ErrLoggerImpl()
    {
        super(System.err);
    }


    @Override
    public void printStackTrace(final @NotNull Exception exception)
    {
        exception.printStackTrace(printStream);
    }
}
