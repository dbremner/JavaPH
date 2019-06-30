package com.bovilexics.javaph.logging;

import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;

public class PrintStreamLogger implements Logger
{
    @SuppressWarnings("WeakerAccess")
    protected final @NotNull PrintStream printStream;

    PrintStreamLogger(final @NotNull PrintStream printStream)
    {
        this.printStream = printStream;
    }

    @Override
    public final void log(final @NotNull String text)
    {
        printStream.println(text);
    }
}
