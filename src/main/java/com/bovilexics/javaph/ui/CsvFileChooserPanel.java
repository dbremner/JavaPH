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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.bovilexics.javaph.JavaPH;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public class CsvFileChooserPanel extends JPanel
{
	class CheckBoxListener implements ActionListener
	{
		public void actionPerformed(ActionEvent ae)
		{
			parent.setFieldQuoted(quotesCheckBox.isSelected());
		}
	}

	class RadioButtonListener implements ActionListener
	{	
		public void actionPerformed(ActionEvent ae)
		{
			if (commaRadioButton.isSelected())
				parent.setFieldSeparator(JavaPH.COMMA_SEPARATOR);
			else if (tabRadioButton.isSelected())
				parent.setFieldSeparator(JavaPH.TAB_SEPARATOR);
			else if (customRadioButton.isSelected())
				parent.setFieldSeparator(customTextField.getText());
		}
	}

	@Nullable
    private JavaPH parent;

	private JCheckBox quotesCheckBox;

	private JRadioButton commaRadioButton;
	private JRadioButton tabRadioButton;
	private JRadioButton customRadioButton;
	
	private JTextField customTextField;
	
	public CsvFileChooserPanel(@Nullable JavaPH javaph)
	{
		if (javaph == null)
			throw new IllegalArgumentException("Error: null JavaPH value passed into CsvFileChooserPanel");
			
		parent = javaph;
		
		int horizStrut = 6;
		int vertStrut = 2;
		
		@NotNull RadioButtonListener listener = new RadioButtonListener();
		
		setBorder(BorderFactory.createEtchedBorder());
		setPreferredSize(new Dimension(120, 0));

		@NotNull JLabel label = new JLabel("Field Separator");
		
		@NotNull ButtonGroup group = new ButtonGroup();
		
		commaRadioButton = new JRadioButton("Comma");
		tabRadioButton = new JRadioButton("Tab");
		customRadioButton = new JRadioButton("Custom");

		String separator = parent.getFieldSeparator();

		if (separator.equals(JavaPH.COMMA_SEPARATOR))
			commaRadioButton.setSelected(true);
		else if (separator.equals(JavaPH.TAB_SEPARATOR))
			tabRadioButton.setSelected(true);
		else
			customRadioButton.setSelected(true);
		
		commaRadioButton.addActionListener(listener);
		tabRadioButton.addActionListener(listener);
		customRadioButton.addActionListener(listener);

		group.add(commaRadioButton);
		group.add(tabRadioButton);
		group.add(customRadioButton);
		
		customTextField = new JTextField(parent.getLastCustomSeparator());
		customTextField.setMinimumSize(new Dimension(20, customTextField.getPreferredSize().height));
		customTextField.setMaximumSize(new Dimension(20, customTextField.getPreferredSize().height));
		customTextField.setPreferredSize(new Dimension(20, customTextField.getPreferredSize().height));
		customTextField.addKeyListener(new KeyListener()
		{
			public void keyPressed(KeyEvent ke)
			{
			}
				
			public void keyReleased(@NotNull KeyEvent ke)
			{
				if (!ke.isActionKey() && ke.getKeyCode() != KeyEvent.VK_BACK_SPACE)
				{
					customRadioButton.setSelected(true);
					parent.setFieldSeparator(customTextField.getText());
				}
			}
				
			public void keyTyped(KeyEvent ke)
			{
			}
		});

		quotesCheckBox = new JCheckBox("Add Quotes");
		quotesCheckBox.addActionListener(new CheckBoxListener());
		quotesCheckBox.setSelected(parent.isFieldQuoted());
		quotesCheckBox.setToolTipText("Add leading and trailing quotes to all table values");

		@NotNull GridBagLayout gbl = new GridBagLayout();
		@NotNull GridBagConstraints gbc = new GridBagConstraints();
		
		setLayout(gbl);
		
		gbc.anchor = GridBagConstraints.NORTHWEST;
		add(Box.createHorizontalStrut(horizStrut), gbc);
		add(label, gbc);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		add(new JLabel(" "), gbc);

		gbc.weightx = 0;
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
}
