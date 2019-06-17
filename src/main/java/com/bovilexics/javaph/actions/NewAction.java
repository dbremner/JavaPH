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
import org.jetbrains.annotations.NotNull;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

import static com.bovilexics.javaph.JavaPHConstants.QUERY_COMMAND;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public final class NewAction extends AbstractAction
{
	private final @NotNull JavaPH parent;
		
	public NewAction(final @NotNull JavaPH javaph)
	{
		super("New Query", javaph.getImageIcon("img/new.gif"));
			
		parent = javaph;
	}
	
	@Override
	public void actionPerformed(final ActionEvent e)
	{
		// Clear the results
		parent.getResultText().setText("");
		parent.getResultTable().getModel().getModel().resetModel();
		
		// Clear the previous query
		parent.setCommandComboBoxSelectedIndex(QUERY_COMMAND);
		parent.getQueryComboBox().setSelectedItem("");
		
		// Log the action
		parent.log("New query requested, results cleared");
	}
}
