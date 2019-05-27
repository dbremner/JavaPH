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
package com.bovilexics.javaph.models;

import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public class ResultTableModel extends DefaultTableModel
{
	public static final int DEFAULT_COLS = 1;
	public static final int DEFAULT_ROWS = 0;
	public static final String DEFAULT_HEADER = "No Results";
	public static final String DEFAULT_VALUE = " ";
	
	public ResultTableModel()
	{	
		resetModel();
	}

	public Object[] getDefaultHeaderArray()
	{
		return getDefaultHeaderArray(DEFAULT_COLS);
	}
	
	public Object[] getDefaultHeaderArray(int cols)
	{
		Object[] headers = new Object[DEFAULT_COLS];
		
		for (int i = 0; i < DEFAULT_COLS; i++)
			headers[i] = DEFAULT_HEADER;
			
		return headers;	
	}
	
	public Vector getDefaultHeaderVector()
	{
		return getDefaultHeaderVector(DEFAULT_COLS);
	}
	
	public Vector getDefaultHeaderVector(int cols)
	{
		Vector headers = new Vector();
		
		for (int i = 0; i < cols; i++)
			headers.addElement(DEFAULT_HEADER);
			
		return headers;
	}
	
	public boolean isCellEditable(int row, int col)
	{
		return false;
	}
	
	public void resetModel()
	{
		setColumnIdentifiers(getDefaultHeaderArray());
		setColumnCount(DEFAULT_COLS);
		setRowCount(DEFAULT_ROWS);
	}
}
