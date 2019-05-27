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

import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public class CustomButtonGroup extends ButtonGroup
{
	private boolean enabled = true;
	
	public int getSelectedIndex()
	{
		boolean foundSelected = false;
		int currentIndex = -1;
		int selectedIndex = -1;
		
		Enumeration enum_ = getElements();
		
		while (enum_.hasMoreElements() && !foundSelected)
		{
			currentIndex++;
			
			if (((AbstractButton)enum_.nextElement()).isSelected())
			{
				selectedIndex = currentIndex;
				foundSelected = true;
			}
		}
		
		return selectedIndex;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enable)
	{
		Enumeration enum = getElements();
		
		while (enum.hasMoreElements())
		{
			((AbstractButton)enum.nextElement()).setEnabled(enable);
		}
		
		enabled = enable;
	}
}
