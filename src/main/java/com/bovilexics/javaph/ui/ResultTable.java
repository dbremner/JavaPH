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

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.bovilexics.javaph.models.ResultTableModel;
import com.bovilexics.javaph.models.TableSorter;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public class ResultTable extends JTable
{
	private static final int WIDTH_BUFFER = 10;
	
	public ResultTable()
	{
		this(new ResultTableModel());
	}
	
	public ResultTable(TableModel model)
	{
		super(model);
		
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setModel(new TableSorter(getModel()));
	}

	private int getColumnHeaderWidth(@NotNull TableColumn column)
	{
		TableCellRenderer renderer = column.getHeaderRenderer();
		
		if (renderer == null)
			renderer = getDefaultRenderer(column.getClass());
		
		Component component = renderer.getTableCellRendererComponent(this, column.getHeaderValue(), false, false, 0, 0);
		
		return component.getPreferredSize().width + WIDTH_BUFFER;
	}
	
	private int getPreferredWidthForColumn(@NotNull TableColumn column)
	{
		int headerWidth = getColumnHeaderWidth(column);
		int cellWidth = getWidestCellWidth(column);
		
		return (headerWidth > cellWidth) ? headerWidth : cellWidth;  
	}

	private int getWidestCellWidth(@NotNull TableColumn column)
	{
		int colIndex = column.getModelIndex();
		int rowIndex;
		
		int maxWidth = 0;
		int width = 0;
		
		Component component;
		TableCellRenderer renderer;
		
		for (rowIndex = 0; rowIndex < getRowCount(); rowIndex++)
		{
			renderer = getCellRenderer(rowIndex, colIndex);
			component = renderer.getTableCellRendererComponent(this, getValueAt(rowIndex, colIndex), false, false, rowIndex, colIndex);
			
			width = component.getPreferredSize().width;
			
			if (width > maxWidth)
				maxWidth = width;
		}
		
		return maxWidth + WIDTH_BUFFER;
	}

	public void resetColumnWidths()
	{
		int width;
		TableColumn column;
		
		for (int i = 0; i < getColumnCount(); i++)
		{
			column = getColumn(getModel().getColumnName(i));
			width = getPreferredWidthForColumn(column);
			
			column.setMinWidth(0);
			column.setPreferredWidth(width);
			column.setMaxWidth(width);
		}
	}
}
