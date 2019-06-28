package com.bovilexics.javaph.threads;

import com.bovilexics.javaph.JavaPH;
import com.bovilexics.javaph.logging.ErrLogger;
import com.bovilexics.javaph.qi.Connection;
import org.jetbrains.annotations.NotNull;

import javax.swing.SwingUtilities;

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
        {
            parent.beginQuery(command);
        });

        final @NotNull ResultThread resultThread = new ResultThread(null, command, connection);
        resultThread.start();

        while (!parent.isQueryCanceled() && seconds < runtime && !resultThread.isFinished())
        {
            try
            {
                Thread.sleep(1000);
            }
            catch(final @NotNull InterruptedException ie)
            {
                ErrLogger.instance.printStackTrace(ie);
            }

            ++seconds;
            SwingUtilities.invokeLater(() ->
            {
                parent.showStatus("Query running for " + seconds + " seconds");
                parent.setQueryProgress(seconds);
            });
        }

        // TODO add helper method to reduce boilerplate?
        if (parent.isQueryCanceled())
        {
            resultThread.interrupt();
            SwingUtilities.invokeLater(() ->
            {
                parent.endFailedQuery("Query Canceled", "Canceled");
            });
        }
        else if (seconds == runtime)
        {
            resultThread.interrupt();
            SwingUtilities.invokeLater(() ->
            {
                parent.endFailedQuery("Query Timed Out", "Timeout");
            });
        }
        else
        {
            final String rawResult = resultThread.getRawResult();
            final @NotNull Object[] headers = resultThread.getHeaders();
            final Object[][] values = resultThread.getValues();
            SwingUtilities.invokeLater(() ->
            {
                assert rawResult != null;
                assert values != null;
                parent.endQuery(rawResult, headers, values);
            });
        }
    }
}
