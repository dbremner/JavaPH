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

import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

import com.bovilexics.javaph.JavaPH;
import com.bovilexics.javaph.ui.CsvFileChooser;
import com.bovilexics.javaph.ui.TextFileChooser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public class SaveAction extends AbstractAction
{
	@NotNull
	private JavaPH parent;
		
	public SaveAction(@NotNull JavaPH javaph)
	{
		super("Save Results", new ImageIcon(javaph.getURL("img/save.gif")));

		parent = javaph;
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		int selectedTab = parent.getResultPanel().getSelectedIndex();
		int state = JFileChooser.CANCEL_OPTION;
		
		File file;
		JFileChooser chooser;
		
		if (selectedTab == JavaPH.RESULT_TABLE_TAB)
			chooser = new CsvFileChooser(parent);
		else
			chooser = new TextFileChooser();

		chooser.setDialogTitle("Save " + JavaPH.TAB_LABELS[selectedTab]);
		chooser.setSelectedFile(new File(JavaPH.TAB_FILENAMES[selectedTab]));
			
		state = chooser.showSaveDialog(parent.getDefaultPane());
		file = chooser.getSelectedFile();
			
		if (state == JFileChooser.APPROVE_OPTION && file != null)
		{
			parent.log("Saving file " + file.getPath());
			
			if (selectedTab == JavaPH.RESULT_TABLE_TAB)
				saveCsvFile(selectedTab, file);
			else
				saveTextFile(selectedTab, file);
		}
	}
	
	private void saveCsvFile(int tab, @NotNull File file)
	{
		String message;
		StringBuffer toWrite;

		boolean quoted = parent.isFieldQuoted();
	
		String separator = parent.getFieldSeparator();
		TableModel model = parent.getResultTable().getModel();

		int cols = model.getColumnCount();
		int rows = model.getRowCount();
		
		int c, r;
		
		if (rows <= 0)
		{
			message = "Nothing to save in " + JavaPH.TAB_LABELS[tab] + " tab";
			parent.log(message);
			JOptionPane.showMessageDialog(parent.getDefaultPane(), message, "Finished", JOptionPane.WARNING_MESSAGE);
		}
		else
		{
			try
			{
				@NotNull BufferedWriter writer = new BufferedWriter(new FileWriter(file));

				// start at row -1 to grab headers first
				for (r = -1; r < rows; r++)
				{
					toWrite = new StringBuffer();

					for (c = 0; c < cols; c++)
					{
						if (c > 0)
							toWrite.append(separator);

						if (quoted)
							toWrite.append(JavaPH.FIELD_QUOTE);
						
						if (r == -1) // write the header
							toWrite.append((model.getColumnName(c)) == null ? "" : model.getColumnName(c));
						else // write the value	
							toWrite.append((model.getValueAt(r, c)) == null ? "" : model.getValueAt(r, c).toString());

						if (quoted)
							toWrite.append(JavaPH.FIELD_QUOTE);

					}
					toWrite.append("\n");
				
					writer.write(toWrite.toString());
					writer.flush();
				}
				writer.close();
				
				message = "File save finished";
				parent.log(message);
				JOptionPane.showMessageDialog(parent.getDefaultPane(), message, "Finished", JOptionPane.INFORMATION_MESSAGE);
			}
			catch (IOException e)
			{
				message = "Error: IOException received when trying to save file " + file.getPath();
				parent.log(message);
				JOptionPane.showMessageDialog(parent.getDefaultPane(), message, "Exception", JOptionPane.ERROR_MESSAGE);
			}
		}	
	}
	
	private void saveTextFile(int tab, @NotNull File file)
	{
		String message;
		@Nullable String toWrite = null;
		
		if (tab == JavaPH.RESULT_TEXT_TAB)
		{
			toWrite = parent.getResultText().getText();
		}
		else if (tab == JavaPH.SYSTEM_LOG_TAB)
		{
			toWrite = parent.getLogText().getText();
		}
		
		if (toWrite == null || toWrite.equals(""))
		{
			message = "Nothing to save in " + JavaPH.TAB_LABELS[tab] + " tab";
			parent.log(message);
			JOptionPane.showMessageDialog(parent.getDefaultPane(), message, "Finished", JOptionPane.WARNING_MESSAGE);
		}
		else
		{
			try
			{
				@NotNull BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				writer.write(toWrite);
				writer.flush();
				writer.close();
				
				message = "File save finished";
				parent.log(message);
				JOptionPane.showMessageDialog(parent.getDefaultPane(), message, "Finished", JOptionPane.INFORMATION_MESSAGE);
			}
			catch (IOException e)
			{
				message = "Error: IOException received when trying to save file " + file.getPath();
				parent.log(message);
				JOptionPane.showMessageDialog(parent.getDefaultPane(), message, "Exception", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
