package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.IOException;

interface ConnectionHelper extends Closeable
{
    @Nullable String readLine() throws IOException;
    void writeLine(final @NotNull String string) throws IOException;
    @Contract(pure = true)
    boolean connected();
}
