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
	JavaPHDialog(@NotNull IconProvider javaph)
	{
		setModal(true);
		setIcon(javaph.getImageIcon("img/ph-icon-smaller.gif"));
	}

	JavaPHDialog(@NotNull IconProvider provider, @NotNull String title)
	{
		this(provider);
		setTitle(title);
	}
	
	private void setIcon(@NotNull ImageIcon icon)
	{
		final Frame frame = JOptionPane.getFrameForComponent(this);
		frame.setIconImage(icon.getImage());
	}
}
