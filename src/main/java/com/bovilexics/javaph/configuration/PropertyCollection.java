package com.bovilexics.javaph.configuration;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

@SuppressWarnings("RedundantThrows")
public interface PropertyCollection
{
    int size();

    boolean isEmpty();

    void setProperty(@NotNull String key, @NotNull String value);

    void setProperty(@NotNull String key, int value);

    @SuppressWarnings("BooleanParameter")
    void setProperty(@NotNull String key, boolean value);

    void load(@NotNull InputStream inStream) throws IOException;

    void store(@NotNull OutputStream out, @NotNull String comments) throws IOException;

    @NotNull Optional<String> getProperty(@NotNull String key);

    @NotNull String getProperty(@NotNull String key, @NotNull String defaultValue);

    int getProperty(@NotNull String key, int defaultValue);
}
