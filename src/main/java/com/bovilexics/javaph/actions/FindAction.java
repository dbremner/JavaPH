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
import javax.swing.ImageIcon;
import java.awt.event.ActionEvent;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public final class FindAction extends AbstractAction
{
	@NotNull
	private final Runnable runnable;

	public FindAction(@NotNull JavaPH javaph)
	{
		super("Find", new ImageIcon(javaph.getURL("img/find.gif")));

		runnable = javaph::showFindDialog;
	}
	
	@Override
	public void actionPerformed(ActionEvent ae)
	{
		runnable.run();
	}
}
