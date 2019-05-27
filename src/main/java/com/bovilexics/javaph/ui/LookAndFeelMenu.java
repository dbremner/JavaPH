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

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;

import com.bovilexics.javaph.JavaPH;
import com.sun.java.swing.plaf.mac.MacLookAndFeel;
import com.sun.java.swing.plaf.motif.MotifLookAndFeel;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public class LookAndFeelMenu extends JMenu
{
	private static final String MAC_LOOK_AND_FEEL = "com.sun.java.swing.plaf.mac.MacLookAndFeel";
	private static final String METAL_LOOK_AND_FEEL = "javax.swing.plaf.metal.MetalLookAndFeel";
	private static final String MOTIF_LOOK_AND_FEEL = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
	private static final String WINDOWS_LOOK_AND_FEEL = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

	private boolean macSupported = new MacLookAndFeel().isSupportedLookAndFeel();
	private boolean metalSupported = new MetalLookAndFeel().isSupportedLookAndFeel();
	private boolean motifSupported = new MotifLookAndFeel().isSupportedLookAndFeel();
	private boolean windowsSupported = new WindowsLookAndFeel().isSupportedLookAndFeel();

	private JMenuItem restoreDefaultItem = null;
	private JMenuItem storeDefaultItem = null;

	private JRadioButtonMenuItem macItem = null;
	private JRadioButtonMenuItem metalItem = null;
	private JRadioButtonMenuItem motifItem = null;
	private JRadioButtonMenuItem windowsItem = null;

	private JavaPH parent = null;
	
	private MouseListener mouseListener = null;

	class LookAndFeelItemListener implements ActionListener
	{
		public void actionPerformed(ActionEvent ae)
		{
			JMenuItem src = (JMenuItem) ae.getSource();
		
			if (src == restoreDefaultItem)
			{
				parent.restoreLookAndFeel();
				resetSelectedLookAndFeel();
			}
			else if (src == storeDefaultItem)
			{
				parent.storeLookAndFeel();
			}
		}
	}
	
	class LookAndFeelRadioListener implements ActionListener
	{
		public void actionPerformed(ActionEvent ae)
		{
			JRadioButtonMenuItem src = (JRadioButtonMenuItem) ae.getSource();
		
			if (macSupported && src == macItem)
			{
				parent.restoreLookAndFeel(MAC_LOOK_AND_FEEL);
			}
			else if (metalSupported && src == metalItem)
			{
				parent.restoreLookAndFeel(METAL_LOOK_AND_FEEL);
			}
			else if (motifSupported && src == motifItem)
			{
				parent.restoreLookAndFeel(MOTIF_LOOK_AND_FEEL);
			}
			else if (windowsSupported && src == windowsItem)
			{
				parent.restoreLookAndFeel(WINDOWS_LOOK_AND_FEEL);
			}
		}
	}

	class StatusMouseListener implements MouseListener
	{
		public void mouseClicked(MouseEvent e)
		{
		}

		public void mouseEntered(MouseEvent e)
		{
			if (e.getSource() instanceof JMenuItem)
				parent.showStatus(((JMenuItem)e.getSource()).getActionCommand());
		}

		public void mouseExited(MouseEvent e)
		{
			parent.showDefaultStatus();
		}

		public void mousePressed(MouseEvent e)
		{
		}

		public void mouseReleased(MouseEvent e)
		{
		}
	}

	public LookAndFeelMenu(JavaPH javaph)
	{
		super("Look and Feel");
		
		if (javaph == null)
			throw new IllegalArgumentException("Error: null JavaPH value passed into LookAndFeelMenu");	

		parent = javaph;

		addMouseListener(getMouseListener());

		ButtonGroup group = new ButtonGroup();

		ActionListener lookAndFeelItemListener = new LookAndFeelItemListener();
		ActionListener lookAndFeelRadioListener = new LookAndFeelRadioListener();

		setActionCommand("Change look and feel");
		setMnemonic(KeyEvent.VK_L);

		if (macSupported)
		{
			macItem = new JRadioButtonMenuItem("Mac");
			group.add(macItem);
			macItem.addActionListener(lookAndFeelRadioListener);
			macItem.addMouseListener(getMouseListener());
			macItem.setActionCommand("Change to Mac look and feel");
			add(macItem);
		}

		if (metalSupported)
		{
			metalItem = new JRadioButtonMenuItem("Metal");
			group.add(metalItem);
			metalItem.addActionListener(lookAndFeelRadioListener);
			metalItem.addMouseListener(getMouseListener());
			metalItem.setActionCommand("Change to Metal look and feel");
			add(metalItem);
		}

		if (motifSupported)
		{
			motifItem = new JRadioButtonMenuItem("Motif");
			group.add(motifItem);
			motifItem.addActionListener(lookAndFeelRadioListener);
			motifItem.addMouseListener(getMouseListener());
			motifItem.setActionCommand("Change to Motif look and feel");
			add(motifItem);
		}

		if (windowsSupported)
		{
			windowsItem = new JRadioButtonMenuItem("Windows");		
			group.add(windowsItem);
			windowsItem.addActionListener(lookAndFeelRadioListener);
			windowsItem.addMouseListener(getMouseListener());
			windowsItem.setActionCommand("Change to Windows look and feel");
			add(windowsItem);
		}

		addSeparator();
		
		restoreDefaultItem = new JMenuItem("Restore Default");
		restoreDefaultItem.addActionListener(lookAndFeelItemListener);
		restoreDefaultItem.addMouseListener(getMouseListener());
		restoreDefaultItem.setActionCommand("Change to Default look and feel");
		restoreDefaultItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Event.CTRL_MASK));
		restoreDefaultItem.setMnemonic(KeyEvent.VK_R);
		add(restoreDefaultItem);

		if (!parent.isApplet())
		{
			storeDefaultItem = new JMenuItem("Save Default");
			storeDefaultItem.addActionListener(lookAndFeelItemListener);
			storeDefaultItem.addMouseListener(getMouseListener());
			storeDefaultItem.setActionCommand("Save look and feel as Default");
			storeDefaultItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, Event.CTRL_MASK));
			storeDefaultItem.setMnemonic(KeyEvent.VK_S);
			add(storeDefaultItem);		
		}
		
		resetSelectedLookAndFeel();	
	}

	private MouseListener getMouseListener()
	{
		if (mouseListener == null)
			mouseListener = new StatusMouseListener();
			
		return mouseListener;
	}

	private void resetSelectedLookAndFeel()
	{
		if (macSupported)
			macItem.setSelected(UIManager.getLookAndFeel().getClass().getName().equals(MAC_LOOK_AND_FEEL));	
		if (metalSupported)
			metalItem.setSelected(UIManager.getLookAndFeel().getClass().getName().equals(METAL_LOOK_AND_FEEL));
		if (motifSupported)	
			motifItem.setSelected(UIManager.getLookAndFeel().getClass().getName().equals(MOTIF_LOOK_AND_FEEL));
		if (windowsSupported)
			windowsItem.setSelected(UIManager.getLookAndFeel().getClass().getName().equals(WINDOWS_LOOK_AND_FEEL));
	}
}
