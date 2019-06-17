package com.bovilexics.javaph.qi;

import com.bovilexics.javaph.logging.ErrLogger;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class QiServerFileReader
{
    private static final String SEPARATOR = "::";
    private FieldFactory factory = null;

    public QiServerFileReader(final @NotNull FieldFactory factory)
    {
        this.factory = factory;
    }

    public @NotNull List<Server> readServers(final @NotNull String fileName) throws FileNotFoundException, IOException
    {
        final @NotNull Path path = Paths.get(fileName);
        final @NotNull List<String> lines = Files.readAllLines(path);
        final @NotNull List<Server> results = new ArrayList<>(lines.size());

        for (int i = 0; i < lines.size(); i++)
        {
            final @NotNull String line = lines.get(i);
            // Ignore comment lines
            if (line.startsWith("#")) {
                continue;
            }

            final @NotNull String[] items = line.split(SEPARATOR);

            if (items.length != 3)
            {
                ErrLogger.instance.println("Error: Invalid server entry in " + fileName + " on line " + i + " --> " + line);
            }
            else
                {
                final @NotNull Server server = new QiServer(factory, items[0], items[1], items[2]);
                results.add(server);
            }
        }
        return Collections.unmodifiableList(results);
    }
}
