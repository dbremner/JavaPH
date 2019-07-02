package com.bovilexics.javaph.qi;

import com.bovilexics.javaph.JavaPHConstants;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.InvalidPathException;

public final class QiServerFileException extends Exception
{
    private final @NotNull String filename;
    private final int lineNumber;
    private final @NotNull String contents;
    private final @Nullable Exception exception;

    public QiServerFileException(final @NotNull String filename, final int lineNumber, final @NotNull String contents)
    {
        super(String.format(JavaPHConstants.ERROR_INVALID_SERVER_ENTRY_IN_S_ON_LINE_D_S, filename, lineNumber, contents));
        this.filename = filename;
        this.lineNumber = lineNumber;
        this.contents = contents;
        exception = null;
    }

    public QiServerFileException(final @NotNull InvalidPathException exception, final @NotNull String filename)
    {
        super(String.format(JavaPHConstants.ERROR_INVALID_PATH_EXCEPTION_THROWN_WHEN_CREATING_FILE_PATH_S, filename));
        this.exception = exception;
        this.filename = filename;
        lineNumber = -1;
        contents = "";
    }

    public QiServerFileException(final @NotNull FileNotFoundException exception, final @NotNull String filename)
    {
        super(String.format(JavaPHConstants.ERROR_FILE_NOT_FOUND_EXCEPTION_RECEIVED_WHEN_TRYING_TO_READ_FILE_S, filename));
        this.exception = exception;
        this.filename = filename;
        lineNumber = -1;
        contents = "";
    }

    public QiServerFileException(final @NotNull IOException exception, final @NotNull String filename)
    {
        super(String.format(JavaPHConstants.ERROR_IOEXCEPTION_RECEIVED_WHEN_TRYING_TO_READ_FILE_S, filename));
        this.exception = exception;
        this.filename = filename;
        lineNumber = -1;
        contents = "";
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
        return String.format("QiServerFileException{filename='%s', lineNumber=%d, contents='%s'}",
                filename,
                lineNumber,
                contents);
    }
}
