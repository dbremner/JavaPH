/*
 * Created on Apr 16, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package com.bovilexics.javaph.ui;

import com.bovilexics.javaph.JavaPH;
import com.bovilexics.javaph.qi.QiServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
	@Nullable
	private final ImageIcon[] icons;
	
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
	
	@Override
	@NotNull
	public Component getListCellRendererComponent(@NotNull JList list, @NotNull Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

		setIcon(icons[((QiServer)value).getFieldState().getValue()]);
		setText(value.toString());
		
		return this;
	}
}
