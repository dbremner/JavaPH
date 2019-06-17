package com.bovilexics.javaph;

import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
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

    @Override
    public String toString()
    {
        return properties.toString();
    }

    @Override
    public boolean equals(final Object o)
    {
        return properties.equals(o);
    }

    @Override
    public int hashCode()
    {
        return properties.hashCode();
    }

    public void setProperty(final @NotNull String key, final @NotNull String value)
    {
        properties.setProperty(key, value);
    }

    public void setProperty(final @NotNull String key, final int value)
    {
        properties.setProperty(key, String.valueOf(value));
    }

    public void setProperty(final @NotNull String key, final boolean value)
    {
        properties.setProperty(key, String.valueOf(value));
    }

    public void load(final @NotNull InputStream inStream) throws IOException
    {
        properties.load(inStream);
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

    public int getIntProperty(final @NotNull String key, final int defaultValue)
    {
        try
        {
            final @NotNull Optional<String> stringValue = getProperty(key);
            return stringValue.map(Integer::parseInt).orElse(defaultValue);
        }
        catch (final @NotNull NumberFormatException e)
        {
            return defaultValue;
        }
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
