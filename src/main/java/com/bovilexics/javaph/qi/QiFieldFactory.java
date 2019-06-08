package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.NotNull;

public class QiFieldFactory implements FieldFactory
{
    @Override
    public @NotNull Field create(final @NotNull String name, final @NotNull String someProperties, final @NotNull String description) throws QiProtocolException
    {
        return new QiField(name, someProperties, description);
    }
}
