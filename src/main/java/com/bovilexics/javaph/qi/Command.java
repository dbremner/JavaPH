package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.NotNull;

public interface Command
{
    @NotNull String getCommand();

    @NotNull String getDescription();

    @NotNull String getName();

    boolean isListEditable();

    boolean isTextEditable();
}
