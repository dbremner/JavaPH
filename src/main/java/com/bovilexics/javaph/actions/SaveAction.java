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
package com.bovilexics.javaph.actions;

import com.bovilexics.javaph.JavaPH;
import com.bovilexics.javaph.ui.CsvFileChooser;
import com.bovilexics.javaph.ui.TextFileChooser;
import org.jetbrains.annotations.NotNull;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.bovilexics.javaph.JavaPHConstants.FIELD_QUOTE;
import static com.bovilexics.javaph.JavaPHConstants.RESULT_TABLE_TAB;
import static com.bovilexics.javaph.JavaPHConstants.RESULT_TEXT_TAB;
import static com.bovilexics.javaph.JavaPHConstants.SYSTEM_LOG_TAB;
import static com.bovilexics.javaph.JavaPHConstants.TAB_FILENAMES;
import static com.bovilexics.javaph.JavaPHConstants.TAB_LABELS;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public final class SaveAction extends AbstractAction
{
	private final @NotNull JavaPH parent;
		
	public SaveAction(final @NotNull JavaPH javaph)
	{
		super("Save Results", javaph.getImageIcon("img/save.gif"));

		parent = javaph;
	}
	
	@Override
	public void actionPerformed(final ActionEvent ae)
	{
		final int selectedTab = parent.getResultPanel().getSelectedIndex();

		final @NotNull JFileChooser chooser;
		
		if (selectedTab == RESULT_TABLE_TAB) {
            chooser = new CsvFileChooser(parent);
        } else {
            chooser = new TextFileChooser();
        }

		chooser.setDialogTitle("Save " + TAB_LABELS[selectedTab]);
		chooser.setSelectedFile(new File(TAB_FILENAMES[selectedTab]));

		final int state = chooser.showSaveDialog(parent.getDefaultPane());
		final File file = chooser.getSelectedFile();

		if (state == JFileChooser.APPROVE_OPTION && file != null)
		{
			parent.log("Saving file " + file.getPath());
			
			if (selectedTab == RESULT_TABLE_TAB) {
                saveCsvFile(selectedTab, file);
            } else {
                saveTextFile(selectedTab, file);
            }
		}
	}
	
	private void saveCsvFile(final int tab, final @NotNull File file)
	{
		final boolean quoted = parent.isFieldQuoted();
	
		final @NotNull String separator = parent.getFieldSeparator();
		final TableModel model = parent.getResultTable().getModel();

		final int cols = model.getColumnCount();
		final int rows = model.getRowCount();

		if (rows <= 0)
		{
			final @NotNull String message = "Nothing to save in " + TAB_LABELS[tab] + " tab";
			parent.log(message);
			parent.showWarningDialog(message, "Finished");
		}
		else
		{
			try
			{
				final @NotNull BufferedWriter writer = new BufferedWriter(new FileWriter(file));

				// start at row -1 to grab headers first
				for (int r = -1; r < rows; r++)
				{
					final @NotNull StringBuffer toWrite = new StringBuffer();

					for (int c = 0; c < cols; c++)
					{
						if (c > 0) {
                            toWrite.append(separator);
                        }

						if (quoted) {
                            toWrite.append(FIELD_QUOTE);
                        }
						
						if (r == -1) // write the header
                        {
                            toWrite.append(model.getColumnName(c) == null ? "" : model.getColumnName(c));
                        } else // write the value
                        {
                            toWrite.append(model.getValueAt(r, c) == null ? "" : model.getValueAt(r, c).toString());
                        }

						if (quoted) {
                            toWrite.append(FIELD_QUOTE);
                        }

					}
					toWrite.append("\n");
				
					writer.write(toWrite.toString());
					writer.flush();
				}
				writer.close();

				final @NotNull String message = "File save finished";
				parent.log(message);
				parent.showInformationDialog(message, "Finished");
			}
			catch (final IOException e)
			{
				final @NotNull String message = "Error: IOException received when trying to save file " + file.getPath();
				parent.log(message);
				parent.showErrorDialog(message, "Exception");
			}
		}	
	}
	
	private void saveTextFile(final int tab, final @NotNull File file)
	{
		final @NotNull String toWrite;
		
		if (tab == RESULT_TEXT_TAB)
		{
			toWrite = parent.getResultText().getText();
		}
		else if (tab == SYSTEM_LOG_TAB)
		{
			toWrite = parent.getLogText().getText();
		}
		else
		{
			toWrite = "";
		}

		if (toWrite.isEmpty())
		{
			final @NotNull String message = "Nothing to save in " + TAB_LABELS[tab] + " tab";
			parent.log(message);
			parent.showWarningDialog(message, "Finished");
			return;
		}

		try (@NotNull BufferedWriter writer = new BufferedWriter(new FileWriter(file)))
		{
			writer.write(toWrite);
			writer.flush();
			final @NotNull String message = "File save finished";
			parent.log(message);
			parent.showInformationDialog(message, "Finished");
		}
		catch (final IOException e)
		{
			final @NotNull String message = "Error: IOException received when trying to save file " + file.getPath();
			parent.log(message);
			parent.showErrorDialog(message, "Exception");
		}
	}
}
