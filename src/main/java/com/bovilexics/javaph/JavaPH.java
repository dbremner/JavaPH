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
import com.bovilexics.javaph.configuration.PropertyCollection;
import com.bovilexics.javaph.logging.ErrLogger;
import com.bovilexics.javaph.logging.ErrLoggerImpl;
import com.bovilexics.javaph.logging.StatusLogger;
import com.bovilexics.javaph.models.QueryComboBoxModel;
import com.bovilexics.javaph.models.ResultTableModel;
import com.bovilexics.javaph.qi.Command;
import com.bovilexics.javaph.qi.Connection;
import com.bovilexics.javaph.qi.Field;
import com.bovilexics.javaph.qi.FieldState;
import com.bovilexics.javaph.qi.QiServerFileException;
import com.bovilexics.javaph.qi.Server;
import com.bovilexics.javaph.qi.ServerManager;
import com.bovilexics.javaph.threads.QueryThreadRunnable;
import com.bovilexics.javaph.ui.AboutDialog;
import com.bovilexics.javaph.ui.ContentPanel;
import com.bovilexics.javaph.ui.ControlTabDispatcher;
import com.bovilexics.javaph.ui.CustomButtonGroup;
import com.bovilexics.javaph.ui.FieldSelection;
import com.bovilexics.javaph.ui.FindDialog;
import com.bovilexics.javaph.ui.IconProvider;
import com.bovilexics.javaph.ui.ListDataAdapter;
import com.bovilexics.javaph.ui.LoadFields;
import com.bovilexics.javaph.ui.MainMenu;
import com.bovilexics.javaph.ui.MouseEnterExitListener;
import com.bovilexics.javaph.ui.NullKeyAdapter;
import com.bovilexics.javaph.ui.NullMouseAdapter;
import com.bovilexics.javaph.ui.PropertiesDialog;
import com.bovilexics.javaph.ui.QueryComboBox;
import com.bovilexics.javaph.ui.QueryRuntime;
import com.bovilexics.javaph.ui.QueryToolBar;
import com.bovilexics.javaph.ui.ResultTable;
import com.bovilexics.javaph.ui.ServerRenderer;
import com.bovilexics.javaph.ui.SplashWindow;
import com.bovilexics.javaph.ui.Tab;
import com.bovilexics.javaph.ui.TextFieldComboBoxEditor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
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
import javax.swing.WindowConstants;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
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
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

import static com.bovilexics.javaph.JavaPHConstants.APP_DEFAULT_HEIGHT;
import static com.bovilexics.javaph.JavaPHConstants.APP_DEFAULT_STATUS;
import static com.bovilexics.javaph.JavaPHConstants.APP_DEFAULT_WIDTH;
import static com.bovilexics.javaph.JavaPHConstants.APP_MIN_HEIGHT;
import static com.bovilexics.javaph.JavaPHConstants.APP_MIN_WIDTH;
import static com.bovilexics.javaph.JavaPHConstants.COMMA_SEPARATOR;
import static com.bovilexics.javaph.JavaPHConstants.CUSTOM_SEPARATOR;
import static com.bovilexics.javaph.JavaPHConstants.FIELD_LABEL_PREFIX;
import static com.bovilexics.javaph.JavaPHConstants.PROP_APP_HEIGHT;
import static com.bovilexics.javaph.JavaPHConstants.PROP_APP_WIDTH;
import static com.bovilexics.javaph.JavaPHConstants.PROP_APP_X_POSITION;
import static com.bovilexics.javaph.JavaPHConstants.PROP_APP_Y_POSITION;
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
import static com.bovilexics.javaph.JavaPHConstants.SERVER_LABEL_PREFIX;
import static com.bovilexics.javaph.JavaPHConstants.SPLASH_DISPLAY;
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
public final class JavaPH extends JApplet implements IconProvider, WindowListener, StatusLogger
{
	private final @NotNull ErrLogger errLogger = new ErrLoggerImpl();

	// Custom widgets and other private stuff

	private final @NotNull JFrame frame = new JFrame();

	private boolean fieldQuoted = false;
	private boolean savePosition = false;

	private @NotNull LoadFields loadFields = LoadFields.Selected;
	private int queryRuntime = QueryRuntime.DEFAULT.getValue();

	private final @NotNull QueryPanel queryPanel;

	private final @NotNull AboutDialog aboutDialog;
	private final @NotNull FindDialog findDialog;
	private final @NotNull Font fixedWidthFont = new Font("Monospaced", Font.PLAIN, 12);
	private final @NotNull ServerManager serverManager;
	private final @NotNull PropertyCollection defaultProperties;
	private final @NotNull PropertyCollection properties;
	private final @NotNull PropertiesDialog propertiesDialog;
	private final @NotNull Command[] commands;
	private @Nullable Connection connection;
	private final @NotNull QueryComboBox queryComboBox = new QueryComboBox();
	private final @NotNull QueryToolBar queryToolBar;
	private final @NotNull ResultTable resultTable = new ResultTable();
	private final @NotNull SplashWindow splashWindow;
	private @NotNull String customFieldSeparator = CUSTOM_SEPARATOR;
	private @NotNull String fieldSeparator = COMMA_SEPARATOR;
	private final @NotNull TextFieldComboBoxEditor queryComboBoxEditor;
	private final @NotNull Vector<Server> servers;

	// Swing widgets and stuff

