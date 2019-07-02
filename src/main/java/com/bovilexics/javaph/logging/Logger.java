package com.bovilexics.javaph.logging;

import org.jetbrains.annotations.NotNull;

public interface Logger
{
    @Deprecated
    void log(final @NotNull String text);
}
