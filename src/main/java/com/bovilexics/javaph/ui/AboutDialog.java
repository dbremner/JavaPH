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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.bovilexics.javaph.JavaPH;
import com.bovilexics.javaph.JavaPHConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public final class AboutDialog extends JavaPHDialog implements JavaPHConstants
{
	@NotNull
    private final JavaPH parent;

	@NotNull
	private final JButton okButton;

	public AboutDialog(@NotNull JavaPH javaph)
	{
		super(javaph);

		parent = javaph;

		setTitle("About " + INFO_NAME);
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		@NotNull JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBorder(BorderFactory.createEtchedBorder());
		contentPane.add(contentPanel, BorderLayout.CENTER);

		@NotNull JPanel imagePanel = new JPanel(new BorderLayout());
		
		@NotNull JLabel phImageLabel = new JLabel();
		phImageLabel.setIcon(new ImageIcon(parent.getURL("img/ph-smaller.gif")));
		
		imagePanel.add(phImageLabel, BorderLayout.CENTER);
		
		contentPanel.add(imagePanel, BorderLayout.CENTER);
		contentPanel.add(getInfoPanel(), BorderLayout.EAST);

		@NotNull JPanel buttonPanel = new JPanel();
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		
		okButton = new JButton("OK");
		okButton.addActionListener(ae -> dispose());
		
		buttonPanel.add(okButton);
		
		pack();
	}
	
	@NotNull
    private JPanel getInfoPanel()
	{

		@NotNull JPanel infoPanel = new JPanel();
		
		@NotNull GridBagLayout gbl = new GridBagLayout();
		@NotNull GridBagConstraints gbc = new GridBagConstraints();
		
		infoPanel.setLayout(gbl);
		
		gbc.anchor = GridBagConstraints.NORTHWEST;
		int horizStrut = 6;
		infoPanel.add(Box.createHorizontalStrut(horizStrut), gbc);
		infoPanel.add(new JLabel("Name :"), gbc);
		infoPanel.add(Box.createHorizontalStrut(horizStrut), gbc);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		infoPanel.add(new JLabel(INFO_NAME), gbc);
		
		gbc.weightx = 0;
		int vertStrut = 2;
		infoPanel.add(Box.createVerticalStrut(vertStrut), gbc);
		
		gbc.gridwidth = 1;
		infoPanel.add(Box.createHorizontalStrut(horizStrut), gbc);
		infoPanel.add(new JLabel("Version :"), gbc);
		infoPanel.add(Box.createHorizontalStrut(horizStrut), gbc);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		infoPanel.add(new JLabel(INFO_VERSION), gbc);

		gbc.weightx = 0;
		infoPanel.add(Box.createVerticalStrut(vertStrut), gbc);
		
		gbc.gridwidth = 1;
		infoPanel.add(Box.createHorizontalStrut(horizStrut), gbc);
		infoPanel.add(new JLabel("Date :"), gbc);
		infoPanel.add(Box.createHorizontalStrut(horizStrut), gbc);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		infoPanel.add(new JLabel(INFO_DATE), gbc);

		gbc.weightx = 0;
		infoPanel.add(Box.createVerticalStrut(vertStrut), gbc);
		
		gbc.gridwidth = 1;
		infoPanel.add(Box.createHorizontalStrut(horizStrut), gbc);
		infoPanel.add(new JLabel("Author :"), gbc);
		infoPanel.add(Box.createHorizontalStrut(horizStrut), gbc);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		infoPanel.add(new JLabel(INFO_AUTHOR), gbc);

		gbc.weightx = 0;
		infoPanel.add(Box.createVerticalStrut(vertStrut), gbc);
		
		gbc.gridwidth = 1;
		infoPanel.add(Box.createHorizontalStrut(horizStrut), gbc);
		infoPanel.add(new JLabel("Contact :"), gbc);
		infoPanel.add(Box.createHorizontalStrut(horizStrut), gbc);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		infoPanel.add(new HyperLinkLabel(parent, INFO_CONTACT, "mailto:" + INFO_CONTACT), gbc);

		gbc.weightx = 0;
		infoPanel.add(Box.createVerticalStrut(vertStrut), gbc);

		gbc.gridwidth = 1;
		infoPanel.add(Box.createHorizontalStrut(horizStrut), gbc);
		infoPanel.add(new JLabel("Home :"), gbc);
		infoPanel.add(Box.createHorizontalStrut(horizStrut), gbc);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		infoPanel.add(new HyperLinkLabel(parent, "JavaPH Home Page", INFO_HOME), gbc);

		gbc.weightx = 0;
		infoPanel.add(Box.createVerticalStrut(vertStrut * 10), gbc);

		gbc.gridwidth = 1;
		infoPanel.add(Box.createHorizontalStrut(horizStrut), gbc);
		infoPanel.add(new JLabel("Copyright :"), gbc);
		infoPanel.add(Box.createHorizontalStrut(horizStrut), gbc);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		infoPanel.add(new JLabel(INFO_COPYRIGHT + "     "), gbc);

		gbc.weightx = 0;
		infoPanel.add(Box.createVerticalStrut(vertStrut * 10), gbc);

		return infoPanel;
	}
}
