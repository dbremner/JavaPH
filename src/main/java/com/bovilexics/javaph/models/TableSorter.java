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
import java.util.Optional;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public class TableSorter implements TableModel, TableModelListener
{
	private final @NotNull ResultTableModel realModel;
	private int[] indexes;
	
	public TableSorter(final @NotNull ResultTableModel model)
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
	
	private int compare(final int i, final int j, final int col)
	{
		final @NotNull Optional<Object> ioOpt = Optional.ofNullable(realModel.getValueAt(i, col));
		final @NotNull Optional<Object> joOpt = Optional.ofNullable(realModel.getValueAt(j, col));

		final @NotNull String left = ioOpt.orElse("").toString();
		final @NotNull String right  = joOpt.orElse("").toString();
		
		final int c = left.compareTo(right);
		
		return Integer.compare(c, 0);
	}
	
	public @Nullable ResultTableModel getModel()
	{
		return realModel;
	}
	
	@Override
    public Object getValueAt(final int rowIndex, final int columnIndex)
	{
		return realModel.getValueAt(indexes[rowIndex], columnIndex);
	}
	
	@Override
    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex)
	{
		realModel.setValueAt(aValue, indexes[rowIndex], columnIndex);
	}

	public void sort(final int column)
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
	
	private void swap(final int i, final int j)
	{
		final int temp = indexes[i];
		indexes[i] = indexes[j];
		indexes[j] = temp;
	}
	
	@Override
    public void tableChanged(final TableModelEvent e)
	{
		allocate();
	}
	
	// TableModel pass-through methods follow
	
	@Override
    public void addTableModelListener(final TableModelListener l)
	{
		realModel.addTableModelListener(l);
	}

	@Override
    public Class<?> getColumnClass(final int columnIndex)
	{
		return realModel.getColumnClass(columnIndex);
	}

	@Override
    public int getColumnCount()
	{
		return realModel.getColumnCount();
	}
	
	@Override
    public String getColumnName(final int columnIndex)
	{
		return realModel.getColumnName(columnIndex);
	}
	
	@Override
    public int getRowCount()
	{
		return realModel.getRowCount();
	}

	@Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex)
	{
		return realModel.isCellEditable(rowIndex, columnIndex);
	}
	
	@Override
    public void removeTableModelListener(final TableModelListener l)
	{
		realModel.removeTableModelListener(l);
	}
}
