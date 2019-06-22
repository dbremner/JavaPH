package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Vector;

public interface ServerManager
{
    void addServer(@NotNull Server server);

    @NotNull Server getDefaultServer();

    @SuppressWarnings("UseOfObsoleteCollectionType")
    @NotNull Vector<Server> getServers();

    void loadAllServers(@NotNull String filename);

    void loadAllServers();

    void removeServer(@NotNull Server server);

    void saveServers();

    void setDefaultServer(@NotNull String server);

    void setDefaultServer(@NotNull Optional<String> server);

    void loadAllFields();

    @NotNull ConnectionFactory getConnectionFactory();
}
