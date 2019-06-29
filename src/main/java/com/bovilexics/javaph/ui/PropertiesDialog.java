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
import com.bovilexics.javaph.qi.Server;
import com.bovilexics.javaph.qi.ServerManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.KeyEvent;

import static com.bovilexics.javaph.JavaPHConstants.INFO_NAME;
import static com.bovilexics.javaph.JavaPHConstants.PROP_DEFAULT_SERVER;
import static com.bovilexics.javaph.JavaPHConstants.PROP_DISPLAY_LOG;
import static com.bovilexics.javaph.JavaPHConstants.PROP_DISPLAY_SPLASH;
import static com.bovilexics.javaph.JavaPHConstants.PROP_DISPLAY_TOOLBAR;
import static com.bovilexics.javaph.JavaPHConstants.PROP_LOAD_FIELDS;
import static com.bovilexics.javaph.JavaPHConstants.PROP_QUERY_RUNTIME;
import static com.bovilexics.javaph.JavaPHConstants.PROP_ROLL_TOOLBAR;
import static com.bovilexics.javaph.JavaPHConstants.PROP_SAVE_POSITION;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public final class PropertiesDialog extends JavaPHDialog
{
	private static final double WEIGHTX = 0.5;
	private static final @NotNull String TRUE = "true";
	private final @NotNull JavaPH parent;

	private final @NotNull JCheckBox displayLogCheckBox = new JCheckBox(JavaPHConstants.DISPLAY_SYSTEM_LOG);
	private final @NotNull JCheckBox displaySplashCheckBox = new JCheckBox(JavaPHConstants.DISPLAY_SPLASH_SCREEN);
	private final @NotNull JCheckBox displayToolbarCheckBox = new JCheckBox(JavaPHConstants.DISPLAY_TOOLBAR);
	private final @NotNull JCheckBox rollToolbarCheckBox = new JCheckBox(JavaPHConstants.ROLLOVER_TOOLBAR);
	private final @NotNull JCheckBox savePositionCheckBox = new JCheckBox("Save Window Size / Position");

	private final @NotNull JComboBox<Server> defaultServerComboBox;

	private final @NotNull JRadioButton loadFieldsManual = new JRadioButton("Load Fields Manually");
	private final @NotNull JRadioButton loadFieldsSelected = new JRadioButton("Load Fields on Select");
	private final @NotNull JRadioButton loadFieldsStartup = new JRadioButton("Load Fields on Startup  ( Slow! )");

	private final @NotNull JSlider runtimeSlider = new JSlider(QueryRuntime.MIN.getValue(), QueryRuntime.MAX.getValue());
	
	private final @NotNull JTextField runtimeText = new JTextField();

	private final @NotNull ServerManager serverManager;

	public PropertiesDialog(final @NotNull JavaPH javaph)
	{
		super(javaph, String.format("%s Properties", INFO_NAME));

		parent = javaph;
		serverManager = parent.getServerManager();
		defaultServerComboBox = new JComboBox<>(serverManager.getServers());
		
		final Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		final @NotNull JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		contentPanel.setBorder(BorderFactory.createEtchedBorder());

		contentPanel.add(getStartupPanel(), BorderLayout.NORTH);

		contentPanel.add(getQueryPanel(), BorderLayout.CENTER);

		contentPanel.add(getServerPanel(), BorderLayout.SOUTH);
		
		contentPane.add(contentPanel, BorderLayout.CENTER);
		contentPane.add(getButtonPanel(), BorderLayout.SOUTH);

		pack();
	}
	
	private @NotNull JPanel getButtonPanel()
	{
		final @NotNull JPanel buttonPanel = new JPanel();

		final @NotNull JButton okButton = new JButton(JavaPHConstants.OK);
		okButton.setMnemonic(KeyEvent.VK_O);
		okButton.addActionListener(ae -> {
			saveProperties();
			dispose();
		});

		final @NotNull JButton applyButton = new JButton(JavaPHConstants.APPLY);
		applyButton.setMnemonic(KeyEvent.VK_A);
		applyButton.addActionListener(ae -> saveProperties());

		final @NotNull JButton cancelButton = new JButton(JavaPHConstants.CANCEL);
		cancelButton.setMnemonic(KeyEvent.VK_C);
		cancelButton.addActionListener(ae -> dispose());

		final @NotNull JButton defaultsButton = new JButton(JavaPHConstants.DEFAULTS);
		defaultsButton.setMnemonic(KeyEvent.VK_D);
		defaultsButton.addActionListener(ae -> refreshDefaultProperties());
	
		buttonPanel.add(okButton);
		buttonPanel.add(applyButton);
		buttonPanel.add(cancelButton);
		buttonPanel.add(new JLabel("      "));
		buttonPanel.add(defaultsButton);
		
		return buttonPanel;	
	}

	private @NotNull JPanel getQueryPanel()
	{
		final @NotNull JPanel queryPanel = new JPanel();
		queryPanel.setBorder(BorderFactory.createTitledBorder(" Query Runtime (Seconds) "));
		queryPanel.setLayout(new BorderLayout());
		
		final @NotNull JPanel runtimePanel = new JPanel();
		runtimePanel.setLayout(new FlowLayout());

		runtimeSlider.setMinorTickSpacing(10);
		runtimeSlider.setMajorTickSpacing(30);
		runtimeSlider.setPaintTicks(true);
		runtimeSlider.setPaintLabels(true);
		runtimeSlider.setSnapToTicks(true);
		runtimeSlider.setValue(parent.getQueryRuntime());
		runtimeSlider.addChangeListener(ce -> runtimeText.setText(String.valueOf(runtimeSlider.getValue())));

		runtimeText.setColumns(3);
		runtimeText.setEditable(false);
		runtimeText.setText(String.valueOf(runtimeSlider.getValue()));

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

	private @NotNull JPanel getServerPanel()
	{
		final @NotNull JPanel serverPanel = new JPanel();
		serverPanel.setBorder(BorderFactory.createTitledBorder(" Server Options "));
		serverPanel.setLayout(new BorderLayout());

		final @NotNull JPanel defaultServerPanel = new JPanel();
		defaultServerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		final @NotNull JLabel defaultServerLabel = new JLabel("Default Server : ");
		defaultServerLabel.setAlignmentY(JLabel.CENTER_ALIGNMENT);

		defaultServerComboBox.setRenderer(new ServerRenderer(parent));

		defaultServerPanel.add(defaultServerLabel);
		defaultServerPanel.add(defaultServerComboBox);

		loadFieldsManual.setToolTipText("Load fields for servers manually using the button here");

		loadFieldsSelected.setToolTipText("Load fields for servers only when selected from the server list");

		loadFieldsStartup.setToolTipText("Load fields for all servers when starting the application");

		final @NotNull ButtonGroup loadFieldsGroup = new ButtonGroup();
		loadFieldsGroup.add(loadFieldsManual);
		loadFieldsGroup.add(loadFieldsSelected);
		loadFieldsGroup.add(loadFieldsStartup);

		final @NotNull JPanel loadFieldsPanel = new JPanel();
		
		final @NotNull LayoutManager gbl = new GridBagLayout();
		final @NotNull GridBagConstraints gbc = new GridBagConstraints();
		
		loadFieldsPanel.setLayout(gbl);
		
		gbc.anchor = GridBagConstraints.NORTHWEST;
		final int horizontalStrut = 6;
		loadFieldsPanel.add(Box.createHorizontalStrut(horizontalStrut), gbc);
		loadFieldsPanel.add(loadFieldsManual, gbc);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		loadFieldsPanel.add(new JLabel(" "), gbc);
		
		gbc.weightx = 0;
		final int verticalStrut = 2;
		loadFieldsPanel.add(Box.createVerticalStrut(verticalStrut), gbc);
		
		gbc.gridwidth = 1;
		loadFieldsPanel.add(Box.createHorizontalStrut(horizontalStrut), gbc);
		loadFieldsPanel.add(loadFieldsSelected, gbc);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		loadFieldsPanel.add(new JLabel(" "), gbc);
		
		gbc.weightx = 0;
		loadFieldsPanel.add(Box.createVerticalStrut(verticalStrut), gbc);
		
		gbc.gridwidth = 1;
		loadFieldsPanel.add(Box.createHorizontalStrut(horizontalStrut), gbc);
		loadFieldsPanel.add(loadFieldsStartup, gbc);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		loadFieldsPanel.add(new JLabel(" "), gbc);

		serverPanel.add(defaultServerPanel, BorderLayout.NORTH);
		serverPanel.add(loadFieldsPanel, BorderLayout.CENTER);

		return serverPanel;
	}

	private @NotNull JPanel getStartupPanel()
	{	
		final @NotNull JPanel startupPanel = new JPanel();
		startupPanel.setBorder(BorderFactory.createTitledBorder(" Startup Options "));

		final @NotNull LayoutManager gbl = new GridBagLayout();
		final @NotNull GridBagConstraints gbc = new GridBagConstraints();
		
		startupPanel.setLayout(gbl);

		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.weightx = WEIGHTX;
		startupPanel.add(displaySplashCheckBox, gbc);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		startupPanel.add(rollToolbarCheckBox, gbc);

		gbc.weightx = 0;
		final int verticalStrut = 2;
		startupPanel.add(Box.createVerticalStrut(verticalStrut), gbc);
		
		gbc.gridwidth = 1;
		gbc.weightx = WEIGHTX;
		startupPanel.add(displayLogCheckBox, gbc);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		startupPanel.add(savePositionCheckBox, gbc);

		gbc.weightx = 0;
		startupPanel.add(Box.createVerticalStrut(verticalStrut), gbc);

		gbc.gridwidth = 1;
		gbc.weightx = WEIGHTX;
		startupPanel.add(displayToolbarCheckBox, gbc);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		startupPanel.add(new JLabel(" "), gbc);
		
		return startupPanel;		
	}

	private void refreshDefaultProperties()
	{
		defaultServerComboBox.setSelectedItem(parent.getPropertyDefault(PROP_DEFAULT_SERVER).get());
		displayLogCheckBox.setSelected(parent.propertyDefaultEquals(PROP_DISPLAY_LOG, TRUE, TRUE));
		displaySplashCheckBox.setSelected(parent.propertyDefaultEquals(PROP_DISPLAY_SPLASH, TRUE, TRUE));
		displayToolbarCheckBox.setSelected(parent.propertyDefaultEquals(PROP_DISPLAY_TOOLBAR, TRUE, TRUE));
		rollToolbarCheckBox.setSelected(parent.propertyDefaultEquals(PROP_ROLL_TOOLBAR, TRUE, TRUE));
		savePositionCheckBox.setSelected(parent.propertyDefaultEquals(PROP_SAVE_POSITION, TRUE, TRUE));

		final @NotNull LoadFields lf = parent.getIntPropertyDefault(PROP_LOAD_FIELDS, LoadFields.getDefault());
		loadFieldsManual.setSelected(lf == LoadFields.Manual);
		loadFieldsSelected.setSelected(lf == LoadFields.Selected);
		loadFieldsStartup.setSelected(lf == LoadFields.Startup);

		final @NotNull String queryRuntimeValue = parent.getPropertyDefault(PROP_QUERY_RUNTIME, String.valueOf(QueryRuntime.MIN.getValue()));
		runtimeSlider.setValue(Integer.parseInt(queryRuntimeValue));
	}

	private void refreshProperties()
	{
		defaultServerComboBox.setSelectedItem(serverManager.getDefaultServer());
		displayLogCheckBox.setSelected(parent.propertyEquals(PROP_DISPLAY_LOG,  true, true));
		displaySplashCheckBox.setSelected(parent.propertyEquals(PROP_DISPLAY_SPLASH,  true, true));
		displayToolbarCheckBox.setSelected(parent.propertyEquals(PROP_DISPLAY_TOOLBAR,  true, true));
		rollToolbarCheckBox.setSelected(parent.propertyEquals(PROP_ROLL_TOOLBAR,  true, true));
		savePositionCheckBox.setSelected(parent.propertyEquals(PROP_SAVE_POSITION,  true, true));

		final @NotNull LoadFields loadFields = parent.getLoadFields();
		loadFieldsManual.setSelected(loadFields == LoadFields.Manual);
		loadFieldsSelected.setSelected(loadFields == LoadFields.Selected);
		loadFieldsStartup.setSelected(loadFields == LoadFields.Startup);

		runtimeSlider.setValue(parent.getQueryRuntime());
	}

	private void saveProperties()
	{
		final @NotNull LoadFields loadFields;

		if (loadFieldsManual.isSelected())
		{
			loadFields = LoadFields.Manual;
		}
		else if (loadFieldsSelected.isSelected())
		{
			loadFields = LoadFields.Selected;
		}
		else if (loadFieldsStartup.isSelected())
		{
			loadFields = LoadFields.Startup;
		}
		else
		{
			loadFields = LoadFields.getDefault();
		}

		final @Nullable Object selectedItem = defaultServerComboBox.getSelectedItem();
		assert selectedItem != null;
		parent.setProperty(PROP_DEFAULT_SERVER, selectedItem.toString());
		parent.setProperty(PROP_DISPLAY_LOG, displayLogCheckBox.isSelected());
		parent.setProperty(PROP_DISPLAY_SPLASH, displaySplashCheckBox.isSelected());
		parent.setProperty(PROP_DISPLAY_TOOLBAR, displayToolbarCheckBox.isSelected());
		parent.setProperty(PROP_ROLL_TOOLBAR, rollToolbarCheckBox.isSelected());
		parent.setProperty(PROP_LOAD_FIELDS, loadFields.getValue());
		parent.setProperty(PROP_QUERY_RUNTIME, runtimeSlider.getValue());
		parent.setProperty(PROP_SAVE_POSITION, savePositionCheckBox.isSelected());

        serverManager.setDefaultServer(selectedItem.toString());
		parent.setLoadFields(loadFields);
		parent.setSavePosition(savePositionCheckBox.isSelected());

		parent.storeProperties();

		parent.setQueryRuntime(runtimeSlider.getValue());
	}

	@Override
	public void setVisible(final boolean b)
	{
		refreshProperties();
		super.setVisible(b);
	}
}

