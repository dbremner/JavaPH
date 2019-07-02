package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Optional;
import java.util.Vector;

public interface ServerManager
{
    void addServer(@NotNull Server server);

    @NotNull Server getDefaultServer();

    @SuppressWarnings("UseOfObsoleteCollectionType")
    @NotNull Vector<Server> getServers();

    void loadAllServers(@NotNull String filename) throws IOException, QiServerFileException;

    void loadAllServers();

    void removeServer(@NotNull Server server);

    void saveServers() throws IOException;

    void setDefaultServer(@NotNull String server);

    void setDefaultServer(@NotNull Optional<String> server);

    void loadAllFields();

    @Contract(pure = true)
    @NotNull ConnectionFactory getConnectionFactory();
}
