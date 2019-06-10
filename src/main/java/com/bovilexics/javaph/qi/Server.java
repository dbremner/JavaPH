package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Server
{
    @NotNull String getExpandedName();

    @NotNull List<Field> getFields();

    @NotNull QiFieldState getFieldState();

    @NotNull String getFieldStateMessage();

    @NotNull String getName();

    @NotNull Integer getPort();

    @NotNull String getServer();

    void loadFields();
}
