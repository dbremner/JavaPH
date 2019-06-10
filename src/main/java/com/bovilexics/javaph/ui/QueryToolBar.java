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
import com.bovilexics.javaph.actions.FindAction;
import com.bovilexics.javaph.actions.HelpAction;
import com.bovilexics.javaph.actions.NewAction;
import com.bovilexics.javaph.actions.SaveAction;
import org.jetbrains.annotations.NotNull;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.text.DefaultEditorKit.CopyAction;
import javax.swing.text.DefaultEditorKit.CutAction;
import javax.swing.text.DefaultEditorKit.PasteAction;
import java.awt.Dimension;

import static com.bovilexics.javaph.JavaPHConstants.PROP_ROLL_TOOLBAR;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public final class QueryToolBar extends JToolBar
{

	public QueryToolBar(final @NotNull JavaPH javaph)
	{

		setFloatable(false);
		setFocusable(false);
		setOrientation(JToolBar.HORIZONTAL);
		setRollover(javaph.propertyEquals(PROP_ROLL_TOOLBAR, "true", "true"));

		addToolBarButton(new NewAction(javaph));
		addToolBarButton(new SaveAction(javaph));

		addFixedSeparator();
			
		addToolBarButton(new CutAction(), javaph.getImageIcon("img/cut.gif"));
		addToolBarButton(new CopyAction(), javaph.getImageIcon("img/copy.gif"));
		addToolBarButton(new PasteAction(), javaph.getImageIcon("img/paste.gif"));

		addFixedSeparator();
			
		addToolBarButton(new FindAction(javaph, javaph::showFindDialog));

		addFixedSeparator();
			
		addToolBarButton(new HelpAction(javaph));
	}

	private void addFixedSeparator()
	{
		final int sepHeight = 26;
		final int sepWidth = 13;
		addSeparator(new Dimension(sepWidth, sepHeight));
	}

	private JButton addToolBarButton(final Action action)
	{
		final JButton myJButton = add(action);
		myJButton.setBorder(BorderFactory.createRaisedBevelBorder());
		myJButton.setText(null);

		return myJButton;
	}
		
	private JButton addToolBarButton(final Action action, final @NotNull Icon icon)
	{
		final JButton myJButton = add(action);
		myJButton.setIcon(icon);
		myJButton.setBorder(BorderFactory.createRaisedBevelBorder());
		myJButton.setText(null);

		return myJButton;
	}
}
