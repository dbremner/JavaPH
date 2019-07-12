package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Vector;

public interface ServerManager
{
    void add(@NotNull Server server);

    @NotNull Server getDefaultServer();

    @SuppressWarnings("UseOfObsoleteCollectionType")
    @NotNull Vector<Server> getServers();

    void loadAllServers(@NotNull String filename) throws IOException, QiServerFileException;

    void loadAllServers() throws QiServerFileException;

    void remove(@NotNull Server server);

    void save() throws IOException;

    void setDefaultServer(@NotNull String server);
}
