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
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.bovilexics.javaph.JavaPH;
import com.bovilexics.javaph.qi.QiServer;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public class PropertiesDialog extends JavaPHDialog
{
	private JavaPH parent;
	
	private JButton applyButton;
	private JButton cancelButton;
	private JButton defaultsButton;
	private JButton okButton;

	private JCheckBox displayLogCheckBox;
	private JCheckBox displaySplashCheckBox;
	private JCheckBox displayToolbarCheckBox;
	private JCheckBox rollToolbarCheckBox;
	private JCheckBox savePositionCheckBox;

	private JComboBox defaultServerComboBox;

	private JRadioButton loadFieldsManual;
	private JRadioButton loadFieldsSelected;
	private JRadioButton loadFieldsStartup;

	private JSlider runtimeSlider;
	
	private JTextField runtimeText;

	public PropertiesDialog(JavaPH javaph)
	{
		super(javaph);

		if (javaph == null)
			throw new IllegalArgumentException("Error: null JavaPH value passed into PropertiesDialog");
			
		parent = javaph;

		setModal(true);
		setTitle(JavaPH.INFO_NAME + " Properties");
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		contentPanel.setBorder(BorderFactory.createEtchedBorder());

		if (!parent.isApplet())		
			contentPanel.add(getStartupPanel(), BorderLayout.NORTH);

		contentPanel.add(getQueryPanel(), BorderLayout.CENTER);

		if (!parent.isApplet())
			contentPanel.add(getServerPanel(), BorderLayout.SOUTH);
		
		contentPane.add(contentPanel, BorderLayout.CENTER);
		contentPane.add(getButtonPanel(), BorderLayout.SOUTH);

		pack();
	}
	
	private JPanel getButtonPanel()
	{
		JPanel buttonPanel = new JPanel();
		
		okButton = new JButton("OK");
		okButton.setMnemonic(KeyEvent.VK_O);
		okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				saveProperties();
				dispose();
			}
		});

		applyButton = new JButton("Apply");
		applyButton.setMnemonic(KeyEvent.VK_A);
		applyButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				saveProperties();
			}
		});

		cancelButton = new JButton("Cancel");
		cancelButton.setMnemonic(KeyEvent.VK_C);
		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				dispose();
			}
		});

		defaultsButton = new JButton("Defaults");
		defaultsButton.setMnemonic(KeyEvent.VK_D);
		defaultsButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				refreshDefaultProperties();
			}
		});
	
		buttonPanel.add(okButton);
		buttonPanel.add(applyButton);
		buttonPanel.add(cancelButton);
		buttonPanel.add(new JLabel("      "));
		buttonPanel.add(defaultsButton);
		
		return buttonPanel;	
	}

	private JPanel getQueryPanel()
	{
		JPanel queryPanel = new JPanel();
		queryPanel.setBorder(BorderFactory.createTitledBorder(" Query Runtime (Seconds) "));
		queryPanel.setLayout(new BorderLayout());
		
		JPanel runtimePanel = new JPanel();
		runtimePanel.setLayout(new FlowLayout());
		
		runtimeSlider = new JSlider(JavaPH.QUERY_RUNTIME_MIN, JavaPH.QUERY_RUNTIME_MAX);
		runtimeSlider.setMinorTickSpacing(10);
		runtimeSlider.setMajorTickSpacing(30);
		runtimeSlider.setPaintTicks(true);
		runtimeSlider.setPaintLabels(true);
		runtimeSlider.setSnapToTicks(true);
		runtimeSlider.setValue(parent.getQueryRuntime());
		runtimeSlider.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent ce)
			{
				runtimeText.setText("" + runtimeSlider.getValue());
			}
		});

		runtimeText = new JTextField();
		runtimeText.setColumns(3);
		runtimeText.setEditable(false);
		runtimeText.setText("" + runtimeSlider.getValue());

		runtimePanel.add(runtimeSlider);
		runtimePanel.add(runtimeText);

		queryPanel.add(runtimePanel, BorderLayout.CENTER);
		
		// Need to add a little height to this panel because for
		// some reason the labels and ticks are below the displayed
		// area when the preferred size is originally calculated
		//
		// This only happens sometimes so it's going to be removed
		// for now until the real root of the problem is found
		// Dimension panelSize = queryPanel.getPreferredSize();
		// queryPanel.setPreferredSize(new Dimension(panelSize.width, panelSize.height + 20));
		
		return queryPanel;
	}

	private JPanel getServerPanel()
	{
		JPanel serverPanel = new JPanel();
		serverPanel.setBorder(BorderFactory.createTitledBorder(" Server Options "));
		serverPanel.setLayout(new BorderLayout());

		JPanel defaultServerPanel = new JPanel();
		defaultServerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JLabel defaultServerLabel = new JLabel("Default Server : ");
		defaultServerLabel.setAlignmentY(JLabel.CENTER_ALIGNMENT);
		
		defaultServerComboBox = new JComboBox(QiServer.getServers());
		defaultServerComboBox.setRenderer(new ServerRenderer(parent));

		defaultServerPanel.add(defaultServerLabel);
		defaultServerPanel.add(defaultServerComboBox);

		loadFieldsManual = new JRadioButton("Load Fields Manually");
		loadFieldsManual.setToolTipText("Load fields for servers manually using the button here");

		loadFieldsSelected = new JRadioButton("Load Fields on Select");
		loadFieldsSelected.setToolTipText("Load fields for servers only when selected from the server list");

		loadFieldsStartup = new JRadioButton("Load Fields on Startup  ( Slow! )");
		loadFieldsStartup.setToolTipText("Load fields for all servers when starting the application");

		ButtonGroup loadFieldsGroup = new ButtonGroup();
		loadFieldsGroup.add(loadFieldsManual);
		loadFieldsGroup.add(loadFieldsSelected);
		loadFieldsGroup.add(loadFieldsStartup);

		int horizStrut = 6;
		int vertStrut = 2;
		
		JPanel loadFieldsPanel = new JPanel();
		
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		
		loadFieldsPanel.setLayout(gbl);
		
		gbc.anchor = GridBagConstraints.NORTHWEST;
		loadFieldsPanel.add(Box.createHorizontalStrut(horizStrut), gbc);
		loadFieldsPanel.add(loadFieldsManual, gbc);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		loadFieldsPanel.add(new JLabel(" "), gbc);
		
		gbc.weightx = 0;
		loadFieldsPanel.add(Box.createVerticalStrut(vertStrut), gbc);
		
		gbc.gridwidth = 1;
		loadFieldsPanel.add(Box.createHorizontalStrut(horizStrut), gbc);
		loadFieldsPanel.add(loadFieldsSelected, gbc);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		loadFieldsPanel.add(new JLabel(" "), gbc);
		
		gbc.weightx = 0;
		loadFieldsPanel.add(Box.createVerticalStrut(vertStrut), gbc);
		
		gbc.gridwidth = 1;
		loadFieldsPanel.add(Box.createHorizontalStrut(horizStrut), gbc);
		loadFieldsPanel.add(loadFieldsStartup, gbc);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		loadFieldsPanel.add(new JLabel(" "), gbc);

		serverPanel.add(defaultServerPanel, BorderLayout.NORTH);
		serverPanel.add(loadFieldsPanel, BorderLayout.CENTER);

		return serverPanel;
	}

	private JPanel getStartupPanel()
	{	
		//int horizStrut = 6;
		int vertStrut = 2;

		JPanel startupPanel = new JPanel();
		startupPanel.setBorder(BorderFactory.createTitledBorder(" Startup Options "));

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		
		startupPanel.setLayout(gbl);

		displayLogCheckBox = new JCheckBox("Display System Log");
		displaySplashCheckBox = new JCheckBox("Display Splash Screen");
		displayToolbarCheckBox = new JCheckBox("Display Toolbar");
		rollToolbarCheckBox = new JCheckBox("Rollover Toolbar");
		savePositionCheckBox = new JCheckBox("Save Window Size / Position");
		
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.weightx = 0.5;
		startupPanel.add(displaySplashCheckBox, gbc);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		startupPanel.add(rollToolbarCheckBox, gbc);

		gbc.weightx = 0;
		startupPanel.add(Box.createVerticalStrut(vertStrut), gbc);
		
		gbc.gridwidth = 1;
		gbc.weightx = 0.5;
		startupPanel.add(displayLogCheckBox, gbc);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		startupPanel.add(savePositionCheckBox, gbc);

		gbc.weightx = 0;
		startupPanel.add(Box.createVerticalStrut(vertStrut), gbc);

		gbc.gridwidth = 1;
		gbc.weightx = 0.5;
		startupPanel.add(displayToolbarCheckBox, gbc);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		startupPanel.add(new JLabel(" "), gbc);
		
		return startupPanel;		
	}

	private void refreshDefaultProperties()
	{
		if (!parent.isApplet())
		{		
			defaultServerComboBox.setSelectedItem(parent.getPropertyDefault(JavaPH.PROP_DEFAULT_SERVER));
			displayLogCheckBox.setSelected(parent.propertyDefaultEquals(JavaPH.PROP_DISPLAY_LOG, "true", "true"));
			displaySplashCheckBox.setSelected(parent.propertyDefaultEquals(JavaPH.PROP_DISPLAY_SPLASH, "true", "true"));
			displayToolbarCheckBox.setSelected(parent.propertyDefaultEquals(JavaPH.PROP_DISPLAY_TOOLBAR, "true", "true"));
			rollToolbarCheckBox.setSelected(parent.propertyDefaultEquals(JavaPH.PROP_ROLL_TOOLBAR, "true", "true"));
			savePositionCheckBox.setSelected(parent.propertyDefaultEquals(JavaPH.PROP_SAVE_POSITION, "true", "true"));

			loadFieldsManual.setSelected(parent.getIntPropertyDefault(JavaPH.PROP_LOAD_FIELDS, JavaPH.LOAD_FIELDS_DEF) == JavaPH.LOAD_FIELDS_MANUAL);
			loadFieldsSelected.setSelected(parent.getIntPropertyDefault(JavaPH.PROP_LOAD_FIELDS, JavaPH.LOAD_FIELDS_DEF) == JavaPH.LOAD_FIELDS_SELECTED);
			loadFieldsStartup.setSelected(parent.getIntPropertyDefault(JavaPH.PROP_LOAD_FIELDS, JavaPH.LOAD_FIELDS_DEF) == JavaPH.LOAD_FIELDS_STARTUP);
		}
		
		runtimeSlider.setValue(Integer.parseInt(parent.getPropertyDefault(JavaPH.PROP_QUERY_RUNTIME)));
	}

	private void refreshProperties()
	{
		if (!parent.isApplet())
		{		
			defaultServerComboBox.setSelectedItem(QiServer.getDefaultServer());
			displayLogCheckBox.setSelected(parent.propertyEquals(JavaPH.PROP_DISPLAY_LOG, "true", "true"));
			displaySplashCheckBox.setSelected(parent.propertyEquals(JavaPH.PROP_DISPLAY_SPLASH, "true", "true"));
			displayToolbarCheckBox.setSelected(parent.propertyEquals(JavaPH.PROP_DISPLAY_TOOLBAR, "true", "true"));
			rollToolbarCheckBox.setSelected(parent.propertyEquals(JavaPH.PROP_ROLL_TOOLBAR, "true", "true"));
			savePositionCheckBox.setSelected(parent.propertyEquals(JavaPH.PROP_SAVE_POSITION, "true", "true"));
			
			loadFieldsManual.setSelected(parent.getLoadFields() == JavaPH.LOAD_FIELDS_MANUAL);
			loadFieldsSelected.setSelected(parent.getLoadFields() == JavaPH.LOAD_FIELDS_SELECTED);
			loadFieldsStartup.setSelected(parent.getLoadFields() == JavaPH.LOAD_FIELDS_STARTUP);
		}
		
		runtimeSlider.setValue(parent.getQueryRuntime());
	}

	private void saveProperties()
	{
		if (!parent.isApplet())
		{
			int loadFields = JavaPH.LOAD_FIELDS_DEF;
			
			if (loadFieldsManual.isSelected())
				loadFields = JavaPH.LOAD_FIELDS_MANUAL;
			else if (loadFieldsSelected.isSelected())
				loadFields = JavaPH.LOAD_FIELDS_SELECTED;
			else if (loadFieldsStartup.isSelected())
				loadFields = JavaPH.LOAD_FIELDS_STARTUP;
			
			parent.setProperty(JavaPH.PROP_DEFAULT_SERVER, defaultServerComboBox.getSelectedItem().toString());
			parent.setProperty(JavaPH.PROP_DISPLAY_LOG, new Boolean(displayLogCheckBox.isSelected()).toString());
			parent.setProperty(JavaPH.PROP_DISPLAY_SPLASH, new Boolean(displaySplashCheckBox.isSelected()).toString());
			parent.setProperty(JavaPH.PROP_DISPLAY_TOOLBAR, new Boolean(displayToolbarCheckBox.isSelected()).toString());
			parent.setProperty(JavaPH.PROP_ROLL_TOOLBAR, new Boolean(rollToolbarCheckBox.isSelected()).toString());
			parent.setProperty(JavaPH.PROP_LOAD_FIELDS, "" + loadFields);
			parent.setProperty(JavaPH.PROP_QUERY_RUNTIME, "" + runtimeSlider.getValue());
			parent.setProperty(JavaPH.PROP_SAVE_POSITION, new Boolean(savePositionCheckBox.isSelected()).toString());

			QiServer.setDefaultServer(defaultServerComboBox.getSelectedItem().toString());
			parent.setLoadFields(loadFields);			
			parent.setSavePosition(savePositionCheckBox.isSelected());
		
			parent.storeProperties();
		}

		parent.setQueryRuntime(runtimeSlider.getValue());
	}

	public void show()
	{
		refreshProperties();
		super.show();
	}
}

