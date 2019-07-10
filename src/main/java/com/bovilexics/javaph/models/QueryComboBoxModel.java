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

import org.jetbrains.annotations.Contract;
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
 * TODO change type to Strings
 */
public final class QueryComboBoxModel extends AbstractListModel<Object> implements MutableComboBoxModel<Object>, Serializable
{
	private @Nullable Object selectedObject;

	private final @NotNull List<Object> allObjects;
	private final @NotNull List<Object> objects;

	/**
	 * Constructs an empty DefaultComboBoxModel object.
	 */
	public QueryComboBoxModel()
	{
		objects = new ArrayList<>();
		allObjects = new ArrayList<>();
	}

	// implements javax.swing.MutableComboBoxModel
	/**
	 * Adds elements in sorted string order
	 *
	 * @param item an item
	 */
	@Override
    public void addElement(final @Nullable Object item)
	{
        if (item == null) {
            return;
        }

        if (getSize() == 0)
		{
			insertElementAt(item, 0);
			return;
		}

		int whereToAdd = -1;
		final String newElement = item.toString();

		for (int i = 0; i < objects.size(); i++)
		{
			final String oldElement = objects.get(i).toString();

			final int compare = newElement.compareTo(oldElement);
			if (compare == 0)
			{
				return;
			}
			else if (compare < 0)
			{
				whereToAdd = i;
				break;
			}
		}

		if (whereToAdd == -1) {
			whereToAdd = getSize();
		}
		insertElementAt(item, whereToAdd);
	}

	public void filterElements(final @Nullable String filter)
	{

        if (filter == null || filter.isEmpty())
		{
			restoreAllElements();
		}
		else
		{
			removeAllElements();

			for (final @Nullable Object anObject : allObjects)
			{
				if (anObject == null)
				{
					continue;
				}

				final String element = anObject.toString();

				if (element.equals(filter) || element.startsWith(filter))
				{
					addElement(anObject);
				}
			}
		}
		
		if (!objects.isEmpty()) {
            selectedObject = objects.get(0);
        }
	}

	// implements javax.swing.ListModel
	@Override
	public @Nullable Object getElementAt(final int index)
	{
		if (index < 0 || index >= objects.size())
		{
			return null;
		}

		return objects.get(index);
	}

	// implements javax.swing.ComboBoxModel
	@Override
	public @Nullable Object getSelectedItem()
	{
		return selectedObject;
	}

	// implements javax.swing.ListModel
	@Override
	@Contract(pure = true)
    public int getSize()
	{
		return objects.size();
	}

	/**
	 * Returns the index-position of the specified object in the list.
	 *
	 * @param anObject an object
	 * @return an int representing the index position, where 0 is 
	 *         the first position
	 */
	@Contract(pure = true)
	public int getIndexOf(final @Nullable Object anObject)
	{
		return objects.indexOf(anObject);
	}

	// implements javax.swing.MutableComboBoxModel
	@Override
    public void insertElementAt(final @NotNull Object item, final int index)
	{
		objects.add(index, item);
		fireIntervalAdded(this, index, index);
		
		if (!allObjects.contains(item)) {
            allObjects.add(index, item);
        }
	}

	/**
	 * Empties the list.
	 */
	private void removeAllElements()
	{
		if (objects.isEmpty())
		{
			return;
		}
		final int lastIndex = objects.size() - 1;
		objects.clear();
		selectedObject = null;
		fireIntervalRemoved(this, 0, lastIndex);
	}

	// implements javax.swing.MutableComboBoxModel
	@Override
    public void removeElement(final @NotNull Object obj)
	{
		final int index = objects.indexOf(obj);

		if (index == -1)
		{
			return;
		}
		removeElementAt(index);
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

	private void restoreAllElements()
	{
		removeAllElements();

		for (final Object allObject : allObjects)
		{
			addElement(allObject);
		}
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
