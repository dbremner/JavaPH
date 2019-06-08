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
package com.bovilexics.javaph.ui;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.Ostermiller.util.Browser;
import com.bovilexics.javaph.JavaPH;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
final class HyperLinkLabel extends JLabel
{
	private final @NotNull JavaPH parent;

	public HyperLinkLabel(final @NotNull JavaPH javaph, final String text, final String url)
	{
		super("<html><font name='Dialog' color='blue'><u>" + text + "</u></font></html>");

		parent = javaph;
		
		Browser.init();
	
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(final MouseEvent me)
			{
				try
				{
					Browser.displayURL(url, text);
				}
				catch (final IOException e)
				{
					final @NotNull String message = "Error: IOException received when trying to open " + url;
					parent.log(message);
					JOptionPane.showMessageDialog(parent.getDefaultPane(), message, "Exception", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

	}
}
