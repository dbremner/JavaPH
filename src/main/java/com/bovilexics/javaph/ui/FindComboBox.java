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

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import com.bovilexics.javaph.models.FindComboBoxModel;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
final class FindComboBox extends JComboBox
{
	public FindComboBox()
	{
		this(false);
	}
	
	public FindComboBox(boolean useDefaultModel)
	{
		super();
		
		if (useDefaultModel) {
			setModel(new DefaultComboBoxModel());
		} else {
			setModel(new FindComboBoxModel());
		}
	}
}
