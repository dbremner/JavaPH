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

import com.bovilexics.javaph.models.ResultTableModel;
import com.bovilexics.javaph.models.TableSorter;
import org.jetbrains.annotations.NotNull;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.Component;
import java.util.Optional;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public final class ResultTable extends JTable
{
	private static final int WIDTH_BUFFER = 10;
	
	public ResultTable()
	{
		super(new ResultTableModel());
		
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		final @NotNull ResultTableModel model = (ResultTableModel)getModel();
		setModel(new TableSorter(model));
	}


	private int getColumnHeaderWidth(final @NotNull TableColumn column)
	{
		final @NotNull Optional<TableCellRenderer> optional = Optional.ofNullable(column.getHeaderRenderer());
		final @NotNull TableCellRenderer renderer = optional.orElse(getDefaultRenderer(column.getClass()));
		final Component component = renderer.getTableCellRendererComponent(this, column.getHeaderValue(), false, false, 0, 0);
		
		return component.getPreferredSize().width + WIDTH_BUFFER;
	}
	
	private int getPreferredWidthForColumn(final @NotNull TableColumn column)
	{
		final int headerWidth = getColumnHeaderWidth(column);
		final int cellWidth = getWidestCellWidth(column);
		
		return (headerWidth > cellWidth) ? headerWidth : cellWidth;  
	}

	private int getWidestCellWidth(final @NotNull TableColumn column)
	{
		final int colIndex = column.getModelIndex();

		int maxWidth = 0;

		for (int rowIndex = 0; rowIndex < getRowCount(); rowIndex++)
		{
			final TableCellRenderer renderer = getCellRenderer(rowIndex, colIndex);
			final Component component = renderer.getTableCellRendererComponent(this, getValueAt(rowIndex, colIndex), false, false, rowIndex, colIndex);
			final int width = component.getPreferredSize().width;

			if (width > maxWidth) {
                maxWidth = width;
            }
		}
		
		return maxWidth + WIDTH_BUFFER;
	}

	public void resetColumnWidths()
	{
		final @NotNull TableModel model = getModel();
		for (int i = 0; i < getColumnCount(); i++)
		{
			final @NotNull String value = model.getColumnName(i);
			final @NotNull TableColumn column = getColumn(value);
			final int width = getPreferredWidthForColumn(column);

			column.setMinWidth(0);
			column.setPreferredWidth(width);
			column.setMaxWidth(width);
		}
	}

	public @NotNull TableSorter getTableSorter()
	{
		return (TableSorter)dataModel;
	}
}
