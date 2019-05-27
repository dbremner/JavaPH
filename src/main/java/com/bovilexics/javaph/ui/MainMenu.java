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

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit.CopyAction;
import javax.swing.text.DefaultEditorKit.CutAction;
import javax.swing.text.DefaultEditorKit.PasteAction;

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

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public final class MainMenu extends JMenuBar
{
	class StatusMouseListener implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
		}

		@Override
		public void mouseEntered(@NotNull MouseEvent e)
		{
			if (e.getSource() instanceof JMenuItem)
				parent.showStatus(((JMenuItem)e.getSource()).getActionCommand());
		}

		@Override
		public void mouseExited(MouseEvent e)
		{
			parent.showDefaultStatus();
		}

		@Override
		public void mousePressed(MouseEvent e)
		{
		}

		@Override
		public void mouseReleased(MouseEvent e)
		{
		}
	}

	@NotNull
	private final MouseListener mouseListener;
	
	@NotNull
    private final JavaPH parent;
	private JCheckBoxMenuItem rollToolBarItem;
	private JCheckBoxMenuItem showLogItem;
	private JCheckBoxMenuItem showToolBarItem;
	
	public MainMenu(@NotNull JavaPH javaph)
	{
		parent = javaph;

		mouseListener = new StatusMouseListener();
		addMouseListener(mouseListener);

		JMenu menu;
		
		menu = getFileComponent();
		menu.addMouseListener(mouseListener);
		add(menu);
		
		menu = getEditComponent();
		menu.addMouseListener(mouseListener);
		add(menu);
		
		menu = getWindowComponent();
		menu.addMouseListener(mouseListener);
		add(menu);

		menu = getHelpComponent();
		menu.addMouseListener(mouseListener);
		add(menu);
	}

	@NotNull
    private JMenu getEditComponent()
	{
		JMenuItem menuItem;

		@NotNull JMenu editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);

		menuItem = getNewMenuItem();
		menuItem.setAction(new CutAction());
		menuItem.setIcon(new ImageIcon(parent.getURL("img/cut.gif")));
		menuItem.setText("Cut");
		menuItem.setActionCommand("Cuts the selection and puts it on the clipboard");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK));
		menuItem.setMnemonic(KeyEvent.VK_T);
		editMenu.add(menuItem);

		menuItem = new JMenuItem("Copy");
		menuItem.setAction(new CopyAction());
		menuItem.setIcon(new ImageIcon(parent.getURL("img/copy.gif")));
		menuItem.setText("Copy");
		menuItem.setActionCommand("Copies the selection and puts it on the clipboard");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK));
		menuItem.setMnemonic(KeyEvent.VK_C);
		editMenu.add(menuItem);

		menuItem = getNewMenuItem();
		menuItem.setAction(new PasteAction());
		menuItem.setIcon(new ImageIcon(parent.getURL("img/paste.gif")));
		menuItem.setText("Paste");
		menuItem.setActionCommand("Inserts clipboard contents");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK));
		menuItem.setMnemonic(KeyEvent.VK_P);
		editMenu.add(menuItem);
		
		editMenu.addSeparator();
		
		menuItem = getNewMenuItem();
		menuItem.setAction(new FindAction(parent));
		menuItem.setActionCommand("Finds the specified text in the query results");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, Event.CTRL_MASK));
		menuItem.setMnemonic(KeyEvent.VK_F);
		editMenu.add(menuItem);
		
		return editMenu;
	}
	
	@NotNull
    private JMenu getFileComponent()
	{
		JMenuItem menuItem;

		@NotNull JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		
		menuItem = getNewMenuItem();
		menuItem.setAction(new NewAction(parent));
		menuItem.setActionCommand("Create a New Query");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK));
		menuItem.setMnemonic(KeyEvent.VK_N);
		fileMenu.add(menuItem);

		menuItem = getNewMenuItem();
		menuItem.setAction(new SaveAction(parent));
		menuItem.setActionCommand("Save a Query Results");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
		menuItem.setMnemonic(KeyEvent.VK_S);
		fileMenu.add(menuItem);

		fileMenu.addSeparator();

		menuItem = getNewMenuItem();
		menuItem.setAction(new ExitAction(parent));
		menuItem.setActionCommand("Exit JavaPH");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.ALT_MASK));
		menuItem.setMnemonic(KeyEvent.VK_X);
		fileMenu.add(menuItem);

		return fileMenu;
	}

	@NotNull
    private JMenu getHelpComponent()
	{
		JMenuItem menuItem;

		@NotNull JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);

		menuItem = getNewMenuItem();
		menuItem.setAction(new HelpAction(parent));
		menuItem.setActionCommand("Open JavaPH help window");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, Event.CTRL_MASK));
		menuItem.setMnemonic(KeyEvent.VK_H);
		helpMenu.add(menuItem);
		
		menuItem = getNewMenuItem();
		menuItem.setAction(new AboutAction(parent));
		menuItem.setActionCommand("Displays information about JavaPH");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.CTRL_MASK));
		menuItem.setMnemonic(KeyEvent.VK_A);
		helpMenu.add(menuItem);
		
		return helpMenu;
	}

	@NotNull
    private JMenuItem getNewMenuItem()
	{
		@NotNull JMenuItem menuItem = new JMenuItem();
		menuItem.addMouseListener(mouseListener);
		
		return menuItem;
	}

	@NotNull
    private JMenu getWindowComponent()
	{
		JMenuItem menuItem;

		@NotNull JMenu windowMenu = new JMenu("Window");
		windowMenu.setMnemonic(KeyEvent.VK_W);

		showLogItem = new JCheckBoxMenuItem("View System Log");
		showLogItem.addMouseListener(mouseListener);
		showLogItem.setActionCommand("Set visibility of system log");
		showLogItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, Event.CTRL_MASK));
		showLogItem.setMnemonic(KeyEvent.VK_L);
		showLogItem.setSelected(parent.propertyEquals(JavaPH.PROP_DISPLAY_LOG, "true", "true"));
		
		showLogItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(@NotNull ActionEvent ae)
			{
				// If the menu item was selected then just update
				// the toolbar display to reflect the change,
				// otherwise the accelerator key was used so we
				// need to update the state of the menu item first
				if (!(ae.getSource() instanceof JCheckBoxMenuItem))
				{
					showLogItem.setState(!showLogItem.isSelected());
				}
				parent.showLog(showLogItem.isSelected());
			}
		});
		
		windowMenu.add(showLogItem);

		showToolBarItem = new JCheckBoxMenuItem("View Toolbar");
		showToolBarItem.addMouseListener(mouseListener);
		showToolBarItem.setActionCommand("Set visibility of toolbar");
		showToolBarItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Event.CTRL_MASK));
		showToolBarItem.setMnemonic(KeyEvent.VK_T);
		showToolBarItem.setSelected(parent.propertyEquals(JavaPH.PROP_DISPLAY_TOOLBAR, "true", "true"));
		
		showToolBarItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(@NotNull ActionEvent ae)
			{
				// If the menu item was selected then just update
				// the toolbar display to reflect the change,
				// otherwise the accelerator key was used so we
				// need to update the state of the menu item first
				if (!(ae.getSource() instanceof JCheckBoxMenuItem))
				{
					showToolBarItem.setSelected(!showToolBarItem.getState());
				}
				parent.showToolBar(showToolBarItem.isSelected());
			}
		});
		
		windowMenu.add(showToolBarItem);

		rollToolBarItem = new JCheckBoxMenuItem("Rollover Toolbar");
		rollToolBarItem.addMouseListener(mouseListener);
		rollToolBarItem.setActionCommand("Set rollover property of toolbar");
		rollToolBarItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Event.CTRL_MASK));
		rollToolBarItem.setMnemonic(KeyEvent.VK_R);
		rollToolBarItem.setSelected(parent.propertyEquals(JavaPH.PROP_ROLL_TOOLBAR, "true", "true"));
		
		rollToolBarItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(@NotNull ActionEvent ae)
			{
				// If the menu item was selected then just update
				// the toolbar display to reflect the change,
				// otherwise the accelerator key was used so we
				// need to update the state of the menu item first
				if (!(ae.getSource() instanceof JCheckBoxMenuItem))
				{
					rollToolBarItem.setSelected(!rollToolBarItem.getState());
				}
				parent.getQueryToolBar().setRollover(rollToolBarItem.isSelected());
			}
		});
		
		windowMenu.add(rollToolBarItem);

		windowMenu.addSeparator();

		windowMenu.add(new LookAndFeelMenu(parent));

		menuItem = getNewMenuItem();
		menuItem.setAction(new BrowserAction(parent));
		menuItem.setActionCommand("Configure browser used to display JavaPH help");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, Event.CTRL_MASK));
		menuItem.setMnemonic(KeyEvent.VK_B);
		windowMenu.add(menuItem);

		menuItem = getNewMenuItem();
		menuItem.setAction(new PrefsAction(parent));
		menuItem.setActionCommand("Set JavaPH preferences");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK));
		menuItem.setMnemonic(KeyEvent.VK_P);
		windowMenu.add(menuItem);

		return windowMenu;
	}
}
