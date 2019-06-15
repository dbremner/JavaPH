/*
 * Created on Apr 16, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package com.bovilexics.javaph.ui;

import com.bovilexics.javaph.qi.QiFieldState;
import com.bovilexics.javaph.qi.Server;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;
import java.awt.Component;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author Robert Fernandes
 * TODO fix interface type, this implements ListCellRenderer of Object
 */
public class ServerRenderer extends DefaultListCellRenderer
{
	private final @NotNull ImmutableMap<QiFieldState, Icon> map;
	
	public ServerRenderer(final @NotNull IconProvider javaph)
	{
		final @NotNull Map<QiFieldState, Icon> enumMap = new EnumMap<>(QiFieldState.class);
		enumMap.put(QiFieldState.FIELD_LOAD_ERROR, javaph.getImageIcon("img/field-error.gif"));
		enumMap.put(QiFieldState.FIELD_LOAD_FALSE, javaph.getImageIcon("img/field-false.gif"));
		enumMap.put(QiFieldState.FIELD_LOAD_TRUE, javaph.getImageIcon("img/field-true.gif"));
		enumMap.put(QiFieldState.SERVER_ERROR, javaph.getImageIcon("img/server-error.gif"));
		map = Maps.immutableEnumMap(enumMap);
	}
	
	@Override
    public @NotNull Component getListCellRendererComponent(final @NotNull JList list, final @NotNull Object value, final int index, final boolean isSelected, final boolean cellHasFocus)
	{
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

		final @NotNull Server server = (Server)value;
		final @NotNull QiFieldState state = server.getFieldState();
		setIcon(map.get(state));
		setText(server.toString());
		
		return this;
	}
}
