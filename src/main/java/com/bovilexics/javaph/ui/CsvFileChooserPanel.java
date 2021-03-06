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

import com.bovilexics.javaph.JavaPH;
import com.bovilexics.javaph.JavaPHConstants;
import org.jetbrains.annotations.NotNull;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static com.bovilexics.javaph.JavaPHConstants.COMMA_SEPARATOR;
import static com.bovilexics.javaph.JavaPHConstants.TAB_SEPARATOR;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
final class CsvFileChooserPanel extends JPanel
{
	private final @NotNull JavaPH parent;

	private final @NotNull JCheckBox quotesCheckBox;

	private final @NotNull JRadioButton commaRadioButton;
	private final @NotNull JRadioButton tabRadioButton;
	private final @NotNull JRadioButton customRadioButton;
	
	private final @NotNull JTextField customTextField;
	
	CsvFileChooserPanel(final @NotNull JavaPH javaph)
	{
		parent = javaph;
		
		setBorder(BorderFactory.createEtchedBorder());
		setPreferredSize(new Dimension(120, 0));

		final @NotNull JLabel label = new JLabel(JavaPHConstants.FIELD_SEPARATOR);
		
		final @NotNull ButtonGroup group = new ButtonGroup();
		
		commaRadioButton = new JRadioButton(JavaPHConstants.COMMA);
		tabRadioButton = new JRadioButton(JavaPHConstants.TAB);
		customRadioButton = new JRadioButton(JavaPHConstants.CUSTOM);

		final @NotNull String separator = parent.getFieldSeparator();

		if (separator.equals(COMMA_SEPARATOR)) {
			commaRadioButton.setSelected(true);
		} else if (separator.equals(TAB_SEPARATOR)) {
			tabRadioButton.setSelected(true);
		} else {
			customRadioButton.setSelected(true);
		}

		commaRadioButton.addActionListener(this::setSeparator);
		tabRadioButton.addActionListener(this::setSeparator);
		customRadioButton.addActionListener(this::setSeparator);

		group.add(commaRadioButton);
		group.add(tabRadioButton);
		group.add(customRadioButton);
		
		customTextField = new JTextField(parent.getLastCustomSeparator());
		final int CUSTOM_TEXT_FIELD_WIDTH = 20;
		final @NotNull Dimension dimension = new Dimension(CUSTOM_TEXT_FIELD_WIDTH, customTextField.getPreferredSize().height);
		customTextField.setMinimumSize(new Dimension(dimension));
		customTextField.setMaximumSize(new Dimension(dimension));
		customTextField.setPreferredSize(new Dimension(dimension));
		customTextField.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(final @NotNull KeyEvent e)
			{
				if (!e.isActionKey() && e.getKeyCode() != KeyEvent.VK_BACK_SPACE)
				{
					customRadioButton.setSelected(true);
					parent.setFieldSeparator(customTextField.getText());
				}
			}
		});

		quotesCheckBox = new JCheckBox(JavaPHConstants.ADD_QUOTES);
		quotesCheckBox.addActionListener(ae -> parent.setFieldQuoted(quotesCheckBox.isSelected()));
		quotesCheckBox.setSelected(parent.isFieldQuoted());
		quotesCheckBox.setToolTipText(JavaPHConstants.ADD_LEADING_AND_TRAILING_QUOTES_TO_ALL_TABLE_VALUES);

		final @NotNull LayoutManager gbl = new GridBagLayout();
		final @NotNull GridBagConstraints gbc = new GridBagConstraints();
		
		setLayout(gbl);
		
		gbc.anchor = GridBagConstraints.NORTHWEST;
		final int horizStrut = 6;
		add(Box.createHorizontalStrut(horizStrut), gbc);
		add(label, gbc);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		add(new JLabel(" "), gbc);

		gbc.weightx = 0;
		final int vertStrut = 2;
		add(Box.createVerticalStrut(vertStrut), gbc);
		
		gbc.gridwidth = 1;
		add(Box.createHorizontalStrut(horizStrut), gbc);
		add(commaRadioButton, gbc);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		add(new JLabel(" "), gbc);


		gbc.weightx = 0;
		add(Box.createVerticalStrut(vertStrut), gbc);
		
		gbc.gridwidth = 1;
		add(Box.createHorizontalStrut(horizStrut), gbc);
		add(tabRadioButton, gbc);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		add(new JLabel(" "), gbc);
		
		gbc.weightx = 0;
		add(Box.createVerticalStrut(vertStrut), gbc);
		
		gbc.gridwidth = 1;
		add(Box.createHorizontalStrut(horizStrut), gbc);
		add(customRadioButton, gbc);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		add(customTextField, gbc);
	
		gbc.weightx = 0;
		add(Box.createVerticalStrut(vertStrut * 6), gbc);
		
		gbc.gridwidth = 1;
		add(Box.createHorizontalStrut(horizStrut), gbc);
		add(quotesCheckBox, gbc);
	}

	@SuppressWarnings("unused")
    private void setSeparator(final @NotNull ActionEvent ae)
	{
		if (commaRadioButton.isSelected())
		{
			parent.setFieldSeparator(COMMA_SEPARATOR);
		}
		else if (tabRadioButton.isSelected())
		{
			parent.setFieldSeparator(TAB_SEPARATOR);
		}
		else if (customRadioButton.isSelected())
		{
			parent.setFieldSeparator(customTextField.getText());
		}
	}
}
