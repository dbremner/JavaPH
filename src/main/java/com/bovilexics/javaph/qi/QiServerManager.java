package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public final class QiServerManager {
    private static final String SEPARATOR = "::";
    private static final String SERVER_FILE = "javaph.servers";
    private static QiServer defaultServer;
    private static final Vector<QiServer> servers = new Vector<>();

    public static void addServer(@NotNull QiServer server) {

        if (servers.isEmpty())
        {
            servers.add(server);
            return;
        }

        int whereToAdd = -1;
        final @NotNull String newElement = server.toString();

        for (int i = 0; i < servers.size(); i++) {
            final @NotNull String oldElement = servers.get(i).toString();

            if (newElement.compareTo(oldElement) == 0) {
                return;
            } else if (newElement.compareTo(oldElement) < 0) {
                whereToAdd = i;
                break;
            }
        }

        if (whereToAdd == -1) {
            whereToAdd = servers.size();
        }
        servers.insertElementAt(server, whereToAdd);
    }

    public static @NotNull QiServer getDefaultServer()
    {
        if (defaultServer == null)
        {
            return QiServerManager.getUndefinedServer();
        }

        return defaultServer;
    }

    private static @NotNull QiServer getUndefinedServer()
    {
        final @NotNull String UNDEFINED = "undefined";
        final @NotNull QiServer undefined = new QiServer(UNDEFINED, UNDEFINED, "0");
        return undefined;
    }

    public static @NotNull Vector<QiServer> getServers() {
        return servers;
    }

    public static void loadAllServers(@NotNull String filename) {
        servers.clear();

        try {
            final @NotNull Reader in = new FileReader(filename);

            final @NotNull LineNumberReader lr = new LineNumberReader(in);

            String line;

            while ((line = lr.readLine()) != null) {
                // Ignore comment lines
                if (line.startsWith("#")) {
                    continue;
                }

                final @NotNull String[] items = line.split(SEPARATOR);

                if (items.length != 3) {
                    System.err.println("Error: Invalid server entry in " + filename + " on line " + lr.getLineNumber() + " --> " + line);
                } else {
                    final @NotNull QiServer server1 = new QiServer(items[0], items[1], items[2]);
                    addServer(server1);
                }
            }

            defaultServer = servers.get(0);
        } catch (FileNotFoundException e) {
            System.err.println("Error: FileNotFoundException received when trying to read file " + filename);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error: IOException received when trying to read file " + filename);
            e.printStackTrace();
        }
    }

    public static void loadAllServers() {
        servers.clear();
        final @NotNull List<QiServer> results = loadServers(SERVER_FILE);
        for (final @NotNull QiServer server : results) {
            addServer(server);
        }
    }

    public static @NotNull List<QiServer> loadServers(@NotNull String filename) {
        final @NotNull List<QiServer> serverResults = new ArrayList<>();

        try {
            final @NotNull Reader in = new FileReader(filename);

            final @NotNull LineNumberReader lr = new LineNumberReader(in);

            String line;

            while ((line = lr.readLine()) != null) {
                // Ignore comment lines
                if (line.startsWith("#")) {
                    continue;
                }

                final @NotNull String[] items = line.split(SEPARATOR);

                if (items.length != 3) {
                    System.err.println("Error: Invalid server entry in " + filename + " on line " + lr.getLineNumber() + " --> " + line);
                } else {
                    final @NotNull QiServer server1 = new QiServer(items[0], items[1], items[2]);
                    serverResults.add(server1);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: FileNotFoundException received when trying to read file " + filename);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error: IOException received when trying to read file " + filename);
            e.printStackTrace();
        }
        return serverResults;
    }

    public static void removeServer(QiServer server) {
        servers.remove(server);
    }

    public static void saveServers() {
        try {
            final @NotNull BufferedWriter writer = new BufferedWriter(new FileWriter(SERVER_FILE));

            writer.write(getFileHeader());
            writer.flush();

            for (int i = 0; i < servers.size(); i++) {
                final QiServer server = servers.get(i);

                final @NotNull StringBuilder toWrite = new StringBuilder();
                toWrite.append(server.getName());
                toWrite.append(SEPARATOR);
                toWrite.append(server.getServer());
                toWrite.append(SEPARATOR);
                toWrite.append(server.getPort());

                if (i < servers.size() - 1) {
                    toWrite.append("\n");
                }

                writer.write(toWrite.toString());
                writer.flush();
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error: IOException received when trying to write file " + SERVER_FILE);
            e.printStackTrace();
        }
    }

    public static void setDefaultServer(@NotNull String server)
    {
        for (final @NotNull QiServer item : servers) {
            if (item.toString().equals(server)) {
                defaultServer = item;
                return;
            }
        }
    }

    public static @NotNull String getFileHeader()
    {
        final @NotNull StringBuilder out = new StringBuilder();

        out.append("# JavaPH Server File\n");
        out.append("# \n");
        out.append("# Manually add servers to this list using the following format\n");
        out.append("#   <name / description> :: <host / ip address> :: <port (0-65535)>\n");
        out.append("# \n");

        return out.toString();
    }

    public static void loadAllFields()
    {
        for (final @NotNull QiServer server : servers) {
            if (server.getFieldState() != QiFieldState.FIELD_LOAD_ERROR) {
                server.loadFields();
            }
        }
    }
}