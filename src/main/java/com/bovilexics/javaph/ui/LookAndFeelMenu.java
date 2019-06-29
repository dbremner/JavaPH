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
import com.sun.java.swing.plaf.motif.MotifLookAndFeel;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;

//import com.sun.java.swing.plaf.mac.MacLookAndFeel;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
final class LookAndFeelMenu extends JMenu
{
	private static final @NotNull String MAC_LOOK_AND_FEEL = "com.sun.java.swing.plaf.mac.MacLookAndFeel";
	private static final @NotNull String METAL_LOOK_AND_FEEL = "javax.swing.plaf.metal.MetalLookAndFeel";
	private static final @NotNull String MOTIF_LOOK_AND_FEEL = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
	private static final @NotNull String WINDOWS_LOOK_AND_FEEL = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

	private boolean macSupported; //= new MacLookAndFeel().isSupportedLookAndFeel();
	private final boolean metalSupported = new MetalLookAndFeel().isSupportedLookAndFeel();
	private final boolean motifSupported = new MotifLookAndFeel().isSupportedLookAndFeel();
	private final boolean windowsSupported = new WindowsLookAndFeel().isSupportedLookAndFeel();

	private final @Nullable JMenuItem restoreDefaultItem;
	private final @Nullable JMenuItem storeDefaultItem;

	private @Nullable JRadioButtonMenuItem macItem = null;
	private @Nullable JRadioButtonMenuItem metalItem = null;
	private @Nullable JRadioButtonMenuItem motifItem = null;
	private @Nullable JRadioButtonMenuItem windowsItem = null;

	private final @NotNull JavaPH parent;

	private final class LookAndFeelItemListener implements ActionListener
	{
		@SuppressWarnings("WeakerAccess")
		LookAndFeelItemListener()
		{
		}

		@Override
		public void actionPerformed(final @NotNull ActionEvent e)
		{
			final @NotNull JMenuItem src = (JMenuItem) e.getSource();
		
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
	
	private final class LookAndFeelRadioListener implements ActionListener
	{
		@SuppressWarnings("WeakerAccess")
		LookAndFeelRadioListener()
		{
		}

		@Override
		public void actionPerformed(final @NotNull ActionEvent e)
		{
			final @NotNull JRadioButtonMenuItem src = (JRadioButtonMenuItem) e.getSource();
		
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

	LookAndFeelMenu(final @NotNull JavaPH javaph)
	{
		super("Look and Feel");
		
		parent = javaph;

		final @NotNull MouseListener mouseListener = new StatusMouseListener(parent);
		addMouseListener(mouseListener);

		final @NotNull ButtonGroup group = new ButtonGroup();

		final @NotNull ActionListener lookAndFeelItemListener = new LookAndFeelItemListener();
		final @NotNull ActionListener lookAndFeelRadioListener = new LookAndFeelRadioListener();

		setActionCommand("Change look and feel");
		setMnemonic(KeyEvent.VK_L);

		if (macSupported)
		{
			macItem = new JRadioButtonMenuItem(JavaPHConstants.MAC);
			group.add(macItem);
			macItem.addActionListener(lookAndFeelRadioListener);
			macItem.addMouseListener(mouseListener);
			macItem.setActionCommand(String.format(JavaPHConstants.CHANGE_TO_S_LOOK_AND_FEEL, JavaPHConstants.MAC));
			add(macItem);
		}

		if (metalSupported)
		{
			metalItem = new JRadioButtonMenuItem(JavaPHConstants.METAL);
			group.add(metalItem);
			metalItem.addActionListener(lookAndFeelRadioListener);
			metalItem.addMouseListener(mouseListener);
			metalItem.setActionCommand(String.format(JavaPHConstants.CHANGE_TO_S_LOOK_AND_FEEL, JavaPHConstants.METAL));
			add(metalItem);
		}

		if (motifSupported)
		{
			motifItem = new JRadioButtonMenuItem(JavaPHConstants.MOTIF);
			group.add(motifItem);
			motifItem.addActionListener(lookAndFeelRadioListener);
			motifItem.addMouseListener(mouseListener);
			motifItem.setActionCommand(String.format(JavaPHConstants.CHANGE_TO_S_LOOK_AND_FEEL, JavaPHConstants.MOTIF));
			add(motifItem);
		}

		if (windowsSupported)
		{
			windowsItem = new JRadioButtonMenuItem(JavaPHConstants.WINDOWS);
			group.add(windowsItem);
			windowsItem.addActionListener(lookAndFeelRadioListener);
			windowsItem.addMouseListener(mouseListener);
			windowsItem.setActionCommand(String.format(JavaPHConstants.CHANGE_TO_S_LOOK_AND_FEEL, JavaPHConstants.WINDOWS));
			add(windowsItem);
		}

		addSeparator();
		
		restoreDefaultItem = new JMenuItem(String.format("Restore %s", JavaPHConstants.DEFAULT));
		restoreDefaultItem.addActionListener(lookAndFeelItemListener);
		restoreDefaultItem.addMouseListener(mouseListener);
		restoreDefaultItem.setActionCommand(String.format(JavaPHConstants.CHANGE_TO_S_LOOK_AND_FEEL, JavaPHConstants.DEFAULT));
		restoreDefaultItem.setAccelerator(KeyStroke.getKeyStroke("ctrl R"));
		restoreDefaultItem.setMnemonic(KeyEvent.VK_R);
		add(restoreDefaultItem);

		storeDefaultItem = new JMenuItem(String.format(JavaPHConstants.SAVE_S, JavaPHConstants.DEFAULT));
		storeDefaultItem.addActionListener(lookAndFeelItemListener);
		storeDefaultItem.addMouseListener(mouseListener);
		storeDefaultItem.setActionCommand(String.format("Save look and feel as %s", JavaPHConstants.DEFAULT));
		storeDefaultItem.setAccelerator(KeyStroke.getKeyStroke("ctrl D"));
		storeDefaultItem.setMnemonic(KeyEvent.VK_S);
		add(storeDefaultItem);

		resetSelectedLookAndFeel();	
	}

	private void resetSelectedLookAndFeel()
	{
		if (macSupported) {
			assert macItem != null;
			macItem.setSelected(UIManager.getLookAndFeel().getClass().getName().equals(MAC_LOOK_AND_FEEL));
		}
		if (metalSupported) {
			assert metalItem != null;
			metalItem.setSelected(UIManager.getLookAndFeel().getClass().getName().equals(METAL_LOOK_AND_FEEL));
		}
		if (motifSupported) {
			assert motifItem != null;
			motifItem.setSelected(UIManager.getLookAndFeel().getClass().getName().equals(MOTIF_LOOK_AND_FEEL));
		}
		if (windowsSupported) {
			assert windowsItem != null;
			windowsItem.setSelected(UIManager.getLookAndFeel().getClass().getName().equals(WINDOWS_LOOK_AND_FEEL));
		}
	}
}
