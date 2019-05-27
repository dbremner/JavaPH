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

import org.jetbrains.annotations.Nullable;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public class TableSorter implements TableModel, TableModelListener
{
	@Nullable
    private TableModel realModel;
	private int indexes[];
	
	public TableSorter(@Nullable TableModel model)
	{
		if (model == null)
			throw new IllegalArgumentException("Error: null TableModel value passed into TableSorter");
			
		realModel = model;
		realModel.addTableModelListener(this);
		allocate();
	}

	private void allocate()
	{
		indexes = new int[getRowCount()];
		
		for (int i = 0; i < indexes.length; ++i)
			indexes[i] = i;
	}
	
	public int compare(int i, int j, int col)
	{
		Object io = realModel.getValueAt(i, col);
		Object jo = realModel.getValueAt(j, col);
		
		io = (io == null) ? "" : io;
		jo = (jo == null) ? "" : jo;
		
		int c = jo.toString().compareTo(io.toString());
		
		return (c < 0) ? -1 : ((c > 0) ? 1 : 0);
	}
	
	@Nullable
    public TableModel getModel()
	{
		return realModel;
	}
	
	public Object getValueAt(int row, int col)
	{
		return realModel.getValueAt(indexes[row], col);
	}
	
	public void setValueAt(Object aValue, int row, int col)
	{
		realModel.setValueAt(aValue, indexes[row], col);
	}

	public void sort(int column)
	{
		int rowCount = getRowCount();
		
		for (int i = 0; i < rowCount; i++)
		{
			for (int j = i + 1; j < rowCount; j++)
			{
				if (compare(indexes[i], indexes[j], column) < 0)
					swap(i, j);
			}
		}
	}
	
	public void swap(int i, int j)
	{
		int temp = indexes[i];
		indexes[i] = indexes[j];
		indexes[j] = temp;
	}
	
	public void tableChanged(TableModelEvent e)
	{
		allocate();
	}
	
	// TableModel pass-through methods follow
	
	public void addTableModelListener(TableModelListener listener)
	{
		realModel.addTableModelListener(listener);
	}

	public Class getColumnClass(int columnIndex)
	{
		return realModel.getColumnClass(columnIndex);
	}

	public int getColumnCount()
	{
		return realModel.getColumnCount();
	}
	
	public String getColumnName(int columnIndex)
	{
		return realModel.getColumnName(columnIndex);
	}
	
	public int getRowCount()
	{
		return realModel.getRowCount();
	}

	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return realModel.isCellEditable(rowIndex, columnIndex);
	}
	
	public void removeTableModelListener(TableModelListener listener)
	{
		realModel.removeTableModelListener(listener);
	}
}
