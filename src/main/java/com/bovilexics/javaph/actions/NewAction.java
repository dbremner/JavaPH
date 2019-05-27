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

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import com.bovilexics.javaph.JavaPH;
import com.bovilexics.javaph.models.ResultTableModel;
import com.bovilexics.javaph.models.TableSorter;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public class NewAction extends AbstractAction
{
	@NotNull
	private JavaPH parent;
		
	public NewAction(@NotNull JavaPH javaph)
	{
		super("New Query", new ImageIcon(javaph.getURL("img/new.gif")));
			
		parent = javaph;
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		// Clear the results
		parent.getResultText().setText("");
		((ResultTableModel)((TableSorter)parent.getResultTable().getModel()).getModel()).resetModel();
		
		// Clear the previous query
		parent.getCommandComboBox().setSelectedIndex(JavaPH.QUERY_COMMAND);
		parent.getQueryComboBox().setSelectedItem("");
		
		// Log the action
		parent.log("New query requested, results cleared");
	}
}
