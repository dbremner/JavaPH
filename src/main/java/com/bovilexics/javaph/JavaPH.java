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
package com.bovilexics.javaph;

import com.Ostermiller.util.Browser;
import com.bovilexics.javaph.logging.ErrLogger;
import com.bovilexics.javaph.logging.Logger;
import com.bovilexics.javaph.logging.OutLogger;
import com.bovilexics.javaph.models.QueryComboBoxModel;
import com.bovilexics.javaph.models.TableSorter;
import com.bovilexics.javaph.qi.Connection;
import com.bovilexics.javaph.qi.Field;
import com.bovilexics.javaph.qi.QiCommand;
import com.bovilexics.javaph.qi.QiConnection;
import com.bovilexics.javaph.qi.QiFieldState;
import com.bovilexics.javaph.qi.QiServerManager;
import com.bovilexics.javaph.qi.Server;
import com.bovilexics.javaph.threads.QueryThread;
import com.bovilexics.javaph.ui.AboutDialog;
import com.bovilexics.javaph.ui.CustomButtonGroup;
import com.bovilexics.javaph.ui.FindDialog;
import com.bovilexics.javaph.ui.IconProvider;
import com.bovilexics.javaph.ui.ListDataAdapter;
import com.bovilexics.javaph.ui.MainMenu;
import com.bovilexics.javaph.ui.PropertiesDialog;
import com.bovilexics.javaph.ui.QueryComboBox;
import com.bovilexics.javaph.ui.QueryToolBar;
import com.bovilexics.javaph.ui.ResultTable;
import com.bovilexics.javaph.ui.ServerRenderer;
import com.bovilexics.javaph.ui.SplashWindow;
import com.bovilexics.javaph.ui.TextFieldComboBoxEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.ProgressMonitor;
import javax.swing.RootPaneContainer;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ListDataEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import static com.bovilexics.javaph.JavaPHConstants.APP_DEFAULT_HEIGHT;
import static com.bovilexics.javaph.JavaPHConstants.APP_DEFAULT_STATUS;
import static com.bovilexics.javaph.JavaPHConstants.APP_DEFAULT_WIDTH;
import static com.bovilexics.javaph.JavaPHConstants.APP_MIN_HEIGHT;
import static com.bovilexics.javaph.JavaPHConstants.APP_MIN_WIDTH;
import static com.bovilexics.javaph.JavaPHConstants.COMMA_SEPARATOR;
import static com.bovilexics.javaph.JavaPHConstants.CUSTOM_SEPARATOR;
import static com.bovilexics.javaph.JavaPHConstants.FIELD_ALL;
import static com.bovilexics.javaph.JavaPHConstants.FIELD_CUSTOM;
import static com.bovilexics.javaph.JavaPHConstants.FIELD_DEFAULT;
import static com.bovilexics.javaph.JavaPHConstants.FIELD_LABEL_PREFIX;
import static com.bovilexics.javaph.JavaPHConstants.LOAD_FIELDS_DEF;
import static com.bovilexics.javaph.JavaPHConstants.LOAD_FIELDS_MANUAL;
import static com.bovilexics.javaph.JavaPHConstants.LOAD_FIELDS_SELECTED;
import static com.bovilexics.javaph.JavaPHConstants.LOAD_FIELDS_STARTUP;
import static com.bovilexics.javaph.JavaPHConstants.PORT_LABEL_PREFIX;
import static com.bovilexics.javaph.JavaPHConstants.PORT_LABEL_SUFFIX;
import static com.bovilexics.javaph.JavaPHConstants.PROP_APP_HEIGHT;
import static com.bovilexics.javaph.JavaPHConstants.PROP_APP_WIDTH;
import static com.bovilexics.javaph.JavaPHConstants.PROP_APP_X_POSITION;
import static com.bovilexics.javaph.JavaPHConstants.PROP_APP_Y_POSITION;
import static com.bovilexics.javaph.JavaPHConstants.PROP_DEFAULT_LNF;
import static com.bovilexics.javaph.JavaPHConstants.PROP_DEFAULT_SERVER;
import static com.bovilexics.javaph.JavaPHConstants.PROP_DISPLAY_LOG;
import static com.bovilexics.javaph.JavaPHConstants.PROP_DISPLAY_SPLASH;
import static com.bovilexics.javaph.JavaPHConstants.PROP_DISPLAY_TOOLBAR;
import static com.bovilexics.javaph.JavaPHConstants.PROP_FILE;
import static com.bovilexics.javaph.JavaPHConstants.PROP_FILE_DEF;
import static com.bovilexics.javaph.JavaPHConstants.PROP_HEADER;
import static com.bovilexics.javaph.JavaPHConstants.PROP_LOAD_FIELDS;
import static com.bovilexics.javaph.JavaPHConstants.PROP_QUERY_RUNTIME;
import static com.bovilexics.javaph.JavaPHConstants.PROP_SAVE_POSITION;
import static com.bovilexics.javaph.JavaPHConstants.QUERY_COMMAND;
import static com.bovilexics.javaph.JavaPHConstants.QUERY_LABEL_PREFIX;
import static com.bovilexics.javaph.JavaPHConstants.QUERY_RUNTIME_DEF;
import static com.bovilexics.javaph.JavaPHConstants.QUERY_RUNTIME_MAX;
import static com.bovilexics.javaph.JavaPHConstants.QUERY_RUNTIME_MIN;
import static com.bovilexics.javaph.JavaPHConstants.RESULT_TABLE_LABEL;
import static com.bovilexics.javaph.JavaPHConstants.RESULT_TABLE_TAB;
import static com.bovilexics.javaph.JavaPHConstants.RESULT_TEXT_LABEL;
import static com.bovilexics.javaph.JavaPHConstants.RESULT_TEXT_TAB;
import static com.bovilexics.javaph.JavaPHConstants.SERVER_LABEL_PREFIX;
import static com.bovilexics.javaph.JavaPHConstants.SERVER_LABEL_SUFFIX;
import static com.bovilexics.javaph.JavaPHConstants.SPLASH_DISPLAY;
import static com.bovilexics.javaph.JavaPHConstants.SYSTEM_LOG_LABEL;
import static com.bovilexics.javaph.JavaPHConstants.SYSTEM_LOG_TAB;
import static com.bovilexics.javaph.JavaPHConstants.TAB_LABELS;
import static com.bovilexics.javaph.JavaPHConstants.TAB_SEPARATOR;

// TODO : Future : ability to add and remove servers
// TODO : Future : ability to update server list from the web
// TODO : Future : add XML export/save capability
// TODO : Future : having documentation at some point would be nice
// TODO : Future : run query against multiple servers and collate results

