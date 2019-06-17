package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.NotNull;

public class QiConnectionFactory implements ConnectionFactory
{
    private final @NotNull ServerFactory serverFactory;

    QiConnectionFactory(final @NotNull ServerFactory serverFactory)
    {
        this.serverFactory = serverFactory;
    }

    @Override
    public @NotNull Connection create(final @NotNull String aHost, final int aPort)
    {
        final @NotNull Server server = serverFactory.create("unspecified", aHost, aPort);
        return new QiConnection(server);
    }

    @Override
    public @NotNull Connection create(final @NotNull Server server)
    {
        return new QiConnection(server);
    }
}
