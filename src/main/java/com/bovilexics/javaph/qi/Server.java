package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Server
{
    @NotNull String getExpandedName();

    @NotNull List<Field> getFields();

    @NotNull FieldState getFieldState();

    @NotNull String getFieldStateMessage();

    @NotNull String getName();

    int getPort();

    @NotNull String getServer();

    void loadFields();
}
