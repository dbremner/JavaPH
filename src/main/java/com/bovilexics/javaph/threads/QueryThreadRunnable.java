package com.bovilexics.javaph.threads;

import com.bovilexics.javaph.JavaPH;
import com.bovilexics.javaph.JavaPHConstants;
import com.bovilexics.javaph.logging.ErrLoggerImpl;
import com.bovilexics.javaph.logging.StatusErrorLogger;
import com.bovilexics.javaph.qi.Connection;
import com.bovilexics.javaph.qi.LineFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.SwingUtilities;
import java.io.IOException;
import java.util.Collection;

public final class QueryThreadRunnable implements Runnable
{
    private final @NotNull Connection connection;
    private final @NotNull LineFactory lineFactory;
    private int seconds = 0;

    private final @NotNull JavaPH parent;
    private final @NotNull String command;
    private final int runtime;

    public QueryThreadRunnable(
            final @NotNull JavaPH parent,
            final int runtime,
            final @NotNull String command,
            final @NotNull Connection connection,
            final @NotNull LineFactory lineFactory)
    {
        this.parent = parent;
        this.runtime = runtime;
        this.command = command;
        this.connection = connection;
        this.lineFactory = lineFactory;
    }

    @Override
    public void run()
    {
        SwingUtilities.invokeLater(() ->
                parent.beginQuery(command));

        final @NotNull ResultThread resultThread;
        try
        {
            resultThread = new ResultThread(new StatusErrorLogger(), command, connection, lineFactory);
        }
        catch (final @NotNull IOException e)
        {
            SwingUtilities.invokeLater(() -> parent.endFailedQuery(JavaPHConstants.QUERY_CONNECTION_FAILED, JavaPHConstants.CONNECTION_FAILED));
            return;
        }
        resultThread.start();

        while (!parent.isQueryCanceled() && seconds < runtime && resultThread.isAlive())
        {
            try
            {
                Thread.sleep(1000);
            }
            catch(final @NotNull InterruptedException ie)
            {
                ErrLoggerImpl.instance.printStackTrace(ie);
            }

            ++seconds;
            SwingUtilities.invokeLater(() ->
            {
                parent.showStatus(String.format(JavaPHConstants.QUERY_RUNNING_FOR_D_SECONDS, seconds));
                parent.setQueryProgress(seconds);
            });
        }

        if (parent.isQueryCanceled())
        {
            failQuery(resultThread, JavaPHConstants.QUERY_CANCELED, JavaPHConstants.CANCELED);
        }
        else if (seconds == runtime)
        {
            failQuery(resultThread, JavaPHConstants.QUERY_TIMED_OUT, JavaPHConstants.TIMEOUT);
        }
        else
        {
            final @NotNull ResultThreadResult result = resultThread.getResult();
            final @NotNull String rawResult = result.getRawResult();
            final @NotNull Collection<Object> headers = result.getHeaders();
            final @NotNull Object[][] values = result.getValues();
            SwingUtilities.invokeLater(() ->
                    parent.endQuery(rawResult, headers, values));
        }
    }

    private void failQuery(final @NotNull ResultThread resultThread, final @NotNull String message, final @NotNull String title)
    {
        resultThread.interrupt();
        SwingUtilities.invokeLater(() -> parent.endFailedQuery(message, title));
    }
}
