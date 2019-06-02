package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.NotNull;

public interface FieldFactory
{
    Field create(@NotNull String name, @NotNull String someProperties, @NotNull String description) throws QiProtocolException;
}
