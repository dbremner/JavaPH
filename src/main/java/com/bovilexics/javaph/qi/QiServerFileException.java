package com.bovilexics.javaph.qi;

import com.bovilexics.javaph.JavaPHConstants;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class QiServerFileException extends Exception
{
    private final @NotNull String filename;
    private final int lineNumber;
    private final @NotNull String contents;

    public QiServerFileException(final @NotNull String filename, final int lineNumber, final @NotNull String contents)
    {
        super(String.format(JavaPHConstants.ERROR_INVALID_SERVER_ENTRY_IN_S_ON_LINE_D_S, filename, lineNumber, contents));
        this.filename = filename;
        this.lineNumber = lineNumber;
        this.contents = contents;
    }

    @Contract(pure = true)
    public @NotNull String getFilename()
    {
        return filename;
    }

    @Contract(pure = true)
    public int getLineNumber()
    {
        return lineNumber;
    }

    @Contract(pure = true)
    public @NotNull String getContents()
    {
        return contents;
    }

    @Override
    public @NotNull String toString()
    {
        return String.format("QiServerFileException{filename='%s', lineNumber=%d, contents='%s'}", filename, lineNumber, contents);
    }
}
