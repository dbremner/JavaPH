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
import org.jetbrains.annotations.Nullable;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxEditor;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public final class TextFieldComboBoxEditor implements ComboBoxEditor
{
	private final @NotNull JTextField textField;

	private final @NotNull EventListenerList listenerList = new EventListenerList();

	public TextFieldComboBoxEditor(final ActionListener listener)
	{
		textField = new JTextField();
		textField.setBorder(BorderFactory.createEmptyBorder());
		textField.addActionListener(listener);
	}

	@Override
	public void addActionListener(final ActionListener l)
	{
		listenerList.add(java.awt.event.ActionListener.class, l);
	}

	private void fireActionPerformed(final ActionEvent e)
	{
		final Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ActionListener.class) {
				((ActionListener) listeners[i + 1]).actionPerformed(e);
			}
		}
	}

	@Override
    public @NotNull Component getEditorComponent()
	{
		return textField;
	}

	@Override
	public Object getItem()
	{
		return textField.getText();
	}

	@Override
	public void removeActionListener(final ActionListener l)
	{
		listenerList.remove(java.awt.event.ActionListener.class, l);
	}

	@Override
	public void selectAll()
	{
		textField.selectAll();
	}

	@Override
	public void setItem(final @Nullable Object anObject)
	{
		if (anObject != null) {
            textField.setText(anObject.toString());
        }
	}
}
