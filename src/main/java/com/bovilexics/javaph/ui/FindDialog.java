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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.bovilexics.javaph.JavaPH;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public final class FindDialog extends JavaPHDialog
{
	@NotNull
    private final JavaPH parent;
	
	private JButton closeButton;
	private JButton findButton;
	
	private JCheckBox caseCheckBox;
	private JCheckBox wrapCheckBox;

	private FindComboBox findComboBox;
	private TextFieldComboBoxEditor findComboBoxEditor;

	class FindComboBoxEditorListener implements ActionListener
	{	
		@Override
		public void actionPerformed(ActionEvent ae)
		{
			findButton.doClick();
		}
	}
	
	public FindDialog(@NotNull JavaPH javaph)
	{
		super(javaph);

		parent = javaph;

		setModal(true);
		setTitle("Find Text");
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		contentPane.add(getFindPanel(), BorderLayout.CENTER);
		contentPane.add(getButtonPanel(), BorderLayout.SOUTH);

		pack();
	}
	
	@NotNull
    private JPanel getButtonPanel()
	{
		@NotNull JPanel buttonPanel = new JPanel();
		
		findButton = new JButton("Find Next");
		findButton.setMnemonic(KeyEvent.VK_F);
		findButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				if (findComboBox.isEnabled())
				{
					Object selectedItem = findComboBox.getEditor().getItem();
					@NotNull DefaultComboBoxModel model = (DefaultComboBoxModel)findComboBox.getModel();
					// FindComboBoxModel model = (FindComboBoxModel)findComboBox.getModel();
		
					if (selectedItem != null && !selectedItem.toString().equals(""))
					{
						if ( model.getIndexOf(selectedItem) < 0) {
							model.insertElementAt(selectedItem, 0);
						}
						
						model.setSelectedItem(selectedItem);
					}
				}

				parent.findText(findComboBox.getSelectedItem().toString(), caseCheckBox.isSelected(), wrapCheckBox.isSelected());
			}
		});

		closeButton = new JButton("Close");
		closeButton.setMnemonic(KeyEvent.VK_C);
		closeButton.addActionListener(ae -> dispose());

		buttonPanel.add(findButton);
		buttonPanel.add(closeButton);
		
		return buttonPanel;	
	}

	@NotNull
    private JPanel getFindPanel()
	{
		@NotNull JPanel findPanel = new JPanel();
		findPanel.setBorder(BorderFactory.createEtchedBorder());
		findPanel.setLayout(new BoxLayout(findPanel, BoxLayout.Y_AXIS));
		
		@NotNull JPanel findTextPanel = new JPanel();
		findTextPanel.setLayout(new BoxLayout(findTextPanel, BoxLayout.X_AXIS));

		@NotNull JPanel findControlPanel = new JPanel(new FlowLayout());
		
		findComboBoxEditor = new TextFieldComboBoxEditor(new FindComboBoxEditorListener());
/*
		findComboBoxEditor.getEditorComponent().addKeyListener(new KeyListener()
		{
			public void keyPressed(KeyEvent ke)
			{
			}
				
			public void keyReleased(KeyEvent ke)
			{
				if (ke.getKeyCode() == KeyEvent.VK_ENTER || ke.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					findComboBox.hidePopup();
				}
				else if (!ke.isActionKey())
				{
					String filter = findComboBoxEditor.getItem().toString();
						
					findComboBox.hidePopup();
					((FindComboBoxModel)findComboBox.getModel()).filterElements(filter);
						
					if (findComboBox.getModel().getSize() > 0)
						findComboBox.showPopup();
				}
			}
				
			public void keyTyped(KeyEvent ke)
			{
			}
		});
*/
		findComboBox = new FindComboBox(true);
		findComboBox.setEditable(true);
		findComboBox.setEditor(findComboBoxEditor);
		findComboBox.setPreferredSize(new Dimension(200, 0));
		
		@NotNull JLabel findLabel = new JLabel(" Find Text : ");
		
		findTextPanel.add(findLabel);
		findTextPanel.add(findComboBox);

		caseCheckBox = new JCheckBox("Case Sensitive");
		caseCheckBox.setMnemonic(KeyEvent.VK_S);
		caseCheckBox.setSelected(true);

		wrapCheckBox = new JCheckBox("Wrap Search");
		wrapCheckBox.setMnemonic(KeyEvent.VK_W);
		wrapCheckBox.setSelected(true);

		findControlPanel.add(caseCheckBox);
		findControlPanel.add(wrapCheckBox);
		
		findPanel.add(new JLabel(" "));
		findPanel.add(findTextPanel);
		findPanel.add(findControlPanel);
		findPanel.add(new JLabel(" "));
		
		return findPanel;
	}
}
