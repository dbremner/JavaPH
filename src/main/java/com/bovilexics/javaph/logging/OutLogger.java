package com.bovilexics.javaph.logging;

import org.jetbrains.annotations.NotNull;

public final class OutLogger implements Logger
{
    @Override
    public void log(final @NotNull String text)
    {
        System.out.println(text);
    }
}
