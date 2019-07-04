package com.bovilexics.javaph.qi;

import com.bovilexics.javaph.JavaPHConstants;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

final class NullConnectionHelper implements ConnectionHelper
{
    NullConnectionHelper()
    {
    }

    @Override
    public @Nullable String readLine() throws IOException
    {
        throw new IOException(JavaPHConstants.QI_SOCKET_UNINITIALIZED);
    }

    @Override
    public void writeLine(final @NotNull String string) throws IOException
    {
        throw new IOException(JavaPHConstants.QI_SOCKET_UNINITIALIZED);
    }

    @Override
    public boolean connected()
    {
        return false;
    }

    @Override
    public void close()
    {
    }

    @NonNls
    @Override
    public String toString()
    {
        return "NullConnectionHelper";
    }
}
