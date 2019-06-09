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
package com.bovilexics.javaph.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public final class FindComboBoxModel extends AbstractListModel implements MutableComboBoxModel, Serializable
{
	private @Nullable Object selectedObject;

	private final @NotNull List<Object> allObjects;
	private final @NotNull List<Object> objects;

	/**
	 * Constructs an empty DefaultComboBoxModel object.
	 */
	public FindComboBoxModel()
	{
		objects = new ArrayList<>();
		allObjects = new ArrayList<>();
	}

	// implements javax.swing.MutableComboBoxModel
	/**
	 * Adds elements to the beginning of the collection
	 *
	 */
	@Override
	public void addElement(final Object item)
	{
		if (!objects.contains(item)) {
			insertElementAt(item, 0);
		}
	}

	// implements javax.swing.ListModel
	@Override
	public @Nullable Object getElementAt(final int index)
	{
		if (index >= 0 && index < objects.size()) {
			return objects.get(index);
		} else {
			return null;
		}
	}

	// implements javax.swing.ComboBoxModel
	@Override
	public @Nullable Object getSelectedItem()
	{
		return selectedObject;
	}

	// implements javax.swing.ListModel
	@Override
	public int getSize()
	{
		return objects.size();
	}

	// implements javax.swing.MutableComboBoxModel
	@Override
	public void insertElementAt(final Object item, final int index)
	{
		objects.add(index, item);
		fireIntervalAdded(this, index, index);

		if (!allObjects.contains(item)) {
			allObjects.add(index, item);
		}
	}

	// implements javax.swing.MutableComboBoxModel
	@Override
	public void removeElement(final Object obj)
	{
		final int index = objects.indexOf(obj);

		if (index != -1) {
			removeElementAt(index);
		}
	}

	// implements javax.swing.MutableComboBoxModel
	@Override
	public void removeElementAt(final int index)
	{
		if (getElementAt(index) == selectedObject)
		{
			if (index == 0) {
				setSelectedItem(getSize() == 1 ? null : getElementAt(index + 1));
			} else {
				setSelectedItem(getElementAt(index - 1));
			}
		}
		objects.remove(index);
		fireIntervalRemoved(this, index, index);
	}

	// implements javax.swing.ComboBoxModel
	/**
	 * Set the value of the selected item. The selected item may be null.
	 * <p>
	 * @param anItem The combo box value or null for no selection.
	 */
	@Override
	public void setSelectedItem(final @Nullable Object anItem)
	{
		if ((selectedObject != null && !selectedObject.equals(anItem))
			|| selectedObject == null
			&& anItem != null)
		{
			selectedObject = anItem;
			fireContentsChanged(this, -1, -1);
		}
	}
}
