package com.bovilexics.javaph.logging;

import org.jetbrains.annotations.NotNull;

public interface Logger
{
    void printStackTrace(final @NotNull Exception exception);
    void println(final @NotNull String text);
}
