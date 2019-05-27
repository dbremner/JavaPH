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

import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public class TextFieldComboBoxEditor extends AbstractComboBoxEditor
{
	@NotNull
	private final JTextField textField;

	private TextFieldComboBoxEditor()
	{
		textField = new JTextField();
		textField.setBorder(BorderFactory.createEmptyBorder());
	}

	public TextFieldComboBoxEditor(ActionListener listener)
	{
		this();
		textField.addActionListener(listener);
	}

	@NotNull
	public Component getEditorComponent()
	{
		return textField;
	}

	public Object getItem()
	{
		return textField.getText();
	}

	public void selectAll()
	{
		textField.selectAll();
	}

	public void setItem(@Nullable Object obj)
	{
		if (obj != null)
			textField.setText(obj.toString());
	}
}
