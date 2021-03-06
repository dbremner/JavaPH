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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public final class FindDialog extends JavaPHDialog
{
    private final @NotNull JButton findButton = new JButton(JavaPHConstants.FIND_NEXT);
	
	private final @NotNull JCheckBox caseCheckBox = new JCheckBox(JavaPHConstants.CASE_SENSITIVE);
	private final @NotNull JCheckBox wrapCheckBox = new JCheckBox(JavaPHConstants.WRAP_SEARCH);

	private final @NotNull FindComboBox findComboBox = new FindComboBox();

	public FindDialog(final @NotNull JavaPH javaph)
	{
		super(javaph, JavaPHConstants.FIND_TEXT);
		
		final Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		final @NotNull JPanel findPanel = getFindPanel();
		contentPane.add(findPanel, BorderLayout.CENTER);
		final @NotNull JPanel buttonPanel = getButtonPanel(javaph);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);

		pack();
	}
	
	private @NotNull JPanel getButtonPanel(final @NotNull JavaPH parent)
	{
		final @NotNull JPanel buttonPanel = new JPanel();

		findButton.setMnemonic(KeyEvent.VK_F);
		findButton.addActionListener(ae -> {
			if (findComboBox.isEnabled())
			{
				final @Nullable Object selectedItem = findComboBox.getEditor().getItem();
				final @NotNull DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>)findComboBox.getModel();
				if (selectedItem != null && !selectedItem.toString().isEmpty())
				{
					if ( model.getIndexOf(selectedItem) < 0) {
						model.insertElementAt(selectedItem.toString(), 0);
					}

					model.setSelectedItem(selectedItem);
				}
			}

			final @Nullable Object selected = findComboBox.getSelectedItem();
			assert selected != null;
			parent.findText(selected.toString(), caseCheckBox.isSelected(), wrapCheckBox.isSelected());
		});

        final @NotNull JButton closeButton = new JButton(JavaPHConstants.CLOSE);
		closeButton.setMnemonic(KeyEvent.VK_C);
		closeButton.addActionListener(ae -> dispose());

		buttonPanel.add(findButton);
		buttonPanel.add(closeButton);
		
		return buttonPanel;	
	}

	private @NotNull JPanel getFindPanel()
	{
		final @NotNull JPanel findPanel = new JPanel();
		findPanel.setBorder(BorderFactory.createEtchedBorder());
		findPanel.setLayout(new BoxLayout(findPanel, BoxLayout.Y_AXIS));
		
		final @NotNull JPanel findTextPanel = new JPanel();
		findTextPanel.setLayout(new BoxLayout(findTextPanel, BoxLayout.X_AXIS));

		final @NotNull JPanel findControlPanel = new JPanel(new FlowLayout());

        final @NotNull ComboBoxEditor findComboBoxEditor = new TextFieldComboBoxEditor(ae -> findButton.doClick());
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


		findComboBox.setEditable(true);
		findComboBox.setEditor(findComboBoxEditor);
		findComboBox.setPreferredSize(new Dimension(200, 0));
		
		final @NotNull JLabel findLabel = new JLabel(JavaPHConstants.FIND_TEXT_LABEL);
		
		findTextPanel.add(findLabel);
		findTextPanel.add(findComboBox);

		caseCheckBox.setMnemonic(KeyEvent.VK_S);
		caseCheckBox.setSelected(true);

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
