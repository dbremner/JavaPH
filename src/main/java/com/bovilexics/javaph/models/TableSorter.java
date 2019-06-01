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

import org.jetbrains.annotations.NotNull;
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
	private final @NotNull TableModel realModel;
	private int[] indexes;
	
	public TableSorter(@NotNull TableModel model)
	{
		realModel = model;
		realModel.addTableModelListener(this);
		allocate();
	}

	private void allocate()
	{
		indexes = new int[getRowCount()];
		
		for (int i = 0; i < indexes.length; ++i) {
            indexes[i] = i;
        }
	}
	
	private int compare(int i, int j, int col)
	{
		final @Nullable Object io = realModel.getValueAt(i, col);
		final @Nullable Object jo = realModel.getValueAt(j, col);

		final @NotNull String left = (io == null) ? "" : io.toString();
		final @NotNull String right  = (jo == null) ? "" : jo.toString();
		
		final int c = left.compareTo(right);
		
		return Integer.compare(c, 0);
	}
	
	public @Nullable TableModel getModel()
	{
		return realModel;
	}
	
	@Override
    public Object getValueAt(int row, int col)
	{
		return realModel.getValueAt(indexes[row], col);
	}
	
	@Override
    public void setValueAt(Object aValue, int row, int col)
	{
		realModel.setValueAt(aValue, indexes[row], col);
	}

	public void sort(int column)
	{
		final int rowCount = getRowCount();
		
		for (int i = 0; i < rowCount; i++)
		{
			for (int j = i + 1; j < rowCount; j++)
			{
				if (compare(indexes[i], indexes[j], column) < 0) {
                    swap(i, j);
                }
			}
		}
	}
	
	private void swap(int i, int j)
	{
		final int temp = indexes[i];
		indexes[i] = indexes[j];
		indexes[j] = temp;
	}
	
	@Override
    public void tableChanged(TableModelEvent e)
	{
		allocate();
	}
	
	// TableModel pass-through methods follow
	
	@Override
    public void addTableModelListener(TableModelListener listener)
	{
		realModel.addTableModelListener(listener);
	}

	@Override
    public Class getColumnClass(int columnIndex)
	{
		return realModel.getColumnClass(columnIndex);
	}

	@Override
    public int getColumnCount()
	{
		return realModel.getColumnCount();
	}
	
	@Override
    public String getColumnName(int columnIndex)
	{
		return realModel.getColumnName(columnIndex);
	}
	
	@Override
    public int getRowCount()
	{
		return realModel.getRowCount();
	}

	@Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return realModel.isCellEditable(rowIndex, columnIndex);
	}
	
	@Override
    public void removeTableModelListener(TableModelListener listener)
	{
		realModel.removeTableModelListener(listener);
	}
}
