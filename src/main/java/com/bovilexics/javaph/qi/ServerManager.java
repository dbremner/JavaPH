package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Vector;

public interface ServerManager
{
    void addServer(@NotNull Server server);

    @NotNull Server getDefaultServer();

    @NotNull Vector<Server> getServers();

    void loadAllServers(@NotNull String filename);

    void loadAllServers();

    @NotNull List<Server> loadServers(@NotNull String filename);

    void removeServer(@NotNull Server server);

    void saveServers();

    void setDefaultServer(@NotNull String server);

    void loadAllFields();

    @NotNull ConnectionFactory getConnectionFactory();
}
