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

import com.bovilexics.javaph.FilePaths;
import com.bovilexics.javaph.JavaPH;
import com.bovilexics.javaph.JavaPHConstants;
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
	private final @NotNull JCheckBoxMenuItem rollToolBarItem = new JCheckBoxMenuItem(JavaPHConstants.ROLLOVER_TOOLBAR);
	private final @NotNull JCheckBoxMenuItem showLogItem = new JCheckBoxMenuItem(JavaPHConstants.VIEW_SYSTEM_LOG);
	private final @NotNull JCheckBoxMenuItem showToolBarItem = new JCheckBoxMenuItem(JavaPHConstants.VIEW_TOOLBAR);

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

		final @NotNull JMenu editMenu = new JMenu(JavaPHConstants.EDIT);
		editMenu.setMnemonic(KeyEvent.VK_E);

		final @NotNull JMenuItem cutItem = getNewMenuItem();
		cutItem.setAction(new CutAction());
		cutItem.setIcon(parent.getImageIcon(FilePaths.CUT_GIF));
		cutItem.setText(JavaPHConstants.CUT);
		cutItem.setActionCommand(JavaPHConstants.CUTS_THE_SELECTION_AND_PUTS_IT_ON_THE_CLIPBOARD);
		cutItem.setAccelerator(KeyStroke.getKeyStroke("ctrl X"));
		cutItem.setMnemonic(KeyEvent.VK_T);
		editMenu.add(cutItem);

		final @NotNull JMenuItem copyItem = new JMenuItem(JavaPHConstants.COPY);
		copyItem.setAction(new CopyAction());
		copyItem.setIcon(parent.getImageIcon(FilePaths.COPY_GIF));
		copyItem.setText(JavaPHConstants.COPY);
		copyItem.setActionCommand(JavaPHConstants.COPIES_THE_SELECTION_AND_PUTS_IT_ON_THE_CLIPBOARD);
		copyItem.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
		copyItem.setMnemonic(KeyEvent.VK_C);
		editMenu.add(copyItem);

		final @NotNull JMenuItem pasteItem = getNewMenuItem();
		pasteItem.setAction(new PasteAction());
		pasteItem.setIcon(parent.getImageIcon(FilePaths.PASTE_GIF));
		pasteItem.setText(JavaPHConstants.PASTE);
		pasteItem.setActionCommand(JavaPHConstants.INSERTS_CLIPBOARD_CONTENTS);
		pasteItem.setAccelerator(KeyStroke.getKeyStroke("ctrl V"));
		pasteItem.setMnemonic(KeyEvent.VK_P);
		editMenu.add(pasteItem);
		
		editMenu.addSeparator();

		final @NotNull JMenuItem findItem = getNewMenuItem();
		findItem.setAction(new FindAction(parent, parent::showFindDialog));
		findItem.setActionCommand(JavaPHConstants.FINDS_THE_SPECIFIED_TEXT_IN_THE_QUERY_RESULTS);
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
		newItem.setActionCommand(JavaPHConstants.CREATE_A_NEW_QUERY);
		newItem.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
		newItem.setMnemonic(KeyEvent.VK_N);
		fileMenu.add(newItem);

		final @NotNull JMenuItem saveItem = getNewMenuItem();
		saveItem.setAction(new SaveAction(parent));
		saveItem.setActionCommand(JavaPHConstants.SAVE_A_QUERY_RESULTS);
		saveItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
		saveItem.setMnemonic(KeyEvent.VK_S);
		fileMenu.add(saveItem);

		fileMenu.addSeparator();

		final @NotNull JMenuItem exitItem = getNewMenuItem();
		exitItem.setAction(new ExitAction(parent, () -> ((JFrame)parent.getDefaultPane().getTopLevelAncestor()).dispose()));
		exitItem.setActionCommand(JavaPHConstants.EXIT_INFO_NAME);
		exitItem.setAccelerator(KeyStroke.getKeyStroke("alt X"));
		exitItem.setMnemonic(KeyEvent.VK_X);
		fileMenu.add(exitItem);

		return fileMenu;
	}

	private @NotNull JMenu getHelpComponent()
	{

		final @NotNull JMenu helpMenu = new JMenu(JavaPHConstants.HELP);
		helpMenu.setMnemonic(KeyEvent.VK_H);

		final @NotNull JMenuItem helpItem = getNewMenuItem();
		helpItem.setAction(new HelpAction(parent));
		helpItem.setActionCommand(JavaPHConstants.OPEN_INFO_NAME_HELP_WINDOW);
		helpItem.setAccelerator(KeyStroke.getKeyStroke("ctrl H"));
		helpItem.setMnemonic(KeyEvent.VK_H);
		helpMenu.add(helpItem);

		final @NotNull JMenuItem aboutItem = getNewMenuItem();
		aboutItem.setAction(new AboutAction(parent, parent::showAboutDialog));
		aboutItem.setActionCommand(JavaPHConstants.DISPLAYS_INFORMATION_ABOUT_INFO_NAME);
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
		final @NotNull JMenu windowMenu = new JMenu(JavaPHConstants.WINDOW);
		windowMenu.setMnemonic(KeyEvent.VK_W);

		showLogItem.addMouseListener(mouseListener);
		showLogItem.setActionCommand(JavaPHConstants.SET_VISIBILITY_OF_SYSTEM_LOG);
		showLogItem.setAccelerator(KeyStroke.getKeyStroke("ctrl L"));
		showLogItem.setMnemonic(KeyEvent.VK_L);
		showLogItem.setSelected(parent.propertyEquals(PROP_DISPLAY_LOG,  true, true));
		
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

		showToolBarItem.addMouseListener(mouseListener);
		showToolBarItem.setActionCommand(JavaPHConstants.SET_VISIBILITY_OF_TOOLBAR);
		showToolBarItem.setAccelerator(KeyStroke.getKeyStroke("ctrl T"));
		showToolBarItem.setMnemonic(KeyEvent.VK_T);
		showToolBarItem.setSelected(parent.propertyEquals(PROP_DISPLAY_TOOLBAR,  true, true));
		
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

		rollToolBarItem.addMouseListener(mouseListener);
		rollToolBarItem.setActionCommand(JavaPHConstants.SET_ROLLOVER_PROPERTY_OF_TOOLBAR);
		rollToolBarItem.setAccelerator(KeyStroke.getKeyStroke("ctrl R"));
		rollToolBarItem.setMnemonic(KeyEvent.VK_R);
		rollToolBarItem.setSelected(parent.propertyEquals(PROP_ROLL_TOOLBAR,  true, true));
		
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

		final @NotNull JMenuItem browserItem = getNewMenuItem();
		browserItem.setAction(new BrowserAction(parent::showBrowserDialog));
		browserItem.setActionCommand(JavaPHConstants.CONFIGURE_BROWSER_USED_TO_DISPLAY_INFO_NAME_HELP);
		browserItem.setAccelerator(KeyStroke.getKeyStroke("ctrl B"));
		browserItem.setMnemonic(KeyEvent.VK_B);
		windowMenu.add(browserItem);

		final @NotNull JMenuItem propertiesItem = getNewMenuItem();
		propertiesItem.setAction(new PrefsAction(parent::showPropertiesDialog));
		propertiesItem.setActionCommand(JavaPHConstants.SET_INFO_NAME_PREFERENCES);
		propertiesItem.setAccelerator(KeyStroke.getKeyStroke("ctrl P"));
		propertiesItem.setMnemonic(KeyEvent.VK_P);
		windowMenu.add(propertiesItem);

		return windowMenu;
	}
}
