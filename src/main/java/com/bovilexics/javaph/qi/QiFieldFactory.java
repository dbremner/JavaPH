package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.NotNull;

public class QiFieldFactory implements FieldFactory
{
    @NotNull
    @Override
    public Field create(@NotNull String name, @NotNull String someProperties, @NotNull String description) throws QiProtocolException
    {
        return new QiField(name, someProperties, description);
    }
}
