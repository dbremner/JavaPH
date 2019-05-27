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

import java.io.Serializable;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public class QueryComboBoxModel extends AbstractListModel implements MutableComboBoxModel, Serializable
{
	Object selectedObject;

	Vector allObjects;
	Vector objects;

	/**
	 * Constructs an empty DefaultComboBoxModel object.
	 */
	public QueryComboBoxModel()
	{
		objects = new Vector();
		allObjects = (Vector)objects.clone();
	}

	/**
	 * Constructs a DefaultComboBoxModel object initialized
	 * with an array of objects.
	 *
	 * @param items  an array of Object objects
	 */
	public QueryComboBoxModel(final Object items[])
	{
		objects = new Vector();
		objects.ensureCapacity( items.length );

		int i,c;

		for (i = 0, c = items.length; i < c ; i++)
			objects.addElement(items[i]);

		if (getSize() > 0)
			selectedObject = getElementAt(0);
			
		allObjects = (Vector)objects.clone();
	}

	/**
	 * Constructs a DefaultComboBoxModel object initialized
	 * with a vector.
	 *
	 * @param v  a Vector object ...
	 */
	public QueryComboBoxModel(Vector v)
	{
		objects = v;

		if (getSize() > 0)
			selectedObject = getElementAt(0);
			
		allObjects = (Vector)objects.clone();
	}

	// implements javax.swing.MutableComboBoxModel
	/**
	 * Adds elements in sorted string order
	 *
	 * @param anObject
	 */
	public void addElement(Object anObject)
	{
		int whereToAdd = -1;
		
		if (anObject == null)
			return;

		if (getSize() == 0)
		{
			whereToAdd = 0;
		}
		else
		{
			String newElement = anObject.toString();
			String oldElement;

			for (int i = 0; i < getSize(); i++)
			{
				oldElement = getElementAt(i).toString();

				if (newElement.compareTo(oldElement) == 0)
				{
					return;
				}
				else if (newElement.compareTo(oldElement) < 0)
				{
					whereToAdd = i;
					break;
				}
			}

			if (whereToAdd == -1)
				whereToAdd = getSize();
		}
		insertElementAt(anObject, whereToAdd);
	}

	public void filterElements(String filter)
	{
		Object anObject;
		
		if (filter == null || filter.equals(""))
		{
			restoreAllElements();
		}
		else
		{
			removeAllElements();
			
			for (int i = 0; i < allObjects.size(); i++)
			{
				anObject = allObjects.elementAt(i); 
				
				if (anObject == null)
					continue;
				
				String element = anObject.toString(); 
				
				if (element.equals(filter) || element.startsWith(filter))
					addElement(anObject);
			}
		}
		
		if (objects.size() > 0)
			selectedObject = objects.elementAt(0);
	}

	// implements javax.swing.ListModel
	public Object getElementAt(int index)
	{
		if (index >= 0 && index < objects.size())
			return objects.elementAt(index);
		else
			return null;
	}

	// implements javax.swing.ComboBoxModel
	public Object getSelectedItem()
	{
		return selectedObject;
	}

	// implements javax.swing.ListModel
	public int getSize()
	{
		return objects.size();
	}

	/**
	 * Returns the index-position of the specified object in the list.
	 *
	 * @param anObject  
	 * @return an int representing the index position, where 0 is 
	 *         the first position
	 */
	public int getIndexOf(Object anObject)
	{
		return objects.indexOf(anObject);
	}

	// implements javax.swing.MutableComboBoxModel
	public void insertElementAt(Object anObject, int index)
	{
		objects.insertElementAt(anObject, index);
		fireIntervalAdded(this, index, index);
		
		if (!allObjects.contains(anObject))
			allObjects.insertElementAt(anObject, index);
	}

	/**
	 * Empties the list.
	 */
	public void removeAllElements()
	{
		if (objects.size() > 0)
		{
			int firstIndex = 0;
			int lastIndex = objects.size() - 1;
			objects.removeAllElements();
			selectedObject = null;
			fireIntervalRemoved(this, firstIndex, lastIndex);
		}
	}

	// implements javax.swing.MutableComboBoxModel
	public void removeElement(Object anObject)
	{
		int index = objects.indexOf(anObject);

		if (index != -1)
			removeElementAt(index);
	}

	// implements javax.swing.MutableComboBoxModel
	public void removeElementAt(int index)
	{
		if (getElementAt(index) == selectedObject)
		{
			if (index == 0)
				setSelectedItem(getSize() == 1 ? null : getElementAt(index + 1));
			else
				setSelectedItem(getElementAt(index - 1));
		}
		objects.removeElementAt(index);
		fireIntervalRemoved(this, index, index);
	}

	public void restoreAllElements()
	{
		removeAllElements();
		
		for (int i = 0; i < allObjects.size(); i++)
			addElement(allObjects.elementAt(i));
	}

	// implements javax.swing.ComboBoxModel
	/**
	 * Set the value of the selected item. The selected item may be null.
	 * <p>
	 * @param anObject The combo box value or null for no selection.
	 */
	public void setSelectedItem(Object anObject)
	{
		if ((selectedObject != null && !selectedObject.equals(anObject))
			|| selectedObject == null
			&& anObject != null)
		{
			selectedObject = anObject;
			fireContentsChanged(this, -1, -1);
		}
	}
}
