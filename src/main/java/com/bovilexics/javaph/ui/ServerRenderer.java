/*
 * Created on Apr 16, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package com.bovilexics.javaph.ui;

import com.bovilexics.javaph.qi.Server;
import org.jetbrains.annotations.NotNull;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import java.awt.Component;

/**
 * @author Robert Fernandes
 * TODO fix interface type, this implements ListCellRenderer of Object
 */
public class ServerRenderer extends DefaultListCellRenderer
{
	private final @NotNull ImageIcon[] icons;
	
	public ServerRenderer(final @NotNull IconProvider javaph)
	{
		icons = new ImageIcon[]
	   {
		   javaph.getImageIcon("img/field-error.gif"),
		   javaph.getImageIcon("img/field-false.gif"),
		   javaph.getImageIcon("img/field-true.gif"),
		   javaph.getImageIcon("img/server-error.gif")
	   };
	}
	
	@Override
    public @NotNull Component getListCellRendererComponent(final @NotNull JList list, final @NotNull Object value, final int index, final boolean isSelected, final boolean cellHasFocus)
	{
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

		setIcon(icons[((Server)value).getFieldState().getValue()]);
		setText(value.toString());
		
		return this;
	}
}
