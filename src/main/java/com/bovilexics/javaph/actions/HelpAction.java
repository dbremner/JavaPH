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

import com.Ostermiller.util.Browser;
import com.bovilexics.javaph.JavaPH;
import org.jetbrains.annotations.NotNull;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public final class HelpAction extends AbstractAction
{
	private final @NotNull JavaPH parent;
	
	public HelpAction(@NotNull JavaPH javaph)
	{
		super("Help Contents", javaph.getImageIcon("img/help.gif"));
		
		parent = javaph;
		
		Browser.init();
	}
	
	@Override
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			Browser.displayURL(parent.getURL("help/index.html").toString(), "JavaPH Help");
		}
		catch (IOException e)
		{
			final @NotNull String message = "Error: IOException received when trying to open " + parent.getURL("help/index.html").toString();
			parent.log(message);
			JOptionPane.showMessageDialog(parent.getDefaultPane(), message, "Exception", JOptionPane.ERROR_MESSAGE);
		}
	}
}
