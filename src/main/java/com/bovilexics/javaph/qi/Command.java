package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Command
{
    @Contract(pure = true)
    @NotNull String getCommand();

    @Contract(pure = true)
    @NotNull String getDescription();

    @Contract(pure = true)
    @NotNull String getName();

    @Contract(pure = true)
    boolean isListEditable();

    @Contract(pure = true)
    boolean isTextEditable();
}
