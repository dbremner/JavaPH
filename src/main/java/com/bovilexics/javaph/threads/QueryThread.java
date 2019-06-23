/*
 * Copyright (C) 2003  Robert Fernandes  robert@bovilexics.com
 * http://www.bovilexics.com/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * See COPYING.TXT for details.
 */
package com.bovilexics.javaph.threads;

import com.bovilexics.javaph.JavaPH;
import com.bovilexics.javaph.logging.ErrLogger;
import com.bovilexics.javaph.models.ResultTableModel;
import com.bovilexics.javaph.qi.Connection;
import com.bovilexics.javaph.qi.Server;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.SwingUtilities;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public class QueryThread extends Thread
{
	private int seconds = 0;

	private final @NotNull JavaPH parent;
	private final @NotNull String command;
	private @Nullable ResultThread resultThread = null;

	public QueryThread(final @NotNull JavaPH javaph)
	{
		parent = javaph;
		command = parent.getCommand();
	}

	private void closeProgress()
	{
		SwingUtilities.invokeLater(parent::closeQueryProgressMonitor);
	}

	@Override
	public void run()
	{
		final int runtime = parent.getQueryRuntime();
		
		startup();

		parent.log("Running query \"" + command + "\"");


		final @Nullable Server server = parent.getServer();
		assert server != null;
		final @NotNull Connection connection = parent.getServerManager().getConnectionFactory().create(server);
		resultThread = new ResultThread(null, command, connection);
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
			showStatus("Query running for " + seconds + " seconds");
			updateProgress(seconds);
		}
			
		if (parent.isQueryCanceled())
		{
			resultThread.interrupt();
			closeProgress();
			showStatusLog("Query Canceled");
			parent.showErrorDialog("Query Canceled", "Canceled");
		}
		else if (seconds == runtime)
		{
			resultThread.interrupt();
			closeProgress();
			showStatusLog("Query Timed Out");
			parent.showErrorDialog("Query Timed Out", "Timeout");
		}
		else
		{
			showStatusLog("Query Finished");
			showResult();
		}

		shutdown();
	}

	private void showResult()
	{
		SwingUtilities.invokeLater(() ->
		{
			parent.closeQueryProgressMonitor();
			assert resultThread != null;
			parent.getResultText().setText(resultThread.getRawResult());

			final @NotNull ResultTableModel resultModel = parent.getResultTable().getTableSorter().getModel();
			resultModel.setDataVector(resultThread.getValues(), resultThread.getHeaders());
			parent.getResultTable().resetColumnWidths();
		});
	}

	private void showStatus(final @NotNull String status)
	{
		SwingUtilities.invokeLater(() -> parent.showStatus(status));
	}

	private void showStatusLog(final @NotNull String status)
	{
		SwingUtilities.invokeLater(() -> parent.showStatusLog(status));
	}

	private void shutdown()
	{
		SwingUtilities.invokeLater(parent::enableQueryButton);
	}

	private void startup()
	{
		SwingUtilities.invokeLater(() ->
		{
			parent.disableQueryButton();
			parent.showStatus("Query Running... Please Wait");
		});
	}

	private void updateProgress(final int progress)
	{
		SwingUtilities.invokeLater(() -> parent.setQueryProgress(progress));
	}
}