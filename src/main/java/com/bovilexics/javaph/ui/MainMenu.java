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
import com.bovilexics.javaph.actions.AboutAction;
import com.bovilexics.javaph.actions.BrowserAction;
import com.bovilexics.javaph.actions.ExitAction;
import com.bovilexics.javaph.actions.FindAction;
import com.bovilexics.javaph.actions.HelpAction;
import com.bovilexics.javaph.actions.NewAction;
import com.bovilexics.javaph.actions.PrefsAction;
import com.bovilexics.javaph.actions.SaveAction;
import org.jetbrains.annotations.NotNull;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit.CopyAction;
import javax.swing.text.DefaultEditorKit.CutAction;
import javax.swing.text.DefaultEditorKit.PasteAction;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;

import static com.bovilexics.javaph.JavaPHConstants.PROP_DISPLAY_LOG;
import static com.bovilexics.javaph.JavaPHConstants.PROP_DISPLAY_TOOLBAR;
import static com.bovilexics.javaph.JavaPHConstants.PROP_ROLL_TOOLBAR;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public final class MainMenu extends JMenuBar
{

	private final @NotNull MouseListener mouseListener;
	
	private final @NotNull JavaPH parent;
	private JCheckBoxMenuItem rollToolBarItem;
	private JCheckBoxMenuItem showLogItem;
	private JCheckBoxMenuItem showToolBarItem;
	
	public MainMenu(final @NotNull JavaPH javaph)
	{
		parent = javaph;

		mouseListener = new StatusMouseListener(parent);
		addMouseListener(mouseListener);

		final @NotNull JMenu fileMenu = getFileComponent();
		fileMenu.addMouseListener(mouseListener);
		add(fileMenu);

		final @NotNull JMenu editMenu = getEditComponent();
		editMenu.addMouseListener(mouseListener);
		add(editMenu);

		final @NotNull JMenu windowMenu = getWindowComponent();
		windowMenu.addMouseListener(mouseListener);
		add(windowMenu);

		final @NotNull JMenu helpMenu = getHelpComponent();
		helpMenu.addMouseListener(mouseListener);
		add(helpMenu);
	}

	private @NotNull JMenu getEditComponent()
	{

		final @NotNull JMenu editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);

		final @NotNull JMenuItem cutItem = getNewMenuItem();
		cutItem.setAction(new CutAction());
		cutItem.setIcon(parent.getImageIcon("img/cut.gif"));
		cutItem.setText("Cut");
		cutItem.setActionCommand("Cuts the selection and puts it on the clipboard");
		cutItem.setAccelerator(KeyStroke.getKeyStroke("ctrl X"));
		cutItem.setMnemonic(KeyEvent.VK_T);
		editMenu.add(cutItem);

		final @NotNull JMenuItem copyItem = new JMenuItem("Copy");
		copyItem.setAction(new CopyAction());
		copyItem.setIcon(parent.getImageIcon("img/copy.gif"));
		copyItem.setText("Copy");
		copyItem.setActionCommand("Copies the selection and puts it on the clipboard");
		copyItem.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
		copyItem.setMnemonic(KeyEvent.VK_C);
		editMenu.add(copyItem);

		final @NotNull JMenuItem pasteItem = getNewMenuItem();
		pasteItem.setAction(new PasteAction());
		pasteItem.setIcon(parent.getImageIcon("img/paste.gif"));
		pasteItem.setText("Paste");
		pasteItem.setActionCommand("Inserts clipboard contents");
		pasteItem.setAccelerator(KeyStroke.getKeyStroke("ctrl V"));
		pasteItem.setMnemonic(KeyEvent.VK_P);
		editMenu.add(pasteItem);
		
		editMenu.addSeparator();

		final @NotNull JMenuItem findItem = getNewMenuItem();
		findItem.setAction(new FindAction(parent, parent::showFindDialog));
		findItem.setActionCommand("Finds the specified text in the query results");
		findItem.setAccelerator(KeyStroke.getKeyStroke("ctrl F"));
		findItem.setMnemonic(KeyEvent.VK_F);
		editMenu.add(findItem);
		
		return editMenu;
	}
	
	private @NotNull JMenu getFileComponent()
	{

		final @NotNull JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		final @NotNull JMenuItem newItem = getNewMenuItem();
		newItem.setAction(new NewAction(parent));
		newItem.setActionCommand("Create a New Query");
		newItem.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
		newItem.setMnemonic(KeyEvent.VK_N);
		fileMenu.add(newItem);

		final @NotNull JMenuItem saveItem = getNewMenuItem();
		saveItem.setAction(new SaveAction(parent));
		saveItem.setActionCommand("Save a Query Results");
		saveItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
		saveItem.setMnemonic(KeyEvent.VK_S);
		fileMenu.add(saveItem);

		fileMenu.addSeparator();

		final @NotNull JMenuItem exitItem = getNewMenuItem();
		exitItem.setAction(new ExitAction(parent, () -> ((JFrame)parent.getDefaultPane().getTopLevelAncestor()).dispose()));
		exitItem.setActionCommand("Exit JavaPH");
		exitItem.setAccelerator(KeyStroke.getKeyStroke("alt X"));
		exitItem.setMnemonic(KeyEvent.VK_X);
		fileMenu.add(exitItem);

		return fileMenu;
	}

	private @NotNull JMenu getHelpComponent()
	{

		final @NotNull JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);

		final @NotNull JMenuItem helpItem = getNewMenuItem();
		helpItem.setAction(new HelpAction(parent));
		helpItem.setActionCommand("Open JavaPH help window");
		helpItem.setAccelerator(KeyStroke.getKeyStroke("ctrl H"));
		helpItem.setMnemonic(KeyEvent.VK_H);
		helpMenu.add(helpItem);

		final @NotNull JMenuItem aboutItem = getNewMenuItem();
		aboutItem.setAction(new AboutAction(parent, parent::showAboutDialog));
		aboutItem.setActionCommand("Displays information about JavaPH");
		aboutItem.setAccelerator(KeyStroke.getKeyStroke("ctrl A"));
		aboutItem.setMnemonic(KeyEvent.VK_A);
		helpMenu.add(aboutItem);
		
		return helpMenu;
	}

	private @NotNull JMenuItem getNewMenuItem()
	{
		final @NotNull JMenuItem menuItem = new JMenuItem();
		menuItem.addMouseListener(mouseListener);
		
		return menuItem;
	}

	private @NotNull JMenu getWindowComponent()
	{
		final @NotNull JMenu windowMenu = new JMenu("Window");
		windowMenu.setMnemonic(KeyEvent.VK_W);

		showLogItem = new JCheckBoxMenuItem("View System Log");
		showLogItem.addMouseListener(mouseListener);
		showLogItem.setActionCommand("Set visibility of system log");
		showLogItem.setAccelerator(KeyStroke.getKeyStroke("ctrl L"));
		showLogItem.setMnemonic(KeyEvent.VK_L);
		showLogItem.setSelected(parent.propertyEquals(PROP_DISPLAY_LOG, "true", "true"));
		
		showLogItem.addActionListener(ae -> {
			// If the menu item was selected then just update
			// the toolbar display to reflect the change,
			// otherwise the accelerator key was used so we
			// need to update the state of the menu item first
			if (!(ae.getSource() instanceof JCheckBoxMenuItem))
			{
				showLogItem.setState(!showLogItem.isSelected());
			}
			parent.showLog(showLogItem.isSelected());
		});
		
		windowMenu.add(showLogItem);

		showToolBarItem = new JCheckBoxMenuItem("View Toolbar");
		showToolBarItem.addMouseListener(mouseListener);
		showToolBarItem.setActionCommand("Set visibility of toolbar");
		showToolBarItem.setAccelerator(KeyStroke.getKeyStroke("ctrl T"));
		showToolBarItem.setMnemonic(KeyEvent.VK_T);
		showToolBarItem.setSelected(parent.propertyEquals(PROP_DISPLAY_TOOLBAR, "true", "true"));
		
		showToolBarItem.addActionListener(ae -> {
			// If the menu item was selected then just update
			// the toolbar display to reflect the change,
			// otherwise the accelerator key was used so we
			// need to update the state of the menu item first
			if (!(ae.getSource() instanceof JCheckBoxMenuItem))
			{
				showToolBarItem.setSelected(!showToolBarItem.getState());
			}
			parent.showToolBar(showToolBarItem.isSelected());
		});
		
		windowMenu.add(showToolBarItem);

		rollToolBarItem = new JCheckBoxMenuItem("Rollover Toolbar");
		rollToolBarItem.addMouseListener(mouseListener);
		rollToolBarItem.setActionCommand("Set rollover property of toolbar");
		rollToolBarItem.setAccelerator(KeyStroke.getKeyStroke("ctrl R"));
		rollToolBarItem.setMnemonic(KeyEvent.VK_R);
		rollToolBarItem.setSelected(parent.propertyEquals(PROP_ROLL_TOOLBAR, "true", "true"));
		
		rollToolBarItem.addActionListener(ae -> {
			// If the menu item was selected then just update
			// the toolbar display to reflect the change,
			// otherwise the accelerator key was used so we
			// need to update the state of the menu item first
			if (!(ae.getSource() instanceof JCheckBoxMenuItem))
			{
				rollToolBarItem.setSelected(!rollToolBarItem.getState());
			}
			parent.getQueryToolBar().setRollover(rollToolBarItem.isSelected());
		});
		
		windowMenu.add(rollToolBarItem);

		windowMenu.addSeparator();

		windowMenu.add(new LookAndFeelMenu(parent));

		final @NotNull JMenuItem browserItem = getNewMenuItem();
		browserItem.setAction(new BrowserAction(parent::showBrowserDialog));
		browserItem.setActionCommand("Configure browser used to display JavaPH help");
		browserItem.setAccelerator(KeyStroke.getKeyStroke("ctrl B"));
		browserItem.setMnemonic(KeyEvent.VK_B);
		windowMenu.add(browserItem);

		final @NotNull JMenuItem propertiesItem = getNewMenuItem();
		propertiesItem.setAction(new PrefsAction(parent::showPropertiesDialog));
		propertiesItem.setActionCommand("Set JavaPH preferences");
		propertiesItem.setAccelerator(KeyStroke.getKeyStroke("ctrl P"));
		propertiesItem.setMnemonic(KeyEvent.VK_P);
		windowMenu.add(propertiesItem);

		return windowMenu;
	}
}
