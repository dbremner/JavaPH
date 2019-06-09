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

import com.Ostermiller.util.Browser;
import com.bovilexics.javaph.JavaPH;
import org.jetbrains.annotations.NotNull;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
final class HyperLinkLabel extends JLabel
{
	public HyperLinkLabel(final @NotNull JavaPH javaph, @NotNull final String text, @NotNull final String url)
	{
		super("<html><font name='Dialog' color='blue'><u>" + text + "</u></font></html>");
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		addMouseListener(new ClickListener(url, text, javaph, javaph.getDefaultPane()));
	}

	private static final class ClickListener extends MouseAdapter
	{
		private final @NotNull String url;
		private final @NotNull String text;
		private final @NotNull JavaPH parent;
		private final @NotNull JRootPane rootPane;

		ClickListener(final @NotNull String url, final @NotNull String text, final @NotNull JavaPH parent, final @NotNull JRootPane rootPane)
		{
			Browser.init();
			this.url = url;
			this.text = text;
			this.parent = parent;
			this.rootPane = rootPane;
		}

		@Override
		public void mouseClicked(final MouseEvent e)
		{
			try
			{
				Browser.displayURL(url, text);
			}
			catch (final @NotNull IOException ex)
			{
				final @NotNull String message = "Error: IOException received when trying to open " + url;
				parent.log(message);
				JOptionPane.showMessageDialog(rootPane, message, "Exception", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
