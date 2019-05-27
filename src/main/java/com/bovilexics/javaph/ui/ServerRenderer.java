/*
 * Created on Apr 16, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package com.bovilexics.javaph.ui;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.bovilexics.javaph.JavaPH;
import com.bovilexics.javaph.qi.QiServer;
import org.jetbrains.annotations.NotNull;

/**
 * @author Robert Fernandes
 */
public class ServerRenderer extends DefaultListCellRenderer implements ListCellRenderer
{
	private ImageIcon[] icons;
	
	public ServerRenderer(@NotNull JavaPH javaph)
	{
		icons = new ImageIcon[]
	   {
		   new ImageIcon(javaph.getURL("img/field-error.gif")),
		   new ImageIcon(javaph.getURL("img/field-false.gif")),
		   new ImageIcon(javaph.getURL("img/field-true.gif")),
		   new ImageIcon(javaph.getURL("img/server-error.gif"))
	   };
	}
	
	@NotNull
	public Component getListCellRendererComponent(@NotNull JList list, @NotNull Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

		setIcon(icons[((QiServer)value).getFieldState()]);
		setText(value.toString());
		
		return this;
	}
}
