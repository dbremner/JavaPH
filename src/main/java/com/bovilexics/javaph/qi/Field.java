package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Field
{
    @NotNull String getDescription();

    int getLength();

    @NotNull String getName();

    @NotNull List<String> getProperties();

    boolean hasProperty(@NotNull String property);
}
