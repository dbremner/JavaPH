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

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import static com.bovilexics.javaph.JavaPHConstants.INFO_COPYRIGHT;
import static com.bovilexics.javaph.JavaPHConstants.INFO_NAME;
import static com.bovilexics.javaph.JavaPHConstants.INFO_VERSION;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public final class SplashWindow extends JWindow {

	@NonNls
	private static final @NotNull String FontName = "Dialog";

	public SplashWindow(final @NotNull IconProvider javaph)
	{

		final Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		final @NotNull JLabel phImageLabel = new JLabel(javaph.getImageIcon("img/ph-bigger.gif"));
		
		final @NotNull JLabel upperInfoLabel = new JLabel(String.format("%s %s", INFO_NAME, INFO_VERSION));
		upperInfoLabel.setFont(new Font(FontName, Font.BOLD, 14));
		upperInfoLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		
		final @NotNull JLabel lowerInfoLabel = new JLabel(INFO_COPYRIGHT);
		lowerInfoLabel.setFont(new Font(FontName, Font.PLAIN, 12));
		lowerInfoLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		
		final @NotNull JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		// contentPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));

		final @NotNull JPanel imagePanel = new JPanel(new BorderLayout());
		imagePanel.setBorder(BorderFactory.createEtchedBorder());
		imagePanel.add(phImageLabel, BorderLayout.CENTER);

		final @NotNull JPanel infoPanel = new JPanel();
		infoPanel.setBorder(BorderFactory.createEtchedBorder());
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.add(new JLabel(" "));
		infoPanel.add(upperInfoLabel);
		infoPanel.add(lowerInfoLabel);
		infoPanel.add(new JLabel(" "));
	
		contentPanel.add(imagePanel, BorderLayout.CENTER);
		contentPanel.add(infoPanel, BorderLayout.SOUTH);

		contentPane.add(contentPanel, BorderLayout.CENTER);

		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		final Dimension panelSize = contentPanel.getPreferredSize();
		
		setLocation(screenSize.width/2 - (panelSize.width/2), screenSize.height/2 - (panelSize.height/2));
		pack(); 
	}

}
