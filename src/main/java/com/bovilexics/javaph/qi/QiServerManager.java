package com.bovilexics.javaph.qi;

import com.bovilexics.javaph.JavaPHConstants;
import com.bovilexics.javaph.logging.ErrLogger;
import com.bovilexics.javaph.logging.ErrLoggerImpl;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

public final class QiServerManager implements ServerManager
{
    private static final @NotNull String SEPARATOR = "::";
    private static final @NotNull String SERVER_FILE = "javaph.servers";

    private static final @NotNull Splitter splitter = Splitter.on(SEPARATOR)
                                                              .omitEmptyStrings()
                                                              .trimResults();

    private static final @NotNull Joiner joiner = Joiner.on(SEPARATOR);
    private final @NotNull ErrLogger logger = ErrLoggerImpl.instance;

    private @Nullable Server defaultServer = null;
    private final @NotNull Vector<Server> servers = new Vector<>();
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
                servers.insertElementAt(server, i);
                return;
            }
        }

        servers.add(server);
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

    @SuppressWarnings({"UseOfObsoleteCollectionType", "AssignmentOrReturnOfFieldWithMutableType"})
    @Override
    public @NotNull Vector<Server> getServers() {
        return servers;
    }

    @Override
    public @NotNull ConnectionFactory getConnectionFactory()
    {
        return connectionFactory;
    }

    @Override
    public void loadAllServers(final @NotNull String filename) throws IOException, QiServerFileException
    {
        servers.clear();
        final @NotNull Path path = Paths.get(filename);
        final @NotNull List<String> lines = Files.readAllLines(path);
        for(int i = 0; i < lines.size(); i++)
        {
            final @NotNull String line = lines.get(i);
            // Ignore comment lines
            if (!line.startsWith("#"))
            {
                final @NotNull List<String> items = splitter.splitToList(line);

                if (items.size() != 3)
                {
                    throw new QiServerFileException(filename, i, line);
                }
                final @NotNull Server server = serverFactory.create(items.get(0), items.get(1), items.get(2));
                addServer(server);
            }
        }

        if (!servers.isEmpty())
        {
            defaultServer = servers.get(0);
        }
    }

    @Override
    public void loadAllServers()
    {
        final String filename = SERVER_FILE;
        try
        {
            loadAllServers(SERVER_FILE);
        }
        catch (final @NotNull QiServerFileException e)
        {
            logger.log(String.format(JavaPHConstants.ERROR_INVALID_SERVER_ENTRY_IN_S_ON_LINE_D_S, e.getFilename(), e.getLineNumber(), e.getContents()));
        }
        catch (final @NotNull InvalidPathException e)
        {
            logger.log("Error: InvalidPathException thrown when create file path");
            logger.printStackTrace(e);
        }
        catch (final @NotNull FileNotFoundException e)
        {
            logger.log(String.format(JavaPHConstants.ERROR_FILE_NOT_FOUND_EXCEPTION_RECEIVED_WHEN_TRYING_TO_READ_FILE_S, filename));
            logger.printStackTrace(e);
        }
        catch (final @NotNull IOException e)
        {
            logger.log(String.format(JavaPHConstants.ERROR_IOEXCEPTION_RECEIVED_WHEN_TRYING_TO_READ_FILE_S, filename));
            logger.printStackTrace(e);
        }
    }

    @Override
    public void removeServer(final @NotNull Server server)
    {
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
    public void saveServers() throws IOException
    {
        try(final @NotNull BufferedWriter writer = new BufferedWriter(new FileWriter(SERVER_FILE)))
        {
            writer.write(QiServerManager.getFileHeader());
            writer.flush();

            for (int i = 0; i < servers.size(); i++)
            {
                final @NotNull Server server = servers.get(i);

                final @NotNull StringBuilder toWrite = new StringBuilder();
                joiner.appendTo(toWrite, server.getName(), server.getServer(), server.getPort());
                if (i < servers.size() - 1)
                {
                    toWrite.append("\n");
                }

                writer.write(toWrite.toString());
                writer.flush();
            }
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
    public void setDefaultServer(final @NotNull Optional<String> server)
    {
        if (!server.isPresent())
        {
            return;
        }
        setDefaultServer(server.get());
    }

    @Override
    public void loadAllFields()
    {
        for (final @NotNull Server server : servers) {
            if (server.getFieldState() != FieldState.FIELD_LOAD_ERROR) {
                server.loadFields();
            }
        }
    }
}