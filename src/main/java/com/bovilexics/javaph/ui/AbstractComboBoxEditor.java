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
	private final @NotNull EventListenerList listenerList;
	
	AbstractComboBoxEditor()
	{
		listenerList = new EventListenerList();
	}

	@Override
    public void addActionListener(final ActionListener listener)
	{
		listenerList.add(java.awt.event.ActionListener.class, listener);
	}

	protected void fireActionPerformed(final ActionEvent e)
	{
		final Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ActionListener.class) {
				((ActionListener) listeners[i + 1]).actionPerformed(e);
			}
		}
	}

	@Override
    public abstract @NotNull Component getEditorComponent();

	@Override
    public abstract Object getItem();

	@Override
    public void removeActionListener(final ActionListener listener)
	{
		listenerList.remove(java.awt.event.ActionListener.class, listener);
	}

	@Override
    public abstract void selectAll();

	@Override
    public abstract void setItem(Object obj);
}
