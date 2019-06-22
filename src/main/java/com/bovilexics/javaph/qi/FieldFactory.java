package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface FieldFactory
{
    @NotNull Field create(@NotNull String name, @NotNull String someProperties, @NotNull String description) throws QiProtocolException;
}
