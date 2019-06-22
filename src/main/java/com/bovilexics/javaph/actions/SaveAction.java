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
import com.bovilexics.javaph.ui.Tab;
import com.bovilexics.javaph.ui.TextFileChooser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sun.jvm.hotspot.utilities.AssertionFailure;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import static com.bovilexics.javaph.JavaPHConstants.FIELD_QUOTE;

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
	public void actionPerformed(final @NotNull ActionEvent e)
	{
		final @NotNull Tab tab = parent.getResultPanelSelectedTab();
		final @NotNull JFileChooser chooser;

		switch (tab)
		{
			case ResultTable:
				chooser = new CsvFileChooser(parent);
				break;
			case ResultText:
			case SystemLog:
				chooser = new TextFileChooser();
				break;
			default:
				throw new AssertionFailure("unreachable");
		}

		chooser.setDialogTitle("Save " + tab.getLabel());
		chooser.setSelectedFile(new File(tab.getFilename()));

		final int state = chooser.showSaveDialog(parent.getDefaultPane());
		final File file = chooser.getSelectedFile();

		if (state == JFileChooser.APPROVE_OPTION && file != null)
		{
			parent.log("Saving file " + file.getPath());

			switch (tab)
			{
				case ResultTable:
					saveCsvFile(tab, file);
					break;
				case ResultText:
				case SystemLog:
					saveTextFile(tab, file);
					break;
			}
		}
	}
	
	private void saveCsvFile(final @NotNull Tab tab, final @NotNull File file)
	{
		final boolean quoted = parent.isFieldQuoted();
	
		final @NotNull String separator = parent.getFieldSeparator();
		final TableModel model = parent.getResultTable().getModel();

		final int cols = model.getColumnCount();
		final int rows = model.getRowCount();

		if (rows <= 0)
		{
			final @NotNull String message = "Nothing to save in " + tab.getLabel() + " tab";
			parent.log(message);
			parent.showWarningDialog(message, "Finished");
		}
		else
		{
			try(final @NotNull BufferedWriter writer = new BufferedWriter(new FileWriter(file)))
			{
				// start at row -1 to grab headers first
				for (int row = -1; row < rows; row++)
				{
					final @NotNull StringBuilder toWrite = new StringBuilder();

					for (int col = 0; col < cols; col++)
					{
						if (col > 0) {
                            toWrite.append(separator);
                        }

						if (quoted) {
                            toWrite.append(FIELD_QUOTE);
                        }
						
						if (row == -1) // write the header
                        {
                        	final @Nullable String name = model.getColumnName(col);
                        	final @NotNull String value = Optional.ofNullable(name).orElse("");
                            toWrite.append(value);
                        }
						else // write the value
                        {
                        	final @Nullable Object value = model.getValueAt(row, col);
                        	final @NotNull String str = Optional.ofNullable(value).map(Object::toString).orElse("");
                            toWrite.append(str);
                        }

						if (quoted)
						{
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
			catch (final @NotNull IOException e)
			{
				final @NotNull String message = "Error: IOException received when trying to save file " + file.getPath();
				parent.log(message);
				parent.showExceptionDialog(message);
			}
		}	
	}
	
	private void saveTextFile(final @NotNull Tab tab, final @NotNull File file)
	{
		final @NotNull String toWrite;

		switch (tab)
		{
			case ResultText:
				toWrite = parent.getResultText().getText();
				break;
			case SystemLog:
				toWrite = parent.getLogText().getText();
				break;
			case ResultTable:
				toWrite = "";
				break;
			default:
				throw new AssertionFailure("unreachable");
		}

		if (toWrite.isEmpty())
		{
			final @NotNull String message = "Nothing to save in " + tab.getLabel() + " tab";
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
		catch (final @NotNull IOException e)
		{
			final @NotNull String message = "Error: IOException received when trying to save file " + file.getPath();
			parent.log(message);
			parent.showExceptionDialog(message);
		}
	}
}