	private final @NotNull JButton queryButton = new JButton(JavaPHConstants.EXECUTE_QUERY);
	private final @NotNull JComboBox<Command> commandComboBox;
	private final @NotNull JComboBox<Server> serverComboBox;
	private final @NotNull JLabel portStatusLabel = new JLabel();
	private final @NotNull JLabel serverStatusLabel = new JLabel();
	private final @NotNull JLabel statusLabel = new JLabel();
	private final @NotNull JList<String> colList = new JList<>(new DefaultListModel<>());
	private final @NotNull JList<Field> fieldList = new JList<>(new DefaultListModel<>());
	private final @NotNull JPanel colListPanel = new JPanel(new BorderLayout());
	private final @NotNull JPanel fieldListPanel = new JPanel(new BorderLayout());
	private final @NotNull JPanel logTextPanel = new JPanel(new BorderLayout());
	private final @NotNull JRootPane defaultPane;
	private final @NotNull JTabbedPane resultPanel;
	private final @NotNull JTextArea logText = new JTextArea();
	private final @NotNull JTextArea resultText = new JTextArea();
	private final @NotNull IconProvider iconProvider;

	@Override
	public void windowOpened(final @NotNull WindowEvent e)
	{
	}

	@Override
	public void windowClosing(final @NotNull WindowEvent e)
	{
	}

