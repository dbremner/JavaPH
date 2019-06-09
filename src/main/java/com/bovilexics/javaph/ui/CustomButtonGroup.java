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

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import java.util.Enumeration;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public final class CustomButtonGroup extends ButtonGroup
{
	private boolean enabled = true;
	
	public int getSelectedIndex()
	{
		int currentIndex = -1;

		final Enumeration<AbstractButton> enum_ = getElements();
		
		while (enum_.hasMoreElements())
		{
			currentIndex++;
			
			if (enum_.nextElement().isSelected())
			{
				return currentIndex;
			}
		}

		return -1;
	}

	@Override
	public void add(final @NotNull AbstractButton b)
	{
		super.add(b);
	}

	@Override
	public void remove(final @NotNull AbstractButton b)
	{
		super.remove(b);
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(final boolean enable)
	{
		for(final @NotNull AbstractButton button : buttons)
		{
			button.setEnabled(enable);
		}

		enabled = enable;
	}
}
