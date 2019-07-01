package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Line
{
    @Contract(pure = true)
    int getCode();

    @Contract(pure = true)
    int getIndex();

    @Contract(pure = true)
    @NotNull String getResponse();

    @Contract(pure = true)
    @NotNull String getField();

    @Contract(pure = true)
    @NotNull String getValue();

    @Contract(pure = true)
    @NotNull String getFieldValue();
}