	@Override
	public void windowClosed(final @NotNull WindowEvent e)
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
			if (properties.getProperty(PROP_APP_HEIGHT, APP_DEFAULT_HEIGHT) != APP_DEFAULT_HEIGHT)
			{
				needToStore = true;
				setProperty(PROP_APP_HEIGHT, APP_DEFAULT_HEIGHT);
			}
			if (properties.getProperty(PROP_APP_WIDTH, APP_DEFAULT_WIDTH) != APP_DEFAULT_WIDTH)
			{
				needToStore = true;
				setProperty(PROP_APP_WIDTH, APP_DEFAULT_WIDTH);
			}
			if (properties.getProperty(PROP_APP_X_POSITION, -1) != -1)
			{
				needToStore = true;
				setProperty(PROP_APP_X_POSITION, -1);
			}
			if (properties.getProperty(PROP_APP_Y_POSITION, -1) != -1)
			{
				needToStore = true;
				setProperty(PROP_APP_Y_POSITION, -1);
			}
		}

		if (needToStore)
		{
			storeProperties();
		}

		System.exit(0);
	}

	@Override
	public void windowIconified(final @NotNull WindowEvent e)
	{
	}

	@Override
	public void windowDeiconified(final @NotNull WindowEvent e)
	{
	}

	@Override
	public void windowActivated(final @NotNull WindowEvent e)
	{
	}

	@Override
	public void windowDeactivated(final @NotNull WindowEvent e)
	{
	}

	private final class QueryPanel extends JPanel
	{
		private @Nullable ProgressMonitor queryProgressMonitor;

		private final @NotNull JavaPH parent;
		private final @NotNull JButton fieldListMoveDnButton = new JButton(JavaPHConstants.MOVE_DOWN);
		private final @NotNull JButton fieldListMoveUpButton;
		private final @NotNull JButton fieldListSelectAllButton;
		private final @NotNull JButton fieldListSelectNoneButton;
		private final @NotNull JButton fieldLoadButton;
		private final @NotNull JButton fieldCustomButton;
		private final @NotNull ImageIcon fieldCustomOff;
		private final @NotNull ImageIcon fieldCustomOn;
		private final @NotNull ImageIcon fieldLoadOff;
		private final @NotNull ImageIcon fieldLoadOn;
		private final @NotNull JRadioButton fieldCustomRadioButton = new JRadioButton(JavaPHConstants.CUSTOM_FIELDS);
		private final @NotNull CustomButtonGroup fieldRadioGroup = new CustomButtonGroup();

		@SuppressWarnings("WeakerAccess")
		QueryPanel(final @NotNull JavaPH javaph)
		{
			parent = javaph;

			fieldCustomOff = getImageIcon("img/field-custom-off.gif");
			fieldCustomOn = getImageIcon("img/field-custom-on.gif");
			fieldLoadOff = getImageIcon("img/field-load-off.gif");
			fieldLoadOn = getImageIcon("img/field-load-on.gif");
			fieldLoadButton = new JButton(fieldLoadOff);
			fieldCustomButton = new JButton(fieldCustomOff);
			fieldListSelectAllButton = new JButton(JavaPHConstants.SELECT_ALL);
			fieldListSelectNoneButton = new JButton(JavaPHConstants.DESELECT_ALL);
			fieldListMoveUpButton = new JButton(JavaPHConstants.MOVE_UP);

			initFieldListPanel();

			setLayout(new BorderLayout());

			add(getQueryLabelPanel(), BorderLayout.WEST);
			add(getQueryContentPanel(), BorderLayout.CENTER);
			add(getQueryButtonPanel(), BorderLayout.EAST);
		}

		@Contract(pure = true)
		private boolean isQueryCanceled()
		{
			assert queryProgressMonitor != null;
			return queryProgressMonitor.isCanceled();
		}

		private void setQueryProgress(final int progress)
		{
			assert queryProgressMonitor != null;
			queryProgressMonitor.setProgress(progress);
		}

		private void closeQueryProgressMonitor()
		{
			assert queryProgressMonitor != null;
			queryProgressMonitor.close();
		}

		private @NotNull String getCommand()
		{
			@NonNls
			final @NotNull StringBuilder out = new StringBuilder();

			out.append(commands[commandComboBox.getSelectedIndex()].getCommand());

			if (queryComboBox.isEnabled() && queryComboBox.getSelectedItem() != null)
			{
				out.append(queryComboBox.getSelectedItem().toString());
			}

			if (fieldRadioGroup.isEnabled())
			{
				final int index = fieldRadioGroup.getSelectedIndex();
				final @NotNull FieldSelection selection = FieldSelection.fromValue(index);

				switch (selection)
				{
					case DEFAULT:
					{
						// Don't do anything if default fields selected
						break;
					}
					case ALL:
					{
						out.append(" return all");
						break;
					}
					case CUSTOM:
					{
						final @NotNull int[] selectedFields = fieldList.getSelectedIndices();

						// will return default list of fields if nothing selected
						if (selectedFields.length > 0)
						{
							out.append(" return");

							for (final int selectedField : selectedFields)
							{
								out.append(" ");
								out.append(fieldList.getModel().getElementAt(selectedField).getName());
							}
						}
						else
						{
							log(JavaPHConstants.EITHER_NO_FIELDS_AVAILABLE_OR_NO_FIELDS_SELECTED_RETURNING_DEFAULT_FIELDS_INSTEAD);
						}
						break;
					}
					default:
						assert false;
				}
			}

			return out.toString();
		}

		private @Nullable Server getServer()
		{
			return (Server)serverComboBox.getSelectedItem();
		}

		private @NotNull JPanel getQueryButtonPanel()
		{
			final @NotNull JPanel queryButtonPanel = new JPanel(new FlowLayout());

			queryButton.setMnemonic(KeyEvent.VK_Q);
			queryButton.setToolTipText(JavaPHConstants.CLICK_THIS_TO_START_RUNNING_THE_QUERY);
			queryButton.addActionListener(ae ->
			{
				if (queryComboBox.isEnabled())
				{
					final Object selectedItem = queryComboBox.getEditor().getItem();
					final @NotNull QueryComboBoxModel model = queryComboBox.getModel();

					if (selectedItem != null && !selectedItem.toString().isEmpty())
					{
						if (model.getIndexOf(selectedItem) < 0)
						{
							model.addElement(selectedItem);
						}

						model.setSelectedItem(selectedItem);
					}
				}

				queryProgressMonitor = new ProgressMonitor(defaultPane, JavaPHConstants.EXECUTING_QUERY, getCommand(), 0, getQueryRuntime());
				final int runtime = parent.getQueryRuntime();
				final @NotNull String command = getCommand();
				final @Nullable Server server = getServer();
				assert server != null;
				final @NotNull Connection connection = server.open();
				final @NotNull Runnable runnable = new QueryThreadRunnable(parent, runtime, command, connection);
				final @NotNull Thread qt = new Thread(runnable);
				qt.start();
			});

			queryButtonPanel.add(queryButton);

			return queryButtonPanel;
		}

		private @NotNull JPanel getQueryCommandPanel()
		{
			final @NotNull JPanel queryCommandPanel = new JPanel(new BorderLayout());

			commandComboBox.addActionListener(ae ->
			{
				queryComboBox.setSelectedIndex(-1);
				queryComboBox.setEnabled(commands[commandComboBox.getSelectedIndex()].isTextEditable());
				final boolean isQueryCommand = commandComboBox.getSelectedIndex() == QUERY_COMMAND;
				fieldRadioGroup.setEnabled(isQueryCommand);
				fieldCustomButton.setEnabled(isQueryCommand);
				fieldLoadButton.setEnabled(isQueryCommand);
			});

			queryComboBoxEditor.getEditorComponent().addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyReleased(final @NotNull KeyEvent e)
				{
					if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE)
					{
						queryComboBox.hidePopup();
					}
					else if (!e.isActionKey())
					{
						final @Nullable Object obj = queryComboBoxEditor.getItem();
						assert obj != null;
						final String filter = obj.toString();

						queryComboBox.hidePopup();
						queryComboBox.getModel().filterElements(filter);

						if (queryComboBox.getItemCount() > 0)
						{
							queryComboBox.showPopup();
						}
					}
					else if (queryComboBox.getItemCount() == 1 && (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN))
					{
						// force reselection of first item if only one item
						// in the list and the up or down arrow is pressed
						queryComboBox.setSelectedIndex(-1);
						queryComboBox.setSelectedIndex(0);
					}
				}
			});

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

			final @NotNull JRadioButton fieldDefaultRadioButton = new JRadioButton(JavaPHConstants.DEFAULT_FIELDS);
			final @NotNull JRadioButton fieldAllRadioButton = new JRadioButton(JavaPHConstants.ALL_FIELDS);

			fieldRadioGroup.add(fieldDefaultRadioButton);
			fieldRadioGroup.add(fieldAllRadioButton);
			fieldRadioGroup.add(fieldCustomRadioButton);

			fieldDefaultRadioButton.setSelected(true);

			fieldCustomButton.setBorder(BorderFactory.createEtchedBorder());
			fieldCustomButton.setFocusable(false);
			fieldCustomButton.setToolTipText(JavaPHConstants.CHOOSE_THE_FIELDS_THAT_QUERIES_WILL_RETURN);
			fieldCustomButton.addMouseListener(new MouseEnterExitListener(fieldCustomButton, fieldCustomOn, fieldCustomOff));
			fieldCustomButton.addActionListener(ae ->
			{
				final @NotNull int[] prevSelections = fieldList.getSelectedIndices();
				final @Nullable Server server = getServer();
				assert server != null;
				final int option = JOptionPane.showConfirmDialog(getDefaultPane(), fieldListPanel, String.format(JavaPHConstants.FIELD_LIST_FOR_S, server.toString()), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

				if (option != JOptionPane.OK_OPTION)
				{
					fieldList.setSelectedIndices(prevSelections);
					return;
				}
				fieldCustomRadioButton.setSelected(true);
			});

			fieldLoadButton.setBorder(BorderFactory.createEtchedBorder());
			fieldLoadButton.setFocusable(false);
			fieldLoadButton.addMouseListener(new MouseEnterExitListener(fieldLoadButton, fieldLoadOn, fieldLoadOff));
			fieldLoadButton.addActionListener(ae ->
			{
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
			serverComboBox.setRenderer(new ServerRenderer(parent));
			serverComboBox.addActionListener(ae ->
			{
				final @Nullable Server server = getServer();
				assert server != null;
				final @NotNull String serverText = server.getServer();

				final int portInt = server.getPort();

				serverStatusLabel.setText(String.format(JavaPHConstants.SERVER_S, serverText));
				final @NotNull String portText = String.valueOf(portInt);
				portStatusLabel.setText(String.format(JavaPHConstants.PORT_S, portText));

				connection = server.open();

				if (getLoadFields() == LoadFields.Selected && server.getFieldState() != FieldState.FIELD_LOAD_ERROR && server.getFieldState() != FieldState.FIELD_LOAD_TRUE)
				{
					serverComboBox.hidePopup();
					loadFieldsForServer(server);
				}

				populateFieldList(server);
			});
			final @NotNull Optional<String> optional = properties.getProperty(PROP_DEFAULT_SERVER);
			optional.ifPresent(serverManager::setDefaultServer);

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

			fieldListSelectAllButton.addActionListener(
					ae -> fieldList.setSelectionInterval(0, fieldList.getModel().getSize() - 1));

			fieldListSelectNoneButton.addActionListener(ae -> fieldList.clearSelection());


			fieldListMoveUpButton.addActionListener(ae ->
			{
				final @NotNull int[] selections = fieldList.getSelectedIndices();

				for (int i = 0; i < selections.length; i++)
				{
					final Field toMove = fieldList.getModel().getElementAt(selections[i]);
					((DefaultListModel<Field>)fieldList.getModel()).removeElementAt(selections[i]);
					selections[i]--;
					((DefaultListModel<Field>)fieldList.getModel()).insertElementAt(toMove, selections[i]);
				}

				fieldList.setSelectedIndices(selections);
			});

			fieldListMoveDnButton.addActionListener(ae ->
			{
				final @NotNull int[] selections = fieldList.getSelectedIndices();

				for (int i = selections.length - 1; i >= 0; i--)
				{
					final Field toMove = fieldList.getModel().getElementAt(selections[i]);
					((DefaultListModel<Field>)fieldList.getModel()).removeElementAt(selections[i]);
					selections[i]++;
					((DefaultListModel<Field>)fieldList.getModel()).insertElementAt(toMove, selections[i]);
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

			fieldList.addListSelectionListener(lse ->
			{
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
			fieldList.getModel().addListDataListener(new ListDataAdapter( () ->
			{
				fieldListSelectAllButton.setEnabled(fieldList.getModel().getSize() > 0);
				fieldListSelectNoneButton.setEnabled(fieldList.getModel().getSize() > 0);
			}));

			fieldListControlPanel.add(new JScrollPane(fieldList), BorderLayout.CENTER);
			fieldListControlPanel.add(fieldListButtonPanel, BorderLayout.SOUTH);

			fieldListPanel.add(new JLabel(JavaPHConstants.SELECT_THE_FIELDS_TO_BE_RETURNED), BorderLayout.NORTH);
			fieldListPanel.add(fieldListControlPanel, BorderLayout.CENTER);
		}

		private void populateFieldList(final @NotNull Server server)
		{
			final DefaultListModel<Field> model = (DefaultListModel<Field>)fieldList.getModel();
			model.clear();

			if (server.getFieldState() == FieldState.FIELD_LOAD_TRUE)
			{
				final @NotNull List<Field> fields = server.getFields();

				for (final Field field : fields)
				{
					model.addElement(field);
				}
			}

			if (model.getSize() > 0)
			{
				fieldList.setSelectionInterval(0, model.getSize() - 1);
			}

			if (server.getFieldState() == FieldState.FIELD_LOAD_TRUE)
			{
				fieldLoadButton.setToolTipText(JavaPHConstants.RELOAD_FIELDS_FOR_THE_SELECTED_SERVER);
			}
			else
			{
				fieldLoadButton.setToolTipText(JavaPHConstants.LOAD_FIELDS_FOR_THE_SELECTED_SERVER);
			}
		}

		private void loadFieldsForServer(final @NotNull Server server)
		{
			final @NotNull String status1 = String.format(JavaPHConstants.LOADING_FIELDS_FOR_S, server.getExpandedName());
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

	private final class ResultPanel extends JTabbedPane
	{
		private final @NotNull JButton colListSelectAllButton = new JButton(JavaPHConstants.SELECT_ALL);
		private final @NotNull JButton colListSelectNoneButton = new JButton(JavaPHConstants.DESELECT_ALL);
		private final @NotNull JButton resultTableColButton = new JButton(JavaPHConstants.SHOW_HIDE_COLUMNS);
		private final @NotNull JButton resultTableColWidthButton = new JButton(JavaPHConstants.RESET_COLUMN_WIDTHS);
		private final @NotNull JCheckBox logWrapCheckBox = new JCheckBox(JavaPHConstants.LINE_WRAP);
		private final @NotNull JCheckBox resultWrapCheckBox = new JCheckBox(JavaPHConstants.LINE_WRAP);

		@SuppressWarnings("WeakerAccess")
		ResultPanel()
		{
			super(SwingConstants.TOP);

			add(getResultTextPanel(), Tab.ResultText.getLabel());
			add(getResultTablePanel(), Tab.ResultTable.getLabel());

			initColumnListPanel();
			initLogTextPanel();

			if (propertyEquals(PROP_DISPLAY_LOG,  true, true))
			{
				add(logTextPanel, Tab.SystemLog.getLabel());
			}
		}

		private @NotNull JPanel getResultTablePanel()
		{
			final @NotNull JPanel resultTablePanel = new JPanel(new BorderLayout());

			resultTable.setColumnSelectionAllowed(false);
			resultTable.setRowSelectionAllowed(false);
			resultTable.setCellSelectionEnabled(true);
			resultTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			resultTable.getModel().addTableModelListener(tme ->
			{
				resultTableColButton.setEnabled(resultTable.getRowCount() > 0);
				resultTableColWidthButton.setEnabled(resultTable.getRowCount() > 0);
				populateColumnList();
			});

			final @NotNull JTableHeader resultTableHeader = resultTable.getTableHeader();
			resultTableHeader.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(final @NotNull MouseEvent e)
				{
					final TableColumnModel tcm = resultTable.getColumnModel();
					final int vc = tcm.getColumnIndexAtX(e.getX());
					final int mc = resultTable.convertColumnIndexToModel(vc);

					resultTable.getTableSorter().sort(mc);
				}
			});

			final @NotNull JPanel resultControlPanel = new JPanel();
			resultTableColButton.setEnabled(false);
			resultTableColButton.addActionListener(ae ->
			{
				final @NotNull int[] prevSelections = colList.getSelectedIndices();
				final int option = JOptionPane.showConfirmDialog(getDefaultPane(), colListPanel, JavaPHConstants.COLUMN_LIST_FOR_RESULT_TABLE, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

				if (option != JOptionPane.OK_OPTION)
				{
					colList.setSelectedIndices(prevSelections);
					return;
				}
				for (int i = 0; i < colList.getModel().getSize(); i++)
				{
					if (colList.isSelectedIndex(i))
					{
						resultTable.getColumn(colList.getModel().getElementAt(i)).setPreferredWidth(resultTable.getColumn(colList.getModel().getElementAt(i)).getMaxWidth());
					}
					else
					{
						resultTable.getColumn(colList.getModel().getElementAt(i)).setPreferredWidth(0);
					}
				}
			});

			resultTableColWidthButton.setEnabled(false);
			resultTableColWidthButton.addActionListener(ae ->
			{
				for (int i = 0; i < colList.getModel().getSize(); i++)
				{
					// If it is a shown column and not hidden (selected in list)
					// then reset its preferred width to its maximum width
					if (colList.isSelectedIndex(i))
					{
						resultTable.getColumn(colList.getModel().getElementAt(i)).setPreferredWidth(resultTable.getColumn(colList.getModel().getElementAt(i)).getMaxWidth());
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

			final @NotNull JButton selectAllButton = new JButton(JavaPHConstants.SELECT_ALL);
			selectAllButton.setMnemonic(KeyEvent.VK_A);
			selectAllButton.addActionListener(ae ->
			{
				resultText.selectAll();
				resultText.getCaret().setSelectionVisible(true);
			});

			resultWrapCheckBox.addItemListener(ie ->
			{
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

			colList.getModel().addListDataListener(new ListDataAdapter(() ->
			{
				colListSelectAllButton.setEnabled(colList.getModel().getSize() > 0);
				colListSelectNoneButton.setEnabled(colList.getModel().getSize() > 0);
			}));

			colListControlPanel.add(new JScrollPane(colList), BorderLayout.CENTER);
			colListControlPanel.add(colListButtonPanel, BorderLayout.SOUTH);

			colListPanel.add(new JLabel(JavaPHConstants.SELECT_THE_COLUMNS_TO_BE_DISPLAYED), BorderLayout.NORTH);
			colListPanel.add(colListControlPanel, BorderLayout.CENTER);
		}

		private void initLogTextPanel()
		{
			logText.setEditable(false);
			logText.setFont(fixedWidthFont);
			final @NotNull JScrollPane logTextScroll = new JScrollPane(logText);

			final @NotNull JButton selectAllButton = new JButton(JavaPHConstants.SELECT_ALL);
			selectAllButton.setMnemonic(KeyEvent.VK_A);
			selectAllButton.addActionListener(ae ->
			{
				logText.selectAll();
				logText.getCaret().setSelectionVisible(true);
			});

			logWrapCheckBox.addItemListener(ie ->
			{
				logText.setLineWrap(logWrapCheckBox.isSelected());
				logText.setWrapStyleWord(logWrapCheckBox.isSelected());
			});

			final @NotNull JButton logClearButton = new JButton(JavaPHConstants.CLEAR_LOG);
			logClearButton.setMnemonic(KeyEvent.VK_C);
			logClearButton.addActionListener(ae ->
			{
				logText.setText("");
				log(JavaPHConstants.LOG_CLEARED);
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
			final DefaultListModel<String> listModel = (DefaultListModel<String>)colList.getModel();
			listModel.clear();

			for (int i = 0; i < tableModel.getColumnCount(); i++)
			{
				listModel.addElement(tableModel.getColumnName(i));
			}

			if (listModel.getSize() > 0)
			{
				colList.setSelectionInterval(0, listModel.getSize() - 1);
			}
		}

	}

	private final class StatusPanel extends JPanel
	{
		@SuppressWarnings("WeakerAccess")
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

			statusLabel.setHorizontalAlignment(JLabel.LEFT);
			leftPanel.add(statusLabel, BorderLayout.WEST);

			serverStatusLabel.setBorder(BorderFactory.createEtchedBorder());
			serverStatusLabel.setHorizontalAlignment(JLabel.CENTER);
			rightPanel.add(serverStatusLabel, BorderLayout.CENTER);

			portStatusLabel.setBorder(BorderFactory.createEtchedBorder());
			portStatusLabel.setHorizontalAlignment(JLabel.CENTER);
			rightPanel.add(portStatusLabel, BorderLayout.EAST);

			showDefaultStatus();
		}
	}

	public JavaPH(final @NotNull List<Command> commands,  final @NotNull IconProvider iconProvider, final @NotNull ServerManager serverManager, final @NotNull PropertyCollection defaultProperties, final @NotNull PropertyCollection properties) throws IOException, QiServerFileException
	{
		this.iconProvider = iconProvider;
		this.serverManager = serverManager;
		this.defaultProperties = defaultProperties;
		this.properties = properties;
		final @NotNull Command[] cmds = new Command[commands.size()];
		this.commands = commands.toArray(cmds);
		commandComboBox = new JComboBox<>(this.commands);
		int frameHeight = this.properties.getProperty(PROP_APP_HEIGHT, APP_DEFAULT_HEIGHT);
		int frameWidth = this.properties.getProperty(PROP_APP_WIDTH, APP_DEFAULT_WIDTH);
		int frameX = this.properties.getProperty(PROP_APP_X_POSITION, -1);
		int frameY = this.properties.getProperty(PROP_APP_Y_POSITION, -1);

		if (frameHeight < APP_MIN_HEIGHT)
		{
			frameHeight = APP_DEFAULT_HEIGHT;
		}
		if (frameWidth < APP_MIN_WIDTH)
		{
			frameWidth = APP_DEFAULT_WIDTH;
		}

		defaultPane = frame.getRootPane();

		Browser.init();

		queryComboBoxEditor = new TextFieldComboBoxEditor(ae -> queryButton.doClick());
		initProperties();
		this.serverManager.loadAllServers();
		servers  = this.serverManager.getServers();
		serverComboBox = new JComboBox<>(servers);
		aboutDialog = new AboutDialog(this);
		findDialog = new FindDialog(this);
		propertiesDialog = new PropertiesDialog(this);
		queryToolBar = new QueryToolBar(this);
		splashWindow = new SplashWindow(this);
		resultPanel = new ResultPanel();
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new ControlTabDispatcher(resultPanel));

		if (propertyEquals(PROP_DISPLAY_SPLASH, true, true))
		{
			showSplashWindow();
		}

		if (getLoadFields() == LoadFields.Startup) {
			loadFieldsForAllServers();
		}

		final Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		// Need to add status panel first so later panels
		// can have access to the status labels
		contentPane.add(new StatusPanel(), BorderLayout.SOUTH);
		contentPane.add(queryToolBar, BorderLayout.NORTH);
		queryPanel = new QueryPanel(this);
		contentPane.add(new ContentPanel(queryPanel, resultPanel), BorderLayout.CENTER);

		showToolBar(propertyEquals(PROP_DISPLAY_TOOLBAR, true, true));

		defaultPane.setJMenuBar(new MainMenu(this));

		log(JavaPHConstants.INFO_NAME_INITIALIZED);
		log(JavaPHConstants.INITIALIZING_DEFAULT_SERVER);

		serverComboBox.setSelectedItem(serverManager.getDefaultServer());

		log(JavaPHConstants.DEFAULT_SERVER_INITIALIZED);

		showDefaultStatus();
		frame.setContentPane(getContentPane());
		frame.setIconImage(getImageIcon(FilePaths.PH_ICON_SMALLER_GIF).getImage());

		frame.setSize(frameWidth, frameHeight);
		frame.setTitle(JavaPHConstants.INFO_NAME);

		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		// if no previous X/Y position specified or if the previous X/Y
		// position is somehow off the screen then just center the frame
		frameX = (frameX < 0 || frameX > screenSize.width) ? (screenSize.width / 2 - (frameWidth / 2)) : frameX;
		frameY = (frameY < 0 || frameY > screenSize.height) ? (screenSize.height / 2 - (frameHeight / 2)) : frameY;

		frame.setLocation(frameX, frameY);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.addWindowListener(this);
	}

	@SuppressWarnings("BooleanParameter")
	public void findText(final @NotNull String text, final boolean caseSensitive, final boolean wrap)
	{
		final @NotNull Tab tab = getResultPanelSelectedTab();
		switch (tab)
		{
			case ResultText:
				findTextInTextArea(resultText, text, caseSensitive, wrap);
				break;
			case SystemLog:
				findTextInTextArea(logText, text, caseSensitive, wrap);
				break;
			case ResultTable:
				findTextInTable(resultTable, text, caseSensitive, wrap);
				break;
		}
	}

	private void findTextInTable(final @NotNull JTable table, final @NotNull String text, final boolean caseSensitive, final boolean wrap)
	{

		// if there aren't any rows or columns then we aren't going to find anything
		if (table.getColumnCount() <= 0 || table.getRowCount() <= 0)
		{
			log(JavaPHConstants.INFO_CANNOT_PERFORM_A_SEARCH_ON_AN_EMPTY_TABLE);
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
				log(JavaPHConstants.INFO_CANNOT_SEARCH_PAST_THE_LAST_CELL_OF_A_TABLE);

				if (wrap)
				{
					inLastCell = true;
				}
				else
				{
					return;
				}
			}
		}

		if (!inLastCell)
		{
			for (int row = startRow; row < table.getRowCount(); row++)
			{
				// if at the starting row then continue from the starting column
				// otherwise start from the first column
				for (int col = (row == startRow) ? startCol : 0; col < table.getColumnCount(); col++)
				{
					final @Nullable Object anObject = table.getValueAt(row, col);

					if (anObject == null)
					{
						continue;
					}
					else if (caseSensitive && anObject.toString().contains(text))
					{
						table.changeSelection(row, col, false, false);
						return;
					}
					else if (!caseSensitive && anObject.toString().toUpperCase().contains(text.toUpperCase()))
					{
						table.changeSelection(row, col, false, false);
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

			for (int row = startRow; row < table.getRowCount(); row++)
			{
				// if at the starting row then continue from the starting column
				// otherwise start from the first column
				for (int col = (row == startRow) ? startCol : 0; col < table.getColumnCount(); col++)
				{
					final @Nullable Object anObject = table.getValueAt(row, col);

					if (anObject == null)
					{
						continue;
					}
					else if (caseSensitive && anObject.toString().contains(text))
					{
						table.changeSelection(row, col, false, false);
						return;
					}
					else if (!caseSensitive && anObject.toString().toUpperCase().contains(text.toUpperCase()))
					{
						table.changeSelection(row, col, false, false);
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
		if (textArea.getSelectedText() == null)
		{
			startIndex = 0;
		}
		else if (textArea.getSelectionStart() == 0 && textArea.getSelectionEnd() == textArea.getText().length() - 1)
		{
			startIndex = 0;
		}
		else
		{
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

	private void resetCommandComboBoxIndex()
	{
		commandComboBox.setSelectedIndex(QUERY_COMMAND);
	}

	public @NotNull JRootPane getDefaultPane()
	{
		return defaultPane;
	}

	@Contract(pure = true)
	public @NotNull String getFieldSeparator()
	{
		return fieldSeparator;
	}

	public @NotNull LoadFields getIntPropertyDefault(final @NotNull String key, final @NotNull LoadFields defaultValue)
	{
		return getLoadFieldsPropertyDefault(defaultProperties, key, defaultValue);
	}

	private @NotNull LoadFields getLoadFieldsPropertyDefault(final @NotNull PropertyCollection collection, final @NotNull String key, final @NotNull LoadFields defaultValue)
	{
		final int value = collection.getProperty(key, defaultValue.getValue());
		final @NotNull LoadFields fields = LoadFields.fromOrDefault(value);
		return fields;
	}

	@Contract(pure = true)
	public @NotNull String getLastCustomSeparator()
	{
		return customFieldSeparator;
	}

	@Contract(pure = true)
	public @NotNull LoadFields getLoadFields()
	{
		return loadFields;
	}

	public @NotNull JTextArea getLogText()
	{
		return logText;
	}

	public @NotNull ServerManager getServerManager()
	{
		return serverManager;
	}

	public @NotNull Optional<String> getPropertyDefault(final @NotNull String key)
	{
		return defaultProperties.getProperty(key);
	}

	@Contract(pure = true)
	public int getQueryRuntime()
	{
		return queryRuntime;
	}

	public int getDefaultQueryRuntimeValue()
	{
		final int minValue = QueryRuntime.MIN.getValue();
		final @NotNull Optional<String> result = defaultProperties.getProperty(PROP_QUERY_RUNTIME);
		return result.map(Integer::valueOf).orElse(minValue);
	}

	public @NotNull QueryComboBox getQueryComboBox()
	{
		return queryComboBox;
	}

	@Contract(pure = true)
	public boolean isQueryCanceled()
	{
		return queryPanel.isQueryCanceled();
	}

	public void setQueryProgress(final int progress)
	{
		queryPanel.setQueryProgress(progress);
	}

	public void beginQuery(final @NotNull String command)
	{
		queryButton.setEnabled(false);
		showStatus(JavaPHConstants.QUERY_RUNNING_PLEASE_WAIT);
		log(String.format(JavaPHConstants.RUNNING_QUERY_S, command));
	}

	public void endFailedQuery(final @NotNull String message, final @NotNull String title)
	{
		queryPanel.closeQueryProgressMonitor();
		showStatus(message);
		log(message);
		showErrorDialog(message, title);
		queryButton.setEnabled(true);
	}

	public void endQuery(final @NotNull String rawResult, final @NotNull Collection<Object> headers, final @NotNull Object[][] values)
	{
		showStatus(JavaPHConstants.QUERY_FINISHED);
		log(JavaPHConstants.QUERY_FINISHED);
		queryPanel.closeQueryProgressMonitor();
		resultText.setText(rawResult);

		final @NotNull ResultTableModel resultModel = resultTable.getTableSorter().getModel();
		resultModel.setDataVector(values, headers.toArray());
		resultTable.resetColumnWidths();
		queryButton.setEnabled(true);
	}

	public @NotNull JToolBar getQueryToolBar()
	{
		return queryToolBar;
	}

	private @NotNull JTabbedPane getResultPanel()
	{
		return resultPanel;
	}

	public @NotNull Tab getResultPanelSelectedTab()
	{
		final int index = getResultPanel().getSelectedIndex();
		final @NotNull Tab tab = Tab.fromIndex(index);
		return tab;
	}

	private @NotNull ResultTable getResultTable()
	{
		return resultTable;
	}

	public @NotNull TableModel getResultTableModel()
	{
		return getResultTable().getModel();
	}

	public @NotNull JTextArea getResultText()
	{
		return resultText;
	}

	public void clearPreviousQuery()
	{
		// Clear the results
		getResultText().setText("");
		getResultTable().getTableSorter().getModel().resetModel();

		// Clear the previous query
		resetCommandComboBoxIndex();
		getQueryComboBox().setSelectedItem("");
	}

	@Override
	public @NotNull String getURL(final @NotNull String location)
	{
		return iconProvider.getURL(location);
	}

	@Override
	public @NotNull ImageIcon getImageIcon(final @NotNull String location)
	{
		return iconProvider.getImageIcon(location);
	}

	@Override
	public void init()
	{
	}

	private void initProperties() throws IOException
	{
		// First load default properties - prints exception and exits if not found
		loadDefaultProperties();

		// Next load custom properties, overrides defaults - ignores if not found
		try
		{
			loadProperties();
		}
		catch (final IOException ignored)
		{
		}

		setLoadFields(getLoadFieldsPropertyDefault(properties, PROP_LOAD_FIELDS, LoadFields.getDefault()));
		setQueryRuntime(properties.getProperty(PROP_QUERY_RUNTIME, QueryRuntime.DEFAULT.getValue()));
		setSavePosition(propertyEquals(PROP_SAVE_POSITION,  true, true));
	}

	@Contract(pure = true)
	public boolean isFieldQuoted()
	{
		return fieldQuoted;
	}

	@Contract(pure = true)
	public boolean isSavePosition()
	{
		return savePosition;
	}

	private void loadDefaultProperties() throws IOException
	{
		// TODO can I reuse the same stream?
		try (@NotNull FileInputStream inStream = new FileInputStream(PROP_FILE_DEF))
		{
			defaultProperties.load(inStream);
		}
		try (@NotNull FileInputStream inStream1 = new FileInputStream(PROP_FILE_DEF))
		{
			properties.load(inStream1);
		}
	}

	private void loadFieldsForAllServers()
	{
		if (!servers.isEmpty())
		{

			final @NotNull ProgressMonitor fieldProgressMonitor = new ProgressMonitor(defaultPane, JavaPHConstants.LOADING_FIELDS_FOR_SERVER, "", 0, servers.size());
			
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

	private void loadProperties() throws IOException
	{
		try (@NotNull FileInputStream inStream = new FileInputStream(PROP_FILE))
		{
			properties.load(inStream);
		}
	}

	@Override
	public void log(final @NotNull String text)
	{
		final @NotNull Date date = new Date(System.currentTimeMillis());
		logText.append(date.toString());
		logText.append(" :: ");
		logText.append(text);
		logText.append("\n");
	}

	public boolean propertyDefaultEquals(final @NotNull String key, final @NotNull String defaultValue, final @NotNull String equalsValue)
	{
		final @NotNull String value = defaultProperties.getProperty(key, defaultValue);
		return value.equals(equalsValue);
	}

	@SuppressWarnings("BooleanParameter")
	public boolean propertyEquals(final @NotNull String key, final boolean defaultValue, final boolean equalsValue)
	{
		final @NotNull String value = properties.getProperty(key, String.valueOf(defaultValue));
		return value.equals(String.valueOf(equalsValue));
	}

	public void setFieldQuoted(final boolean quoted)
	{
		fieldQuoted = quoted;
	}

	public void setFieldSeparator(final @NotNull String separator)
	{
		if (separator.isEmpty()) {
			throw new IllegalArgumentException(JavaPHConstants.ERROR_NULL_OR_EMPTY_STRING_PASSED_TO_SET_FIELD_SEPARATOR);
		}
			
		// log("Changing field separator string from " + fieldSeparator + " to " + separator);
		fieldSeparator = separator;
		
		// if not a standard separator (comma or tab) then set the custom separator as well
		if (!separator.equals(COMMA_SEPARATOR) && !separator.equals(TAB_SEPARATOR)) {
			customFieldSeparator = separator;
		}
	}

	public void setLoadFields(final @NotNull LoadFields value)
	{
		loadFields = value;
	}

	public void setProperty(final @NotNull String key, final @NotNull String value)
	{
		properties.setProperty(key, value);
	}

	public void setProperty(final @NotNull String key, final int value)
	{
		properties.setProperty(key, value);
	}

	@SuppressWarnings("BooleanParameter")
	public void setProperty(final @NotNull String key, final boolean value)
	{
		properties.setProperty(key, value);
	}

	public void setQueryRuntime(final int runtime)
	{
		if (QueryRuntime.overMaximum(runtime))
		{
			log(String.format(JavaPHConstants.QUERY_RUNTIME_D_TOO_HIGH_USING_MAXIMUM_VALUE_D, runtime, QueryRuntime.MAX.getValue()));
			queryRuntime = QueryRuntime.MAX.getValue();
		}
		else if (QueryRuntime.underMinimum(runtime))
		{
			log(String.format(JavaPHConstants.QUERY_RUNTIME_D_TOO_LOW_USING_MINIMUM_VALUE_D, runtime, QueryRuntime.MIN.getValue()));
			queryRuntime = QueryRuntime.MIN.getValue();
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

	public void showExceptionDialog(final @NotNull String message)
	{
		showErrorDialog(message, JavaPHConstants.EXCEPTION);
	}

	private void showErrorDialog(final @NotNull String message, final @NotNull String title)
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
		final @NotNull Frame defaultFrame = JOptionPane.getFrameForComponent(defaultPane);
		Browser.dialogConfiguration(defaultFrame);
	}

	public void showDefaultStatus()
	{
		showStatus(APP_DEFAULT_STATUS);
	}

	public void showFindDialog()
	{
		findDialog.setLocationRelativeTo(defaultPane);
		final @NotNull Tab tab = getResultPanelSelectedTab();
		findDialog.setTitle(String.format(JavaPHConstants.FIND_TEXT_IN_S, tab.getLabel()));
		findDialog.setVisible(true);
	}

	@SuppressWarnings("BooleanParameter")
	public void showLog(final boolean show)
	{
		final int location = resultPanel.indexOfTab(Tab.SystemLog.getLabel());

		// If requested to show the log and it's not already
		// there then add the tab and select it
		if (show && location < 0)
		{
			resultPanel.add(Tab.SystemLog.getLabel(), logTextPanel);
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
		propertiesDialog.setVisible(true);
	}

	private void showSplashWindow()
	{
		splashWindow.setVisible(true);
			
		try
		{
			Thread.sleep(SPLASH_DISPLAY);
		}
		catch (final @NotNull InterruptedException ignored)
		{
		}
			
		splashWindow.dispose();
	}

	@Override
	public void showStatus(final @NotNull String msg)
	{
		statusLabel.setText(String.format(" %s", msg));
	}

	@SuppressWarnings("BooleanParameter")
	public void showToolBar(final boolean show)
	{
		queryToolBar.setVisible(show);
	}

	public void storeProperties()
	{
		try(FileOutputStream stream = new FileOutputStream(PROP_FILE))
		{
			properties.store(stream, PROP_HEADER);
		}
		catch (final @NotNull FileNotFoundException e)
		{
			errLogger.printStackTrace(e);
			errLogger.log(String.format(JavaPHConstants.FILE_NOT_FOUND_OCCURRED_WHEN_TRYING_TO_STORE_PROPERTIES_TO_S, PROP_FILE));
		}
		catch (final @NotNull IOException e)
		{
			errLogger.printStackTrace(e);
			errLogger.log(String.format(JavaPHConstants.IOEXCEPTION_OCCURRED_WHEN_TRYING_TO_STORE_PROPERTIES_TO_S, PROP_FILE));
		}
	}
}
