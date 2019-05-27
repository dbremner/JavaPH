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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxEditor;
import javax.swing.event.EventListenerList;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
abstract class AbstractComboBoxEditor implements ComboBoxEditor
{
	@NotNull
	final EventListenerList listenerList;
	
	public AbstractComboBoxEditor()
	{
		listenerList = new EventListenerList();
	}

	public void addActionListener(ActionListener listener)
	{
		listenerList.add(java.awt.event.ActionListener.class, listener);
	}

	protected void fireActionPerformed(ActionEvent e)
	{
		Object listeners[] = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2)
			if (listeners[i] == (java.awt.event.ActionListener.class))
				 ((ActionListener) listeners[i + 1]).actionPerformed(e);
	}

	@NotNull
	public abstract Component getEditorComponent();

	public abstract Object getItem();

	public void removeActionListener(ActionListener listener)
	{
		listenerList.remove(java.awt.event.ActionListener.class, listener);
	}

	public abstract void selectAll();

	public abstract void setItem(Object obj);	
}
