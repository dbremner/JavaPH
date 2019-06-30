package com.bovilexics.javaph.threads;

import com.bovilexics.javaph.JavaPH;
import com.bovilexics.javaph.JavaPHConstants;
import com.bovilexics.javaph.logging.ErrLoggerImpl;
import com.bovilexics.javaph.logging.StatusErrorLogger;
import com.bovilexics.javaph.qi.Connection;
import org.jetbrains.annotations.NotNull;

import javax.swing.SwingUtilities;
import java.util.Collection;

public final class QueryThreadRunnable implements Runnable
{
    private final @NotNull Connection connection;
    private int seconds = 0;

    private final @NotNull JavaPH parent;
    private final @NotNull String command;
    private final int runtime;

    public QueryThreadRunnable(
            final @NotNull JavaPH parent,
            final int runtime,
            final @NotNull String command,
            final @NotNull Connection connection)
    {
        this.parent = parent;
        this.runtime = runtime;
        this.command = command;
        this.connection = connection;
    }

    @Override
    public void run()
    {
        SwingUtilities.invokeLater(() ->
                parent.beginQuery(command));

        final @NotNull ResultThread resultThread = new ResultThread(new StatusErrorLogger(), command, connection);
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

        // TODO add helper method to reduce boilerplate?
        if (parent.isQueryCanceled())
        {
            resultThread.interrupt();
            SwingUtilities.invokeLater(() ->
                    parent.endFailedQuery(JavaPHConstants.QUERY_CANCELED, JavaPHConstants.CANCELED));
        }
        else if (seconds == runtime)
        {
            resultThread.interrupt();
            SwingUtilities.invokeLater(() ->
                    parent.endFailedQuery(JavaPHConstants.QUERY_TIMED_OUT, JavaPHConstants.TIMEOUT));
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
}
