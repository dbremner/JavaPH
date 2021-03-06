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

import com.bovilexics.javaph.FilePaths;
import org.jetbrains.annotations.NotNull;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import java.awt.Frame;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
abstract class JavaPHDialog extends JDialog
{
	JavaPHDialog(final @NotNull IconProvider provider, final @NotNull String title)
	{
		setModal(true);
		setIcon(provider.getImageIcon(FilePaths.PH_ICON_SMALLER_GIF));
		setTitle(title);
	}
	
	private void setIcon(final @NotNull ImageIcon icon)
	{
		final Frame frame = JOptionPane.getFrameForComponent(this);
		frame.setIconImage(icon.getImage());
	}

	@Override
	public final void setModal(final boolean modal)
	{
		super.setModal(modal);
	}

	@Override
	public final void setTitle(final @NotNull String title)
	{
		super.setTitle(title);
	}
}
