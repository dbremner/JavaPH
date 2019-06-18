package com.bovilexics.javaph.qi;

import com.bovilexics.javaph.logging.ErrLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

public final class QiServerManager implements ServerManager
{
    private static final String SEPARATOR = "::";
    private static final String SERVER_FILE = "javaph.servers";
    private @Nullable Server defaultServer = null;
    private final Vector<Server> servers = new Vector<>();
    private final @NotNull ServerFactory serverFactory;
    private final @NotNull ConnectionFactory connectionFactory;

    public QiServerManager(final @NotNull ConnectionFactory connectionFactory, final @NotNull ServerFactory serverFactory)
    {
        this.connectionFactory = connectionFactory;
        this.serverFactory = serverFactory;
    }

    @Override
    public void addServer(final @NotNull Server server)
    {
        if (servers.isEmpty())
        {
            servers.add(server);
            return;
        }

        int whereToAdd = -1;
        final @NotNull String newElement = server.toString();

        for (int i = 0; i < servers.size(); i++)
        {
            final @NotNull String oldElement = servers.get(i).toString();

            final int compare = newElement.compareTo(oldElement);
            if (compare == 0)
            {
                return;
            }
            else if (compare < 0)
            {
                whereToAdd = i;
                servers.insertElementAt(server, whereToAdd);
                return;
            }
        }

        if (whereToAdd == -1)
        {
            servers.add(server);
        }
        else
        {
            servers.insertElementAt(server, whereToAdd);
        }
    }

    @Override
    public @NotNull Server getDefaultServer()
    {
        return Optional.ofNullable(defaultServer).orElse(getUndefinedServer());
    }

    private @NotNull Server getUndefinedServer()
    {
        final @NotNull String UNDEFINED = "undefined";
        final @NotNull Server undefined = serverFactory.create(UNDEFINED, UNDEFINED, "0");
        return undefined;
    }

    @Override
    public @NotNull Vector<Server> getServers() {
        return servers;
    }

    @Override
    public @NotNull ServerFactory getServerFactory()
    {
        return serverFactory;
    }

    @Override
    public @NotNull ConnectionFactory getConnectionFactory()
    {
        return connectionFactory;
    }

    @Override
    public void loadAllServers(final @NotNull String filename)
    {
        servers.clear();

        try(final @NotNull Reader in = new FileReader(filename);
            final @NotNull LineNumberReader lr = new LineNumberReader(in))
        {
            String line;

            while ((line = lr.readLine()) != null)
            {
                // Ignore comment lines
                if (line.startsWith("#"))
                {
                    continue;
                }

                final @NotNull String[] items = line.split(SEPARATOR);

                if (items.length != 3)
                {
                    ErrLogger.instance.println("Error: Invalid server entry in " + filename + " on line " + lr.getLineNumber() + " --> " + line);
                }
                else
                {
                    final @NotNull Server server1 = serverFactory.create(items[0], items[1], items[2]);
                    addServer(server1);
                }
            }

            defaultServer = servers.get(0);
        }
        catch (final @NotNull FileNotFoundException e)
        {
            ErrLogger.instance.println("Error: FileNotFoundException received when trying to read file " + filename);
            ErrLogger.instance.printStackTrace(e);
        }
        catch (final @NotNull IOException e)
        {
            ErrLogger.instance.println("Error: IOException received when trying to read file " + filename);
            ErrLogger.instance.printStackTrace(e);
        }
    }

    @Override
    public void loadAllServers() {
        loadAllServers(SERVER_FILE);
        /*
        TODO test this
        servers.clear();
        final @NotNull List<Server> results = loadServers(SERVER_FILE);
        for (final @NotNull Server server : results) {
            addServer(server);
        }
         */
    }

    @Override
    public @NotNull List<Server> loadServers(final @NotNull String filename) {
        final @NotNull List<Server> serverResults = new ArrayList<>();

        try(final @NotNull Reader in = new FileReader(filename);
            final @NotNull LineNumberReader lr = new LineNumberReader(in))
        {
            String line;

            while ((line = lr.readLine()) != null)
            {
                // Ignore comment lines
                if (line.startsWith("#"))
                {
                    continue;
                }

                final @NotNull String[] items = line.split(SEPARATOR);

                if (items.length != 3)
                {
                    ErrLogger.instance.println("Error: Invalid server entry in " + filename + " on line " + lr.getLineNumber() + " --> " + line);
                }
                else
                {
                    final @NotNull Server server1 = serverFactory.create(items[0], items[1], items[2]);
                    serverResults.add(server1);
                }
            }
        }
        catch (final @NotNull FileNotFoundException e)
        {
            ErrLogger.instance.println("Error: FileNotFoundException received when trying to read file " + filename);
            ErrLogger.instance.printStackTrace(e);
        }
        catch (final @NotNull IOException e)
        {
            ErrLogger.instance.println("Error: IOException received when trying to read file " + filename);
            ErrLogger.instance.printStackTrace(e);
        }
        return serverResults;
    }

    @Override
    public void removeServer(final Server server) {
        servers.remove(server);
    }

    private static @NotNull String getFileHeader()
    {
        final @NotNull StringBuilder out = new StringBuilder();

        out.append("# JavaPH Server File\n");
        out.append("# \n");
        out.append("# Manually add servers to this list using the following format\n");
        out.append("#   <name / description> :: <host / ip address> :: <port (0-65535)>\n");
        out.append("# \n");

        return out.toString();
    }

    @Override
    public void saveServers()
    {
        try(final @NotNull BufferedWriter writer = new BufferedWriter(new FileWriter(SERVER_FILE)))
        {
            writer.write(QiServerManager.getFileHeader());
            writer.flush();

            for (int i = 0; i < servers.size(); i++)
            {
                final Server server = servers.get(i);

                final @NotNull StringBuilder toWrite = new StringBuilder();
                toWrite.append(server.getName());
                toWrite.append(SEPARATOR);
                toWrite.append(server.getServer());
                toWrite.append(SEPARATOR);
                toWrite.append(server.getPort());

                if (i < servers.size() - 1)
                {
                    toWrite.append("\n");
                }

                writer.write(toWrite.toString());
                writer.flush();
            }
        }
        catch (final @NotNull IOException e)
        {
            ErrLogger.instance.println("Error: IOException received when trying to write file " + SERVER_FILE);
            ErrLogger.instance.printStackTrace(e);
        }
    }

    @Override
    public void setDefaultServer(final @NotNull String server)
    {
        for (final @NotNull Server item : servers) {
            if (item.toString().equals(server)) {
                defaultServer = item;
                return;
            }
        }
    }

    @Override
    public void loadAllFields()
    {
        for (final @NotNull Server server : servers) {
            if (server.getFieldState() != QiFieldState.FIELD_LOAD_ERROR) {
                server.loadFields();
            }
        }
    }
}