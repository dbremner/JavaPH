package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Field
{
    @Contract(pure = true)
    @NotNull String getDescription();

    @Contract(pure = true)
    int getLength();

    @Contract(pure = true)
    @NotNull String getName();

    @Contract(pure = true)
    @NotNull List<String> getProperties();

    @Contract(pure = true)
    boolean hasProperty(@NotNull String property);
}
