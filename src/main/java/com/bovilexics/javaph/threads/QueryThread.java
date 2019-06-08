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
import com.bovilexics.javaph.models.ResultTableModel;
import com.bovilexics.javaph.models.TableSorter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JOptionPane;
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
	private @Nullable ResultThread resultThread = null;

	public QueryThread(final @NotNull JavaPH javaph)
	{
		parent = javaph;
	}

	private void closeProgress()
	{
		SwingUtilities.invokeLater(() -> parent.getQueryProgressMonitor().close());
	}

	@Override
	public void run()
	{
		final int runtime = parent.getQueryRuntime();
		
		startup();

		parent.log("Running query \"" + parent.getCommand() + "\"");

		resultThread = new ResultThread(parent.getCommand(), parent.getServer());
		resultThread.start();

		while (!parent.getQueryProgressMonitor().isCanceled() && seconds < runtime && !resultThread.isFinished())
		{
			try
			{
				Thread.sleep(1000);
			}
			catch(final InterruptedException ie)
			{
				ie.printStackTrace();
			}

			++seconds;
			showStatus("Query running for " + seconds + " seconds");
			updateProgress(seconds);
		}
			
		if (parent.getQueryProgressMonitor().isCanceled())
		{
			resultThread.interrupt();
			closeProgress();
			showStatus("Query Canceled", true);
			JOptionPane.showMessageDialog(parent.getDefaultPane(), "Query Canceled", "Canceled", JOptionPane.ERROR_MESSAGE);
		}
		else if (seconds == runtime)
		{
			resultThread.interrupt();
			closeProgress();
			showStatus("Query Timed Out", true);
			JOptionPane.showMessageDialog(parent.getDefaultPane(), "Query Timed Out", "Timeout", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			showStatus("Query Finished", true);
			showResult();
		}

		shutdown();
	}

	private void showResult()
	{
		SwingUtilities.invokeLater(() ->
		{
			parent.getQueryProgressMonitor().close();
			parent.getResultText().setText(resultThread.getRawResult());

			final @NotNull ResultTableModel resultModel = (ResultTableModel)((TableSorter)parent.getResultTable().getModel()).getModel();
			resultModel.setDataVector(resultThread.getValues(), resultThread.getHeaders());
			parent.getResultTable().resetColumnWidths();
		});
	}

	private void showStatus(final String status)
	{
		showStatus(status, false);
	}

	private void showStatus(final String status, final boolean logAlso)
	{
		SwingUtilities.invokeLater(() ->
		{
			parent.showStatus(status);

			if (logAlso) {
				parent.log(status);
			}
		});
	}

	private void shutdown()
	{
		SwingUtilities.invokeLater(() -> parent.getQueryButton().setEnabled(true));
	}

	private void startup()
	{
		SwingUtilities.invokeLater(() -> parent.getQueryButton().setEnabled(false));
		
		showStatus("Query Running... Please Wait");
	}

	private void updateProgress(final int progress)
	{
		SwingUtilities.invokeLater(() -> parent.getQueryProgressMonitor().setProgress(progress));
	}
}