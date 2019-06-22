package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.NotNull;

public final class QiServerFileException extends Exception
{
    private final @NotNull String filename;
    private final int lineNumber;
    private final @NotNull String contents;

    public QiServerFileException(final @NotNull String filename, final int lineNumber, final @NotNull String contents)
    {
        super("Error: Invalid server entry in " + filename + " on line " + lineNumber + " --> " + contents);
        this.filename = filename;
        this.lineNumber = lineNumber;
        this.contents = contents;
    }

    public @NotNull String getFilename()
    {
        return filename;
    }

    public int getLineNumber()
    {
        return lineNumber;
    }

    public @NotNull String getContents()
    {
        return contents;
    }
}
