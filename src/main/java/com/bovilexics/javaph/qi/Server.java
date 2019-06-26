package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Server
{
    @Contract(pure = true)
    @NotNull String getExpandedName();

    @Contract(pure = true)
    @NotNull List<Field> getFields();

    @Contract(pure = true)
    @NotNull FieldState getFieldState();

    @NotNull String getFieldStateMessage();

    @Contract(pure = true)
    @NotNull String getName();

    @Contract(pure = true)
    int getPort();

    @Contract(pure = true)
    @NotNull String getServer();

    void loadFields();
}