/**
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public final class JavaPH extends JApplet implements IconProvider, WindowListener
{
	private final Logger logger = new OutLogger();

	private final Logger errLogger = new ErrLogger();

	// Custom widgets and other private stuff

	private final @NotNull JFrame frame = new JFrame();

	private boolean fieldQuoted = false;
	private boolean savePosition = false;

	private int loadFields = LOAD_FIELDS_SELECTED;
	private int queryRuntime = QUERY_RUNTIME_DEF;

	private final @NotNull AboutDialog aboutDialog;
	private CustomButtonGroup fieldRadioGroup;
	private final @NotNull FindDialog findDialog;
	private final @NotNull Font fixedWidthFont = new Font("Monospaced", Font.PLAIN, 12);
	private final @NotNull Properties defaultProperties = new Properties();
	private final @NotNull Properties properties = new Properties();
	private final @NotNull PropertiesDialog propertiesDialog;
	private ProgressMonitor queryProgressMonitor;
	private final @NotNull QiCommand[] commands;
	private @Nullable Connection connection;
	private QueryComboBox queryComboBox;
	private final @NotNull QueryToolBar queryToolBar;
	private ResultTable resultTable;
	private final @NotNull SplashWindow splashWindow;
	private @Nullable String customFieldSeparator = CUSTOM_SEPARATOR;
	private @NotNull String fieldSeparator = COMMA_SEPARATOR;
	private TextFieldComboBoxEditor queryComboBoxEditor;
	private Vector<Server> servers;
	
	// Swing widgets and stuff
	
	private final JButton queryButton = new JButton("Execute Query");
	private JComboBox<QiCommand> commandComboBox;
	private JComboBox<Server> serverComboBox;
	private JLabel portStatusLabel;
	private JLabel serverStatusLabel;
	private JLabel statusLabel;
	private final JList colList = new JList(new DefaultListModel());
	private JList<Field> fieldList;
	private final JPanel colListPanel = new JPanel(new BorderLayout());
	private final JPanel fieldListPanel = new JPanel(new BorderLayout());
	private final JPanel logTextPanel = new JPanel(new BorderLayout());
	private JRootPane defaultPane;
	private final @NotNull JTabbedPane resultPanel;
	private final JTextArea logText = new JTextArea();
	private final JTextArea resultText = new JTextArea();

	@Override
	public void windowOpened(final WindowEvent e)
	{
	}

	@Override
	public void windowClosing(final WindowEvent e)
	{
	}

	@Override
	public void windowClosed(final WindowEvent e)
	{
		boolean needToStore = false;

		if (savePosition)
		{
			needToStore = true;
			setProperty(PROP_APP_HEIGHT, frame.getHeight());
			setProperty(PROP_APP_WIDTH, frame.getWidth());
			setProperty(PROP_APP_X_POSITION, frame.getX());
			setProperty(PROP_APP_Y_POSITION, frame.getY());
		}
		else
		{
			if (getIntProperty(PROP_APP_HEIGHT, APP_DEFAULT_HEIGHT) != APP_DEFAULT_HEIGHT)
			{
				needToStore = true;
				setProperty(PROP_APP_HEIGHT, "" + APP_DEFAULT_HEIGHT);
			}
			if (getIntProperty(PROP_APP_WIDTH, APP_DEFAULT_WIDTH) != APP_DEFAULT_WIDTH)
			{
				needToStore = true;
				setProperty(PROP_APP_WIDTH, "" + APP_DEFAULT_WIDTH);
			}
			if (getIntProperty(PROP_APP_X_POSITION, -1) != -1)
			{
				needToStore = true;
				setProperty(PROP_APP_X_POSITION, -1);
			}
			if (getIntProperty(PROP_APP_Y_POSITION, -1) != -1)
			{
				needToStore = true;
				setProperty(PROP_APP_Y_POSITION, -1);
			}
		}

		if (needToStore) {
			storeProperties();
		}

		System.exit(0);
	}

	@Override
	public void windowIconified(final WindowEvent e)
	{
	}

	@Override
	public void windowDeiconified(final WindowEvent e)
	{
	}

	@Override
	public void windowActivated(final WindowEvent e)
	{
	}

	@Override
	public void windowDeactivated(final WindowEvent e)
	{
	}

	private static class NullKeyAdapter extends KeyAdapter {
	}

	private static class NullMouseAdapter extends MouseAdapter {
	}

	class ControlTabDispatcher	implements KeyEventDispatcher
	{
		@Override
		public boolean dispatchKeyEvent(final @NotNull KeyEvent ke)
		{
			if (ke.isControlDown() && ke.getKeyCode() == KeyEvent.VK_TAB)
			{
				// If we don't have this condition then the tab will switch twice,
				// once for the key pressed event and again for the key released event.
				if (ke.getID() == Event.KEY_RELEASE)
				{
					int selected = getResultPanel().getSelectedIndex();
					final int last = getResultPanel().getTabCount() - 1;
			
					if (ke.isShiftDown())
					{
						if (selected <= 0) {
							selected = last;
						} else {
							selected--;
						}
					}
					else
					{
						if (selected < last) {
							selected++;
						} else {
							selected = 0;
						}
					}
				
					getResultPanel().setSelectedIndex(selected);
				}

				return true;
			}
			else
			{
				return false;
			}
		}
	}	

	final class ContentPanel extends JPanel
	{	
		ContentPanel(final @NotNull JPanel queryPanel, final @NotNull JTabbedPane resultPanel)
		{	
			setBorder(BorderFactory.createEtchedBorder());
			setLayout(new BorderLayout());
			add(queryPanel, BorderLayout.NORTH);
			add(resultPanel, BorderLayout.CENTER);
		}
	}

	final class QueryPanel extends JPanel
	{
		private final @NotNull JavaPH parent;
		private JButton fieldListMoveDnButton;
		private JButton fieldListMoveUpButton;
		private JButton fieldListSelectAllButton;
		private JButton fieldListSelectNoneButton;
		private @Nullable JButton fieldLoadButton;
		private @Nullable JButton fieldCustomButton;
		private final @NotNull ImageIcon fieldCustomOff;
		private final @NotNull ImageIcon fieldCustomOn;
		private final @NotNull ImageIcon fieldLoadOff;
		private final @NotNull ImageIcon fieldLoadOn;
		private final JRadioButton fieldCustomRadioButton = new JRadioButton("Custom Fields");

		
		QueryPanel(final @NotNull JavaPH javaph)
		{
			parent = javaph;

			fieldCustomOff = getImageIcon("img/field-custom-off.gif");
			fieldCustomOn = getImageIcon("img/field-custom-on.gif");
			fieldLoadOff = getImageIcon("img/field-load-off.gif");
			fieldLoadOn = getImageIcon("img/field-load-on.gif");

			initFieldListPanel();

			setLayout(new BorderLayout());

			add(getQueryLabelPanel(), BorderLayout.WEST);
			add(getQueryContentPanel(), BorderLayout.CENTER);
			add(getQueryButtonPanel(javaph), BorderLayout.EAST);
		}

		private @NotNull JPanel getQueryButtonPanel(final @NotNull JavaPH javaph)
		{
			final @NotNull JPanel queryButtonPanel = new JPanel(new FlowLayout());

			queryButton.setMnemonic(KeyEvent.VK_Q);
			queryButton.setToolTipText("Click this to start running the query.");
			queryButton.addActionListener(ae -> {
				if (queryComboBox.isEnabled())
				{
					final Object selectedItem = queryComboBox.getEditor().getItem();
					final @NotNull QueryComboBoxModel model = (QueryComboBoxModel)queryComboBox.getModel();

					if (selectedItem != null && !selectedItem.toString().isEmpty())
					{
						if ( model.getIndexOf(selectedItem) < 0) {
							model.addElement(selectedItem);
						}

						model.setSelectedItem(selectedItem);
					}
				}

				queryProgressMonitor = new ProgressMonitor(defaultPane, "Executing Query", getCommand(), 0, getQueryRuntime());
				final @NotNull QueryThread qt = new QueryThread(javaph);
				qt.start();
			});

			queryButtonPanel.add(queryButton);
			
			return queryButtonPanel;
		}

		private @NotNull JPanel getQueryCommandPanel()
		{
			final @NotNull JPanel queryCommandPanel = new JPanel(new BorderLayout());

			commandComboBox = new JComboBox<>(commands);
			commandComboBox.addActionListener(ae -> {
				queryComboBox.setSelectedIndex(-1);
				queryComboBox.setEnabled(commands[commandComboBox.getSelectedIndex()].isTextEditable());
				fieldRadioGroup.setEnabled(commandComboBox.getSelectedIndex() == QUERY_COMMAND);
				fieldCustomButton.setEnabled(commandComboBox.getSelectedIndex() == QUERY_COMMAND);
				fieldLoadButton.setEnabled(commandComboBox.getSelectedIndex() == QUERY_COMMAND);
			});

			queryComboBoxEditor = new TextFieldComboBoxEditor(ae -> queryButton.doClick());

			queryComboBoxEditor.getEditorComponent().addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyReleased(final @NotNull KeyEvent ke)
				{
					if (ke.getKeyCode() == KeyEvent.VK_ENTER || ke.getKeyCode() == KeyEvent.VK_ESCAPE)
					{
						queryComboBox.hidePopup();
					}
					else if (!ke.isActionKey())
					{
						final String filter = queryComboBoxEditor.getItem().toString();
						
						queryComboBox.hidePopup();
						((QueryComboBoxModel)queryComboBox.getModel()).filterElements(filter);
						
						if (queryComboBox.getModel().getSize() > 0) {
							queryComboBox.showPopup();
						}
					}
					else if (queryComboBox.getModel().getSize() == 1 && (ke.getKeyCode() == KeyEvent.VK_UP || ke.getKeyCode() == KeyEvent.VK_DOWN))
					{
						// force reselecting of first item if only one item
						// in the list and the up or down arrow is pressed
						queryComboBox.setSelectedIndex(-1);
						queryComboBox.setSelectedIndex(0);
					}
				}
			});

			queryComboBox = new QueryComboBox();
			queryComboBox.setEditable(true);
			queryComboBox.setEditor(queryComboBoxEditor);

			queryCommandPanel.add(commandComboBox, BorderLayout.WEST);
			queryCommandPanel.add(queryComboBox, BorderLayout.CENTER);

			return queryCommandPanel;
		}

		private @NotNull JPanel getQueryContentPanel()
		{
			final @NotNull JPanel queryContentPanel = new JPanel(new BorderLayout());

			queryContentPanel.add(getQueryServerPanel(), BorderLayout.NORTH);
			queryContentPanel.add(getQueryCommandPanel(), BorderLayout.CENTER);
			queryContentPanel.add(getQueryFieldPanel(), BorderLayout.SOUTH);
			
			return queryContentPanel;
		}

		private @NotNull JPanel getQueryFieldPanel()
		{
			final @NotNull JPanel queryFieldPanel = new JPanel();
			queryFieldPanel.setLayout(new BoxLayout(queryFieldPanel, BoxLayout.X_AXIS));

			fieldRadioGroup = new CustomButtonGroup();

			final @NotNull JRadioButton fieldDefaultRadioButton = new JRadioButton("Default Fields");
			final @NotNull JRadioButton fieldAllRadioButton = new JRadioButton("All Fields");

			fieldRadioGroup.add(fieldDefaultRadioButton);
			fieldRadioGroup.add(fieldAllRadioButton);
			fieldRadioGroup.add(fieldCustomRadioButton);
			
			fieldDefaultRadioButton.setSelected(true);
				
			fieldCustomButton = new JButton(fieldCustomOff);
			fieldCustomButton.setBorder(BorderFactory.createEtchedBorder());
			fieldCustomButton.setFocusable(false);
			fieldCustomButton.setToolTipText("Choose the fields that queries will return");
			fieldCustomButton.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseEntered(final MouseEvent e)
				{
					fieldCustomButton.setIcon(fieldCustomOn);
				}

				@Override
				public void mouseExited(final MouseEvent e)
				{
					fieldCustomButton.setIcon(fieldCustomOff);
				}
			});
			fieldCustomButton.addActionListener(ae -> {
				final @NotNull int[] prevSelections = fieldList.getSelectedIndices();
				final int option = JOptionPane.showConfirmDialog(getDefaultPane(), fieldListPanel, "Field List for " + getServer().toString(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

				if (option != JOptionPane.OK_OPTION)
				{
					fieldList.setSelectedIndices(prevSelections);
				}
				else
				{
					fieldCustomRadioButton.setSelected(true);
				}
			});

			fieldLoadButton = new JButton(fieldLoadOff);
			fieldLoadButton.setBorder(BorderFactory.createEtchedBorder());
			fieldLoadButton.setFocusable(false);
			fieldLoadButton.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseEntered(final MouseEvent e)
				{
					fieldLoadButton.setIcon(fieldLoadOn);
				}

				@Override
				public void mouseExited(final MouseEvent e)
				{
					fieldLoadButton.setIcon(fieldLoadOff);
				}
			});
			fieldLoadButton.addActionListener(ae -> {
				final @Nullable Server server = getServer();

				if (server != null)
				{
					loadFieldsForServer(server);
					serverComboBox.repaint();
					populateFieldList(server);
				}
			});

			queryFieldPanel.add(new JLabel(FIELD_LABEL_PREFIX), BorderLayout.SOUTH);
			queryFieldPanel.add(fieldDefaultRadioButton);
			queryFieldPanel.add(fieldAllRadioButton);
			queryFieldPanel.add(fieldCustomRadioButton);
			queryFieldPanel.add(fieldCustomButton);
			queryFieldPanel.add(new JLabel(" "));
			queryFieldPanel.add(fieldLoadButton);
			
			return queryFieldPanel;
		}

		private @NotNull JPanel getQueryLabelPanel()
		{
			final @NotNull JPanel queryLabelPanel = new JPanel(new BorderLayout());

			queryLabelPanel.add(new JLabel(SERVER_LABEL_PREFIX), BorderLayout.NORTH);
			queryLabelPanel.add(new JLabel(QUERY_LABEL_PREFIX), BorderLayout.CENTER);
			queryLabelPanel.add(new JLabel(" "), BorderLayout.SOUTH);
			
			return queryLabelPanel;
		}

		private @NotNull JPanel getQueryServerPanel()
		{
			final @NotNull JPanel queryServerPanel = new JPanel(new BorderLayout());

			serverComboBox = new JComboBox<>(servers);
			serverComboBox.setRenderer(new ServerRenderer(parent));
			serverComboBox.addActionListener(ae -> {
				final @NotNull Server server = getServer();
				final @NotNull String serverText = server.getServer();

				final @NotNull Integer portInt = server.getPort();

				serverStatusLabel.setText(SERVER_LABEL_PREFIX + serverText + SERVER_LABEL_SUFFIX);
				final @NotNull String portText = portInt.toString();
				portStatusLabel.setText(PORT_LABEL_PREFIX + portText + PORT_LABEL_SUFFIX);

				connection = new QiConnection(serverText, portInt);

				if (getLoadFields() == LOAD_FIELDS_SELECTED && !(server.getFieldState() == QiFieldState.FIELD_LOAD_ERROR) && !(server.getFieldState() == QiFieldState.FIELD_LOAD_TRUE))
				{
					serverComboBox.hidePopup();
					loadFieldsForServer(server);
				}

				populateFieldList(server);
			});
			QiServerManager.setDefaultServer(getProperty(PROP_DEFAULT_SERVER));

			queryServerPanel.add(serverComboBox, BorderLayout.CENTER);

			return queryServerPanel;
		}
		
		private void initFieldListPanel()
		{
			// This panel is not visible in the main window
			// It will be in a separate dialog that is displayed
			// when the custom field button is activated

			final @NotNull JPanel fieldListControlPanel = new JPanel(new BorderLayout());
			fieldListControlPanel.setBorder(BorderFactory.createEtchedBorder());
			
			final @NotNull JPanel fieldListButtonPanel = new JPanel(new FlowLayout());

			fieldListSelectAllButton = new JButton("Select All");
			fieldListSelectAllButton.addActionListener(
					ae -> fieldList.setSelectionInterval(0, fieldList.getModel().getSize() - 1));
			
			fieldListSelectNoneButton = new JButton("Delselect All");
			fieldListSelectNoneButton.addActionListener(ae -> fieldList.clearSelection());

		
			fieldListMoveUpButton = new JButton("Move Up");
			fieldListMoveUpButton.addActionListener(ae -> {
				final @NotNull int[] selections = fieldList.getSelectedIndices();

				for (int i = 0; i < selections.length; i++)
				{
					final Object toMove = fieldList.getModel().getElementAt(selections[i]);
					((DefaultListModel)fieldList.getModel()).removeElementAt(selections[i]);
					selections[i]--;
					((DefaultListModel)fieldList.getModel()).insertElementAt(toMove, selections[i]);
				}

				fieldList.setSelectedIndices(selections);
			});
			
			fieldListMoveDnButton = new JButton("Move Down");
			fieldListMoveDnButton.addActionListener(ae -> {
				final @NotNull int[] selections = fieldList.getSelectedIndices();

				for (int i = selections.length - 1; i >= 0; i--)
				{
					final Object toMove = fieldList.getModel().getElementAt(selections[i]);
					((DefaultListModel)fieldList.getModel()).removeElementAt(selections[i]);
					selections[i]++;
					((DefaultListModel)fieldList.getModel()).insertElementAt(toMove, selections[i]);
				}

				fieldList.setSelectedIndices(selections);
			});

			fieldListSelectAllButton.setEnabled(false);
			fieldListSelectNoneButton.setEnabled(false);

			fieldListMoveUpButton.setEnabled(false);
			fieldListMoveDnButton.setEnabled(false);

			fieldListButtonPanel.add(fieldListSelectAllButton);
			fieldListButtonPanel.add(fieldListSelectNoneButton);
			fieldListButtonPanel.add(new JLabel("  "));
			fieldListButtonPanel.add(fieldListMoveUpButton);
			fieldListButtonPanel.add(fieldListMoveDnButton);

			fieldList = new JList<>(new DefaultListModel<>());
			fieldList.addListSelectionListener(lse -> {
				if (fieldList.getSelectedIndex() >= 0)
				{
					fieldListMoveUpButton.setEnabled(!fieldList.isSelectedIndex(0));
					fieldListMoveDnButton.setEnabled(!fieldList.isSelectedIndex(fieldList.getModel().getSize() - 1));
				}
				else
				{
					fieldListMoveUpButton.setEnabled(false);
					fieldListMoveDnButton.setEnabled(false);
				}
			});
			fieldList.getModel().addListDataListener(new ListDataAdapter()
			{
				@Override
				protected void changed(final ListDataEvent e)
				{
					fieldListSelectAllButton.setEnabled(fieldList.getModel().getSize() > 0);
					fieldListSelectNoneButton.setEnabled(fieldList.getModel().getSize() > 0);					
				}
			});

			fieldListControlPanel.add(new JScrollPane(fieldList), BorderLayout.CENTER);
			fieldListControlPanel.add(fieldListButtonPanel, BorderLayout.SOUTH);

			fieldListPanel.add(new JLabel("Select the fields to be returned"), BorderLayout.NORTH);
			fieldListPanel.add(fieldListControlPanel, BorderLayout.CENTER);
		}

		private void populateFieldList(final @NotNull Server server)
		{
			final @NotNull DefaultListModel model = (DefaultListModel)fieldList.getModel();
			model.clear();

			if (server.getFieldState() == QiFieldState.FIELD_LOAD_TRUE)
			{
				final @NotNull List<Field> fields = server.getFields();

				for (final Field field : fields) {
					model.addElement(field);
				}
			}

			if (model.getSize() > 0) {
				fieldList.setSelectionInterval(0, model.getSize() - 1);
			}

			if (server.getFieldState() == QiFieldState.FIELD_LOAD_TRUE) {
				fieldLoadButton.setToolTipText("Reload fields for the selected server");
			} else {
				fieldLoadButton.setToolTipText("Load fields for the selected server");
			}
		}

		private void loadFieldsForServer(final @NotNull Server server)
		{
			final @NotNull String status1 = "Loading fields for " + server.getExpandedName();
			showStatus(status1);
			log(status1);

			final Component component = ((RootPaneContainer)defaultPane.getTopLevelAncestor()).getGlassPane();

			component.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			component.setVisible(true);
			component.addMouseListener(new NullMouseAdapter());
			component.addKeyListener(new NullKeyAdapter());

			server.loadFields();

			component.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			component.setVisible(false);

			final @NotNull String status = server.getFieldStateMessage();
			showStatus(status);
			log(status);
		}
	}

	final class ResultPanel extends JTabbedPane
	{
		private final JButton colListSelectAllButton = new JButton("Select All");
		private final JButton colListSelectNoneButton = new JButton("Delselect All");
		private final JButton resultTableColButton = new JButton("Show/Hide Columns");
		private final JButton resultTableColWidthButton = new JButton("Reset Column Widths");
		private final JCheckBox logWrapCheckBox = new JCheckBox("Line Wrap");
		private final JCheckBox resultWrapCheckBox = new JCheckBox("Line Wrap");

		ResultPanel()
		{
			super(SwingConstants.TOP);
	
			add(getResultTextPanel(), RESULT_TEXT_LABEL);
			add(getResultTablePanel(), RESULT_TABLE_LABEL);

			initColumnListPanel();
			initLogTextPanel();
	
			if (propertyEquals(PROP_DISPLAY_LOG, "true", "true")) {
				add(logTextPanel, SYSTEM_LOG_LABEL);
			}
		}

		private @NotNull JPanel getResultTablePanel()
		{
			final @NotNull JPanel resultTablePanel = new JPanel(new BorderLayout());

			resultTable = new ResultTable();
			resultTable.setColumnSelectionAllowed(false);
			resultTable.setRowSelectionAllowed(false);
			resultTable.setCellSelectionEnabled(true);
			resultTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			resultTable.getModel().addTableModelListener(tme -> {
				resultTableColButton.setEnabled(resultTable.getRowCount() > 0);
				resultTableColWidthButton.setEnabled(resultTable.getRowCount() > 0);
				populateColumnList();
			});
				
			final @NotNull JTableHeader resultTableHeader = resultTable.getTableHeader();
			resultTableHeader.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(final @NotNull MouseEvent me)
				{
					final TableColumnModel tcm = resultTable.getColumnModel();
					final int vc = tcm.getColumnIndexAtX(me.getX());
					final int mc = resultTable.convertColumnIndexToModel(vc);
						
					((TableSorter)resultTable.getModel()).sort(mc);
				}
			});

			final @NotNull JPanel resultControlPanel = new JPanel();
			resultTableColButton.setEnabled(false);
			resultTableColButton.addActionListener(ae -> {
				final @NotNull int[] prevSelections = colList.getSelectedIndices();
				final int option = JOptionPane.showConfirmDialog(getDefaultPane(), colListPanel, "Column List for Result Table", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

				if (option != JOptionPane.OK_OPTION)
				{
					colList.setSelectedIndices(prevSelections);
				}
				else
				{
					for (int i = 0; i < colList.getModel().getSize(); i++)
					{
						if (colList.isSelectedIndex(i))
						{
							resultTable.getColumn(colList.getModel().getElementAt(i).toString()).setPreferredWidth(resultTable.getColumn(colList.getModel().getElementAt(i).toString()).getMaxWidth());
						}
						else
						{
							resultTable.getColumn(colList.getModel().getElementAt(i).toString()).setPreferredWidth(0);
						}
					}
				}
			});

			resultTableColWidthButton.setEnabled(false);
			resultTableColWidthButton.addActionListener(ae -> {
				for (int i = 0; i < colList.getModel().getSize(); i++)
				{
					// If it is a shown column and not hidden (selected in list)
					// then reset its preferred width to its maximum width
					if (colList.isSelectedIndex(i))
					{
						resultTable.getColumn(colList.getModel().getElementAt(i).toString()).setPreferredWidth(resultTable.getColumn(colList.getModel().getElementAt(i).toString()).getMaxWidth());
					}
				}
			});

			final @NotNull JScrollPane resultTableScroll = new JScrollPane(resultTable);
			resultControlPanel.add(resultTableColButton);
			resultControlPanel.add(resultTableColWidthButton);

			resultTablePanel.add(resultTableScroll, BorderLayout.CENTER);
			resultTablePanel.add(resultControlPanel, BorderLayout.SOUTH);
	
			return resultTablePanel;
		}
		
		private @NotNull JPanel getResultTextPanel()
		{
			final @NotNull JPanel resultTextPanel = new JPanel(new BorderLayout());

			resultText.setEditable(false);
			resultText.setFont(fixedWidthFont);
			final @NotNull JScrollPane resultTextScroll = new JScrollPane(resultText);

			final @NotNull JButton selectAllButton = new JButton("Select All");
			selectAllButton.setMnemonic(KeyEvent.VK_A);
			selectAllButton.addActionListener(ae -> {
				resultText.selectAll();
				resultText.getCaret().setSelectionVisible(true);
			});

			resultWrapCheckBox.addItemListener(ie -> {
				resultText.setLineWrap(resultWrapCheckBox.isSelected());
				resultText.setWrapStyleWord(resultWrapCheckBox.isSelected());
			});
	
			final @NotNull JPanel resultControlPanel = new JPanel();
			resultControlPanel.add(selectAllButton);
			resultControlPanel.add(resultWrapCheckBox);
	
			resultTextPanel.add(resultTextScroll, BorderLayout.CENTER);
			resultTextPanel.add(resultControlPanel, BorderLayout.SOUTH);

			return resultTextPanel;			
		}

		private void initColumnListPanel()
		{
			// This panel is not visible in the main window
			// It will be in a separate dialog that is displayed
			// when the custom field button is activated
			final @NotNull JPanel colListControlPanel = new JPanel(new BorderLayout());
			colListControlPanel.setBorder(BorderFactory.createEtchedBorder());

			final @NotNull JPanel colListButtonPanel = new JPanel(new FlowLayout());

			colListSelectAllButton.addActionListener(ae ->
					colList.setSelectionInterval(0, colList.getModel().getSize() - 1));

			colListSelectNoneButton.addActionListener(ae -> colList.clearSelection());

			colListSelectAllButton.setEnabled(false);
			colListSelectNoneButton.setEnabled(false);

			colListButtonPanel.add(colListSelectAllButton);
			colListButtonPanel.add(colListSelectNoneButton);

			colList.getModel().addListDataListener(new ListDataAdapter()
			{
				@Override
				protected void changed(final ListDataEvent e)
				{
					colListSelectAllButton.setEnabled(colList.getModel().getSize() > 0);
					colListSelectNoneButton.setEnabled(colList.getModel().getSize() > 0);					
				}
			});

			colListControlPanel.add(new JScrollPane(colList), BorderLayout.CENTER);
			colListControlPanel.add(colListButtonPanel, BorderLayout.SOUTH);

			colListPanel.add(new JLabel("Select the columns to be displayed"), BorderLayout.NORTH);
			colListPanel.add(colListControlPanel, BorderLayout.CENTER);
		}
		
		private void initLogTextPanel()
		{
			logText.setEditable(false);
			logText.setFont(fixedWidthFont);
			final @NotNull JScrollPane logTextScroll = new JScrollPane(logText);

			final @NotNull JButton selectAllButton = new JButton("Select All");
			selectAllButton.setMnemonic(KeyEvent.VK_A);
			selectAllButton.addActionListener(ae -> {
				logText.selectAll();
				logText.getCaret().setSelectionVisible(true);
			});

			logWrapCheckBox.addItemListener(ie -> {
				logText.setLineWrap(logWrapCheckBox.isSelected());
				logText.setWrapStyleWord(logWrapCheckBox.isSelected());
			});

			final @NotNull JButton logClearButton = new JButton("Clear Log");
			logClearButton.setMnemonic(KeyEvent.VK_C);
			logClearButton.addActionListener(ae -> {
				logText.setText("");
				log("Log cleared");
			});
	
			final @NotNull JPanel logControlPanel = new JPanel();
			logControlPanel.add(logClearButton);
			logControlPanel.add(selectAllButton);
			logControlPanel.add(logWrapCheckBox);
	
			logTextPanel.add(logTextScroll, BorderLayout.CENTER);
			logTextPanel.add(logControlPanel, BorderLayout.SOUTH);
		}

		private void populateColumnList()
		{
			final @NotNull TableModel tableModel = resultTable.getModel();
			final @NotNull DefaultListModel listModel = (DefaultListModel)colList.getModel();
			listModel.clear();

			for (int i = 0; i < tableModel.getColumnCount(); i++)
			{
				listModel.addElement(tableModel.getColumnName(i));
			}

			if (listModel.getSize() > 0) {
				colList.setSelectionInterval(0, listModel.getSize() - 1);
			}
		}

	}

	final class StatusPanel extends JPanel
	{	
		StatusPanel()
		{
			setLayout(new BorderLayout());

			final @NotNull JPanel leftPanel = new JPanel();
			leftPanel.setBorder(BorderFactory.createEtchedBorder());
			leftPanel.setLayout(new BorderLayout());

			final @NotNull JPanel rightPanel = new JPanel();
			rightPanel.setLayout(new BorderLayout());

			add(leftPanel, BorderLayout.CENTER);
			add(rightPanel, BorderLayout.EAST);

			statusLabel = new JLabel();
			statusLabel.setHorizontalAlignment(JLabel.LEFT);
			leftPanel.add(statusLabel, BorderLayout.WEST);

			serverStatusLabel = new JLabel();
			serverStatusLabel.setBorder(BorderFactory.createEtchedBorder());
			serverStatusLabel.setHorizontalAlignment(JLabel.CENTER);
			rightPanel.add(serverStatusLabel, BorderLayout.CENTER);

			portStatusLabel = new JLabel();
			portStatusLabel.setBorder(BorderFactory.createEtchedBorder());
			portStatusLabel.setHorizontalAlignment(JLabel.CENTER);
			rightPanel.add(portStatusLabel, BorderLayout.EAST);
			
			showDefaultStatus();
		}
	}

	public static void main(final String[] args)
	{
		final @NotNull JavaPH applet = new JavaPH();
		applet.getQueryComboBox().getEditor().getEditorComponent().requestFocus();
	}

	public JavaPH()
	{
		commands = QiCommand.commands;
		int frameHeight = getIntProperty(PROP_APP_HEIGHT, APP_DEFAULT_HEIGHT);
		int frameWidth = getIntProperty(PROP_APP_WIDTH, APP_DEFAULT_WIDTH);
		int frameX = getIntProperty(PROP_APP_X_POSITION, -1);
		int frameY = getIntProperty(PROP_APP_Y_POSITION, -1);

		if (frameHeight < APP_MIN_HEIGHT) {
			frameHeight = APP_DEFAULT_HEIGHT;
		}
		if (frameWidth < APP_MIN_WIDTH) {
			frameWidth = APP_DEFAULT_WIDTH;
		}

		defaultPane = frame.getRootPane();

		Browser.init();

		initProperties();
		initServers();
		aboutDialog      = new AboutDialog(this);
		findDialog       = new FindDialog(this);
		propertiesDialog = new PropertiesDialog(this);
		queryToolBar     = new QueryToolBar(this);
		splashWindow     = new SplashWindow(this);

		if (defaultPane == null) {
			defaultPane = getRootPane();
		}

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new ControlTabDispatcher());

		restoreLookAndFeel();
		if(propertyEquals(PROP_DISPLAY_SPLASH, "true", "true")) {
			showSplashWindow();
		}

		if (getLoadFields() == LOAD_FIELDS_STARTUP) {
			loadFieldsForAllServers();
		}

		final Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		// Need to add status panel first so later panels
		// can have access to the status labels
		contentPane.add(new StatusPanel(), BorderLayout.SOUTH);
		contentPane.add(queryToolBar, BorderLayout.NORTH);
		resultPanel = new ResultPanel();
		final @NotNull JPanel queryPanel = new QueryPanel(this);
		contentPane.add(new ContentPanel(queryPanel, resultPanel), BorderLayout.CENTER);

		showToolBar(propertyEquals(PROP_DISPLAY_TOOLBAR, "true", "true"));

		defaultPane.setJMenuBar(new MainMenu(this));

		log("JavaPH initialized (Application) Mode)");
		log("Initializing default server");

		serverComboBox.setSelectedItem(QiServerManager.getDefaultServer());

		log("Default server initialized");

		showDefaultStatus();
		restoreLookAndFeel(getContentPane());
		frame.setContentPane(getContentPane());
		frame.setIconImage(getImageIcon("img/ph-icon-smaller.gif").getImage());

		frame.setSize(frameWidth, frameHeight);
		frame.setTitle("JavaPH");

		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		// if no previous X/Y position specified or if the previous X/Y
		// position is somehow off the screen then just center the frame
		frameX = (frameX < 0 || frameX > screenSize.width) ? (screenSize.width/2 - (frameWidth/2)) : frameX;
		frameY = (frameY < 0 || frameY > screenSize.height) ? (screenSize.height/2 - (frameHeight/2)) : frameY;

		frame.setLocation(frameX, frameY);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.addWindowListener(this);
	}

	public void findText(final @NotNull String text, final boolean caseSensitive, final boolean wrap)
	{
		if (resultPanel.getSelectedIndex() == RESULT_TEXT_TAB)
		{
			findTextInTextArea(resultText, text, caseSensitive, wrap);
		}
		else if (resultPanel.getSelectedIndex() == SYSTEM_LOG_TAB)
		{
			findTextInTextArea(logText, text, caseSensitive, wrap);
		}
		else if (resultPanel.getSelectedIndex() == RESULT_TABLE_TAB)
		{
			findTextInTable(resultTable, text, caseSensitive, wrap);
		}
	}

	private void findTextInTable(final @NotNull JTable table, final @NotNull String text, final boolean caseSensitive, final boolean wrap)
	{

		// if there aren't any rows or columns then we aren't going to find anything
		if (table.getColumnCount() <= 0 || table.getRowCount() <= 0)
		{
			log("Info: Cannot perform a search on an empty table");
			return;
		}
		
		// start from the next column if a cell was already selected (hence the +1 to the count)
		int startCol = (table.getSelectedColumn() < 0) ? 0 : table.getSelectedColumn() + 1;
		int startRow = (table.getSelectedRow() < 0) ? 0 : table.getSelectedRow();

		// if we were in the last column (and have now been bumped out by the +1 above)
		// then see if we can go to the next row, if not then the last cell must be selected
		boolean inLastCell = false;
		if (startCol > table.getColumnCount() - 1)
		{
			startCol = 0;
			startRow++;

			if (startRow > table.getRowCount() - 1)
			{
				log("Info: Cannot search past the last cell of a table");
				
				if (wrap) {
					inLastCell = true;
				} else {
					return;
				}
			}
		}

		if (!inLastCell)
		{
			for (int r = startRow; r < table.getRowCount(); r++)
			{
				// if at the starting row then continue from the starting column
				// otherwise start from the first column
				for (int c = (r == startRow) ? startCol : 0; c < table.getColumnCount(); c++)
				{
					final @Nullable Object anObject = table.getValueAt(r, c);
					
					if (anObject == null)
					{
						continue;
					}
					else if (caseSensitive && anObject.toString().contains(text))
					{
						table.changeSelection(r, c, false, false);
						return;
					}
					else if (!caseSensitive && anObject.toString().toUpperCase().contains(text.toUpperCase()))
					{
						table.changeSelection(r, c, false, false);
						return;
					}
				}
			}
		}

		// haven't returned yet so must not have found anything -
		// so if the whole table hasn't already been searched and
		// wrap is allowed then lets start again from the beginning
		
		if (wrap && (startRow > 0 || startCol > 0))
		{
			startRow = 0;
			startCol = 0;

			for (int r = startRow; r < table.getRowCount(); r++)
			{
				// if at the starting row then continue from the starting column
				// otherwise start from the first column
				for (int c = (r == startRow) ? startCol : 0; c < table.getColumnCount(); c++)
				{
					final @Nullable Object anObject = table.getValueAt(r, c);
				
					if (anObject == null)
					{
						continue;
					}
					else if (caseSensitive && anObject.toString().contains(text))
					{
						table.changeSelection(r, c, false, false);
						return;
					}
					else if (!caseSensitive && anObject.toString().toUpperCase().contains(text.toUpperCase()))
					{
						table.changeSelection(r, c, false, false);
						return;
					}
				}
			}
		}
	}

	private void findTextInTextArea(final @NotNull JTextArea textArea, final @NotNull String text, final boolean caseSensitive, final boolean wrap)
	{
		int startIndex;

		// if nothing or everything is selected then start search from the
		// beginning otherwise start search from the end of the selection
		if (textArea.getSelectedText() == null) {
			startIndex = 0;
		} else if (textArea.getSelectionStart() == 0 && textArea.getSelectionEnd() == textArea.getText().length() - 1) {
			startIndex = 0;
		} else {
			startIndex = textArea.getSelectionEnd();
		}

		int location = getLocation(textArea, text, caseSensitive, startIndex);

		if (location > 0)
		{
			textArea.select(location, location + text.length());
			textArea.getCaret().setSelectionVisible(true);
		}
		else if (wrap && startIndex > 0)
		{
			startIndex = 0;

			location = getLocation(textArea, text, caseSensitive, startIndex);

			if (location > 0)
			{
				textArea.select(location, location + text.length());
				textArea.getCaret().setSelectionVisible(true);
			}
		}
	}

	private int getLocation(final @NotNull JTextArea textArea, final @NotNull String text, final boolean caseSensitive, final int startIndex)
	{
		final int location;
		if (caseSensitive)
		{
			location = textArea.getText().indexOf(text, startIndex);
		}
		else
		{
			location = textArea.getText().toUpperCase().indexOf(text.toUpperCase(), startIndex);
		}
		return location;
	}

	public @NotNull String getCommand()
	{
		final @NotNull StringBuilder out = new StringBuilder();
		
		out.append(commands[commandComboBox.getSelectedIndex()].getCommand());
		
		if (queryComboBox.isEnabled() && queryComboBox.getSelectedItem() != null)
		{
			out.append(queryComboBox.getSelectedItem().toString());
		}
		
		if (fieldRadioGroup.isEnabled())
		{
			if (fieldRadioGroup.getSelectedIndex() == FIELD_DEFAULT)
			{
				// Don't do anything if default fields selected
			}
			else if (fieldRadioGroup.getSelectedIndex() == FIELD_ALL)
			{
				out.append(" return all");
			}
			else if (fieldRadioGroup.getSelectedIndex() == FIELD_CUSTOM)
			{
				final @NotNull int[] selectedFields = fieldList.getSelectedIndices();
				
				// will return default list of fields if nothing selected
				if (selectedFields.length > 0)
				{
					out.append(" return");

					for (final int selectedField : selectedFields) {
						out.append(" ");
						out.append(fieldList.getModel().getElementAt(selectedField).getName());
					}
				}
				else
				{
					log("Either no fields available or no fields selected, returning default fields instead");
				}
			}
		}
		
		return out.toString();
	}

	public void setCommandComboBoxSelectedIndex(final int index)
	{
		commandComboBox.setSelectedIndex(index);
	}

	public JRootPane getDefaultPane()
	{
		return defaultPane;
	}

	public @Nullable Connection getConnection()
	{
		return connection;
	}

	public @NotNull String getFieldSeparator()
	{
		return fieldSeparator;
	}

	private int getIntProperty(final @NotNull String key, final int defaultValue)
	{
		try
		{
			final @Nullable String stringValue = getProperty(key);

			if (stringValue == null) {
				return defaultValue;
			}

			final int intValue = Integer.parseInt(stringValue);
			return intValue;
		}
		catch (final @NotNull NumberFormatException e)
		{
			return defaultValue;
		}
	}

	public int getIntPropertyDefault(final @NotNull String key, final int defaultValue)
	{
		int intValue;

		try
		{
			final @Nullable String stringValue = getPropertyDefault(key);

			if (stringValue == null) {
				return defaultValue;
			}
			
			intValue = Integer.parseInt(stringValue);
		}
		catch (final @NotNull NumberFormatException e)
		{
			intValue = defaultValue;
		}
		
		return intValue;
	}

	public @Nullable String getLastCustomSeparator()
	{
		return customFieldSeparator;
	}

	public int getLoadFields()
	{
		return loadFields;
	}

	public @NotNull JTextArea getLogText()
	{
		return logText;
	}

	private @Nullable String getProperty(final @NotNull String key)
	{
		return properties.getProperty(key);
	}

	private @NotNull String getProperty(final @NotNull String key, final @NotNull String defaultValue)
	{
		return properties.getProperty(key, defaultValue);
	}

	public @Nullable String getPropertyDefault(final @NotNull String key)
	{
		return defaultProperties.getProperty(key);
	}

	private @NotNull String getPropertyDefault(final @NotNull String key, final @NotNull String defaultValue)
	{
		return defaultProperties.getProperty(key, defaultValue);
	}

	public int getQueryRuntime()
	{
		return queryRuntime;
	}

	public @NotNull JButton getQueryButton()
	{
		return queryButton;
	}

	public QueryComboBox getQueryComboBox()
	{
		return queryComboBox;
	}

	public boolean isQueryCanceled()
	{
		return queryProgressMonitor.isCanceled();
	}

	public void setQueryProgress(final int progress)
	{
		queryProgressMonitor.setProgress(progress);
	}

	public void closeQueryProgressMonitor()
	{
		queryProgressMonitor.close();
	}

	public @NotNull JToolBar getQueryToolBar()
	{
		return queryToolBar;
	}

	public @NotNull JTabbedPane getResultPanel()
	{
		return resultPanel;
	}

	public ResultTable getResultTable()
	{
		return resultTable;
	}

	public @NotNull JTextArea getResultText()
	{
		return resultText;
	}

	public @Nullable Server getServer()
	{
		return (Server)serverComboBox.getSelectedItem();
	}

	@Override
	public @NotNull String getURL(final @NotNull String location)
	{
		return location;
		/*
		TODO fix this
		@NotNull final URL url = (new File(location)).toURI().toURL();
		return url;
		*/
	}

	@Override
	public @NotNull ImageIcon getImageIcon(final @NotNull String location)
	{
		final @NotNull ImageIcon icon = new ImageIcon((location));
		return icon;
	}

	@Override
	public void init()
	{
	}

	private void initProperties()
	{
		// First load default properties - prints exception and exits if not found 
		loadDefaultProperties();
		
		// Next load custom properties, overrides defaults - ignores if not found
		loadProperties();

		setLoadFields(getIntProperty(PROP_LOAD_FIELDS, LOAD_FIELDS_DEF));
		setQueryRuntime(getIntProperty(PROP_QUERY_RUNTIME, QUERY_RUNTIME_DEF));
		setSavePosition(propertyEquals(PROP_SAVE_POSITION, "true", "true"));
	}

	private void initServers()
	{
		QiServerManager.loadAllServers();
		servers  = QiServerManager.getServers();
	}

	public boolean isFieldQuoted()
	{
		return fieldQuoted;
	}

	public boolean isSavePosition()
	{
		return savePosition;
	}

	private void loadDefaultProperties()
	{

		try
		{
			// TODO can I reuse the same stream?
			try (@NotNull FileInputStream inStream = new FileInputStream(PROP_FILE_DEF)) {
				defaultProperties.load(inStream);
			}
			try (@NotNull FileInputStream inStream1 = new FileInputStream(PROP_FILE_DEF)) {
				properties.load(inStream1);
			}
		}
		catch (final @NotNull FileNotFoundException e)
		{
			errLogger.printStackTrace(e);
			errLogger.println("FileNotFound occurred when trying to load properties from " + PROP_FILE_DEF);
			System.exit(1);
		}
		catch (final @NotNull IOException e)
		{
			errLogger.printStackTrace(e);
			errLogger.println("IOException occurred when trying to load properties from " + PROP_FILE_DEF);
			System.exit(1);
		}
	}

	private void loadFieldsForAllServers()
	{
		if (servers != null && !servers.isEmpty())
		{

			final @NotNull ProgressMonitor fieldProgressMonitor = new ProgressMonitor(defaultPane, "Loading Fields for Server", "", 0, servers.size());
			
			for (int i = 0; i < servers.size(); i++)
			{
				final Server server = servers.get(i);
				fieldProgressMonitor.setNote(server.getExpandedName());
				server.loadFields();
				fieldProgressMonitor.setProgress(i + 1);
				
				if (fieldProgressMonitor.isCanceled()) {
					break;
				}
			}
			
			fieldProgressMonitor.close();
		}
	}

	private void loadProperties()
	{

		try (@NotNull FileInputStream inStream = new FileInputStream(PROP_FILE))
		{
			properties.load(inStream);
		}
		catch (final @NotNull FileNotFoundException e)
		{
		}
		catch (final @NotNull IOException e)
		{
		}
	}

	public void log(final String logMessage)
	{
		final @NotNull Date date = new Date(System.currentTimeMillis());
		logText.append(date.toString());
		logText.append(" :: ");
		logText.append(logMessage);
		logText.append("\n");
	}

	public boolean propertyDefaultEquals(final @NotNull String key, final @NotNull String defaultValue, final @NotNull String equalsValue)
	{
		final @NotNull String value = getPropertyDefault(key, defaultValue);
		return value.equals(equalsValue);
	}

	public boolean propertyEquals(final @NotNull String key, final @NotNull String defaultValue, final @NotNull String equalsValue)
	{
		final @NotNull String value = getProperty(key, defaultValue);
		return value.equals(equalsValue);
	}

	public void restoreLookAndFeel()
	{
		restoreLookAndFeel(getProperty(PROP_DEFAULT_LNF), defaultPane);
	}

	private void restoreLookAndFeel(final @NotNull Component component)
	{
		restoreLookAndFeel(getProperty(PROP_DEFAULT_LNF), component);
	}

	public void restoreLookAndFeel(final String lookAndFeel)
	{
		restoreLookAndFeel(lookAndFeel, defaultPane);
	}

	private void restoreLookAndFeel(@Nullable String lookAndFeel, final @NotNull Component component)
	{

		final @NotNull Component[] otherComponents = {
			aboutDialog, colListPanel, fieldListPanel, findDialog, propertiesDialog, queryToolBar, splashWindow
		};
		
		if (lookAndFeel == null)
		{
			lookAndFeel = UIManager.getSystemLookAndFeelClassName();
			logger.println("No look and feel specified, using system default (" + lookAndFeel + ")");
		}

		try
		{
			UIManager.setLookAndFeel(lookAndFeel);
			SwingUtilities.updateComponentTreeUI(component);
			
			// Must also include the dialogs and toolbars
			// due to the weirdness of potentially running
			// as either an application or an applet
			for (final Component otherComponent : otherComponents)
			{
				SwingUtilities.updateComponentTreeUI(otherComponent);
			}
			
			return;
		}
		catch(final @NotNull Exception e)
		{
			// If we cannot set the look and feel to
			// what was specified in the properties file
			// then just continue and set system default
			logger.printStackTrace(e);
			logger.println("Exception occurred when trying to set custom look and feel");
		}
		
		try
		{
			// UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(component);

			// Must also include the dialogs and toolbars
			// due to the weirdness of potentially running
			// as either an application or an applet
			for (final Component otherComponent : otherComponents)
			{
				SwingUtilities.updateComponentTreeUI(otherComponent);
			}

			return;
		}
		catch(final @NotNull Exception e)
		{
			// If we cannot set the look and feel to
			// what is set as the system look and feel
			// then just continue and allow the default
			// cross platform look at feel to be used (metal)
			logger.printStackTrace(e);
			logger.println("Exception occurred when trying to set default look and feel");
		}
	}

	public void setFieldQuoted(final boolean quoted)
	{
		fieldQuoted = quoted;
	}

	public void setFieldSeparator(final @NotNull String separator)
	{
		if (separator.isEmpty()) {
			throw new IllegalArgumentException("Error: null or empty string passed to setFieldSeparator");
		}
			
		// log("Changing field separator string from " + fieldSeparator + " to " + separator);
		fieldSeparator = separator;
		
		// if not a standard separator (comma or tab) then set the custom separator as well
		if (!separator.equals(COMMA_SEPARATOR) && !separator.equals(TAB_SEPARATOR)) {
			customFieldSeparator = separator;
		}
	}

	public void setLoadFields(final int load)
	{
		if (load != LOAD_FIELDS_MANUAL && load != LOAD_FIELDS_SELECTED && load != LOAD_FIELDS_STARTUP)
		{
			log("Invalid load fields value " + load + " specified, using default value " + LOAD_FIELDS_DEF);
			loadFields = LOAD_FIELDS_DEF;
		}
		else
		{
			loadFields = load;
		}	
	}

	public void setProperty(final String key, final String value)
	{
		properties.setProperty(key, value);
	}

	public void setProperty(final String key, final int value)
	{
		setProperty(key, String.valueOf(value));
	}

	public void setQueryRuntime(final int runtime)
	{
		if (runtime > QUERY_RUNTIME_MAX)
		{
			log("Query runtime " + runtime + " too high, using maximum value " + QUERY_RUNTIME_MAX);
			queryRuntime = QUERY_RUNTIME_MAX;
		}
		else if (runtime < QUERY_RUNTIME_MIN)
		{
			log("Query runtime " + runtime + " too low, using minimum value " + QUERY_RUNTIME_MIN);
			queryRuntime = QUERY_RUNTIME_MIN;
		}
		else
		{
			queryRuntime = runtime;
		}
	}

	public void setSavePosition(final boolean save)
	{
		savePosition = save;
	}

	public void showErrorDialog(final @NotNull String message, final @NotNull String title)
	{
		JOptionPane.showMessageDialog(getDefaultPane(), message, title, JOptionPane.ERROR_MESSAGE);
	}

	public void showWarningDialog(final @NotNull String message, final @NotNull String title)
	{
		JOptionPane.showMessageDialog(getDefaultPane(), message, title, JOptionPane.WARNING_MESSAGE);
	}

	public void showInformationDialog(final @NotNull String message, final @NotNull String title)
	{
		JOptionPane.showMessageDialog(getDefaultPane(), message, title, JOptionPane.INFORMATION_MESSAGE);
	}

	public void showAboutDialog()
	{
		aboutDialog.setLocationRelativeTo(defaultPane);
		aboutDialog.setVisible(true);
	}

	public void showBrowserDialog()
	{
		final @NotNull Frame frame = JOptionPane.getFrameForComponent(defaultPane);
		Browser.dialogConfiguration(frame);
	}

	public void showDefaultStatus()
	{
		showStatus(APP_DEFAULT_STATUS);
	}

	public void showFindDialog()
	{
		findDialog.setLocationRelativeTo(defaultPane);
		findDialog.setTitle("Find Text in " + TAB_LABELS[getResultPanel().getSelectedIndex()]);
		findDialog.setVisible(true);
	}

	public void showLog(final boolean show)
	{
		final int location = resultPanel.indexOfTab(SYSTEM_LOG_LABEL);

		// If requested to show the log and it's not already
		// there then add the tab and select it
		if (show && location < 0)
		{
			resultPanel.add(SYSTEM_LOG_LABEL, logTextPanel);
			resultPanel.setSelectedIndex(resultPanel.getTabCount() - 1);
		}
		// If requested to hide the log and it's
		// there then remove the tab
		else if (!show && location >= 0)
		{
			resultPanel.removeTabAt(location);
		}
	}

	public void showPropertiesDialog()
	{
		propertiesDialog.setLocationRelativeTo(defaultPane);
		propertiesDialog.show();
	}

	private void showSplashWindow()
	{
		splashWindow.setVisible(true);
			
		try
		{
			Thread.sleep(SPLASH_DISPLAY);
		}
		catch (final @NotNull InterruptedException e)
		{
		}
			
		splashWindow.dispose();
	}

	@Override
	public void showStatus(final String status)
	{
		if (statusLabel != null)
		{
			statusLabel.setText(" " + status);
		}
	}

	public void showToolBar(final boolean show)
	{
		queryToolBar.setVisible(show);
	}
	
	public void storeLookAndFeel()
	{
		setProperty(PROP_DEFAULT_LNF, UIManager.getLookAndFeel().getClass().getName());
		storeProperties();
	}

	public void storeProperties()
	{
		try
		{
			properties.store(new FileOutputStream(PROP_FILE), PROP_HEADER);				
		}
		catch (final @NotNull FileNotFoundException e)
		{
			errLogger.printStackTrace(e);
			errLogger.println("FileNotFound occurred when trying to store properties to " + PROP_FILE);
		}
		catch (final @NotNull IOException e)
		{
			errLogger.printStackTrace(e);
			errLogger.println("IOException occurred when trying to store properties to " + PROP_FILE);
		}
	}
}
