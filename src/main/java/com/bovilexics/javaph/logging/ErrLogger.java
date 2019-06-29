package com.bovilexics.javaph.logging;

import org.jetbrains.annotations.NotNull;

public interface ErrLogger extends Logger
{
    void printStackTrace(final @NotNull Exception exception);
}
