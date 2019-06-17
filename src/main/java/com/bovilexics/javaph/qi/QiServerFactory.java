package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.NotNull;

public final class QiServerFactory implements ServerFactory
{
    private final @NotNull FieldFactory fieldFactory;

    public QiServerFactory(final @NotNull FieldFactory fieldFactory)
    {
        this.fieldFactory = fieldFactory;
    }

    @Override
    public @NotNull Server create(final @NotNull String name, final @NotNull String server, final int port)
    {
        return new QiServer(fieldFactory, name, server, port);
    }

    @Override
    public @NotNull Server create(final @NotNull String name, final @NotNull String server, final @NotNull String port)
    {
        return new QiServer(fieldFactory, name, server, port);
    }
}
