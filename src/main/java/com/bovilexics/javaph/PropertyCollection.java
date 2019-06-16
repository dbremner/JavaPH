package com.bovilexics.javaph;

import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

public final class PropertyCollection
{
    private final @NotNull Properties properties = new Properties();

    public int size()
    {
        return properties.size();
    }

    public boolean isEmpty()
    {
        return properties.isEmpty();
    }

    public String toString()
    {
        return properties.toString();
    }

    public boolean equals(final Object o)
    {
        return properties.equals(o);
    }

    public int hashCode()
    {
        return properties.hashCode();
    }

    public void setProperty(final @NotNull String key, final @NotNull String value)
    {
        properties.setProperty(key, value);
    }

    public void load(final @NotNull Reader reader) throws IOException
    {
        properties.load(reader);
    }

    public void load(final @NotNull InputStream inStream) throws IOException
    {
        properties.load(inStream);
    }

    public void save(final @NotNull OutputStream out, final @NotNull String comments)
    {
        properties.save(out, comments);
    }

    public void store(final @NotNull Writer writer, final @NotNull String comments) throws IOException
    {
        properties.store(writer, comments);
    }

    public void store(final @NotNull OutputStream out, final @NotNull String comments) throws IOException
    {
        properties.store(out, comments);
    }

    public @NotNull Optional<String> getProperty(final @NotNull String key)
    {
        return Optional.ofNullable(properties.getProperty(key));
    }

    public @NotNull String getProperty(final @NotNull String key, final @NotNull String defaultValue)
    {
        return properties.getProperty(key, defaultValue);
    }

    public @NotNull Set<String> stringPropertyNames()
    {
        return ImmutableSet.copyOf(properties.stringPropertyNames());
    }

    public void list(final @NotNull PrintStream out)
    {
        properties.list(out);
    }

    public void list(final @NotNull PrintWriter out)
    {
        properties.list(out);
    }
}
