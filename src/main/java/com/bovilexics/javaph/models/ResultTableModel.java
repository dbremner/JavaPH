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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.swing.table.DefaultTableModel;
import java.util.Arrays;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public final class ResultTableModel extends DefaultTableModel
{
	private static final int DEFAULT_COLS = 1;
	private static final int DEFAULT_ROWS = 0;
	private static final @NotNull String DEFAULT_HEADER = "No Results";

	public ResultTableModel()
	{	
		resetModel();
	}

	@Override
	@Contract(pure = true)
	public boolean isCellEditable(final int row, final int column)
	{
		return false;
	}
	
	public void resetModel()
	{
		final @NotNull Object[] headers = new Object[DEFAULT_COLS];
		Arrays.fill(headers, DEFAULT_HEADER);
		setColumnIdentifiers(headers);
		setColumnCount(DEFAULT_COLS);
		setRowCount(DEFAULT_ROWS);
	}
}
