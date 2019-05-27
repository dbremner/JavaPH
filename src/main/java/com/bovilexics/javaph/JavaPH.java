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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

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
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import com.bovilexics.javaph.models.QueryComboBoxModel;
import com.bovilexics.javaph.models.TableSorter;
import com.bovilexics.javaph.qi.QiCommand;
import com.bovilexics.javaph.qi.QiConnection;
import com.bovilexics.javaph.qi.QiField;
import com.bovilexics.javaph.qi.QiServer;
import com.bovilexics.javaph.threads.QueryThread;
import com.bovilexics.javaph.ui.AboutDialog;
import com.bovilexics.javaph.ui.CustomButtonGroup;
import com.bovilexics.javaph.ui.FindDialog;
import com.bovilexics.javaph.ui.MainMenu;
import com.bovilexics.javaph.ui.PropertiesDialog;
import com.bovilexics.javaph.ui.QueryComboBox;
import com.bovilexics.javaph.ui.QueryToolBar;
import com.bovilexics.javaph.ui.ResultTable;
import com.bovilexics.javaph.ui.ServerRenderer;
import com.bovilexics.javaph.ui.SplashWindow;
import com.bovilexics.javaph.ui.TextFieldComboBoxEditor;
import com.bovilexics.javaph.util.Browser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
public class JavaPH extends JApplet implements JavaPHConstants
{
	// Custom widgets and other private stuff

	private boolean fieldQuoted = false;
	private boolean savePosition = false;

	private int loadFields = LOAD_FIELDS_SELECTED;
	private int queryRuntime = QUERY_RUNTIME_DEF;

	private AboutDialog aboutDialog;
	private CustomButtonGroup fieldRadioGroup;
	private FindDialog findDialog;
	@NotNull
	private final Font fixedWidthFont = new Font("Monospaced", Font.PLAIN, 12);
	@Nullable
	private ImageIcon fieldCustomOff;
	@Nullable
	private ImageIcon fieldCustomOn;
	@Nullable
	private ImageIcon fieldLoadOff;
	@Nullable
	private ImageIcon fieldLoadOn;
	@NotNull
	private final Properties defaultProperties = new Properties();
	@NotNull
	private final Properties properties = new Properties();
	private PropertiesDialog propertiesDialog;
	private ProgressMonitor fieldProgressMonitor; 
	private ProgressMonitor queryProgressMonitor;
	private QiCommand[] commands;
	private QiConnection connection;
	private QueryComboBox queryComboBox;
	private QueryToolBar queryToolBar;
	private ResultTable resultTable;
	private SplashWindow splashWindow;
	@Nullable
	private String customFieldSeparator = CUSTOM_SEPARATOR;
	@Nullable
	private String fieldSeparator = COMMA_SEPARATOR;
	private TextFieldComboBoxEditor queryComboBoxEditor;
	private Vector<QiServer> servers;
	
	// Swing widgets and stuff
	
	private JButton colListSelectAllButton;
	private JButton colListSelectNoneButton;
	@Nullable
	private JButton fieldCustomButton;
	private JButton fieldListMoveDnButton;
	private JButton fieldListMoveUpButton;
	private JButton fieldListSelectAllButton;
	private JButton fieldListSelectNoneButton;
	@Nullable
	private JButton fieldLoadButton;
	private JButton logClearButton;
	private JButton queryButton;
	private JButton resultTableColButton;
	private JButton resultTableColWidthButton;
	private JCheckBox logWrapCheckBox;
	private JCheckBox resultWrapCheckBox;
	private JComboBox<QiCommand> commandComboBox;
	private JComboBox<QiServer> serverComboBox;
	private JLabel portStatusLabel;
	private JLabel serverStatusLabel;
	private JLabel statusLabel;
	private JList colList;
	private JList fieldList;
	private JPanel colListPanel;
	private JPanel fieldListPanel;
	private JPanel logTextPanel;
	private JPanel queryPanel;
	private JRadioButton fieldDefaultRadioButton;
	private JRadioButton fieldAllRadioButton;
	private JRadioButton fieldCustomRadioButton;
	private JRootPane defaultPane;
	private JScrollPane logTextScroll;
	private JScrollPane resultTableScroll;
	private JScrollPane resultTextScroll;
	private JTabbedPane resultPanel; 
	private JTextArea logText;
	private JTextArea resultText;

	class ControlTabDispatcher	implements KeyEventDispatcher
	{
		@Override
		public boolean dispatchKeyEvent(@NotNull KeyEvent ke)
		{
			if (ke.isControlDown() && ke.getKeyCode() == KeyEvent.VK_TAB)
			{
				// If we don't have this condition then the tab will switch twice,
				// once for the key pressed event and again for the key released event.
				if (ke.getID() == Event.KEY_RELEASE)
				{
					int selected = getResultPanel().getSelectedIndex();
					int last = getResultPanel().getTabCount() - 1;
			
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

	class ContentPanel extends JPanel
	{	
		ContentPanel(@NotNull final JavaPH javaph)
		{	
			setBorder(BorderFactory.createEtchedBorder());
			setLayout(new BorderLayout());
			
			queryPanel = new QueryPanel(javaph);
			add(queryPanel, BorderLayout.NORTH);
			
			resultPanel = new ResultPanel(javaph);		
			add(resultPanel, BorderLayout.CENTER);
		}
	}

	class QueryPanel extends JPanel
	{
		@NotNull
		private final JavaPH parent;
		
		QueryPanel(@NotNull final JavaPH javaph)
		{
			parent = javaph;

			initFieldListPanel();

			setLayout(new BorderLayout());

			add(getQueryLabelPanel(), BorderLayout.WEST);
			add(getQueryContentPanel(), BorderLayout.CENTER);
			add(getQueryButtonPanel(javaph), BorderLayout.EAST);
		}

		@NotNull
		private JPanel getQueryButtonPanel(@NotNull final JavaPH javaph)
		{
			@NotNull JPanel queryButtonPanel = new JPanel(new FlowLayout());
			
			queryButton = new JButton("Execute Query");
			queryButton.setMnemonic(KeyEvent.VK_Q);
			queryButton.setToolTipText("Click this to start running the query.");
			queryButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent ae)
				{
					if (queryComboBox.isEnabled())
					{
						Object selectedItem = queryComboBox.getEditor().getItem();
						@NotNull QueryComboBoxModel model = (QueryComboBoxModel)queryComboBox.getModel();
		
						if (selectedItem != null && !selectedItem.toString().equals(""))
						{
							if ( model.getIndexOf(selectedItem) < 0) {
								model.addElement(selectedItem);
							}
						
							model.setSelectedItem(selectedItem);
						}
					}
					
					queryProgressMonitor = new ProgressMonitor(defaultPane, "Executing Query", getCommand(), 0, getQueryRuntime());
					@NotNull QueryThread qt = new QueryThread(javaph);
					qt.start();
				}
			});

			queryButtonPanel.add(queryButton);
			
			return queryButtonPanel;
		}

		@NotNull
		private JPanel getQueryCommandPanel()
		{
			@NotNull JPanel queryCommandPanel = new JPanel(new BorderLayout());

			commandComboBox = new JComboBox<>(commands);
			commandComboBox.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent ae)
				{	
					queryComboBox.setSelectedIndex(-1);
					queryComboBox.setEnabled(commands[commandComboBox.getSelectedIndex()].isTextEditable());
					fieldRadioGroup.setEnabled(commandComboBox.getSelectedIndex() == QUERY_COMMAND);
					fieldCustomButton.setEnabled(commandComboBox.getSelectedIndex() == QUERY_COMMAND);
					fieldLoadButton.setEnabled(commandComboBox.getSelectedIndex() == QUERY_COMMAND);
				}
			});

			queryComboBoxEditor = new TextFieldComboBoxEditor(ae -> queryButton.doClick());

			queryComboBoxEditor.getEditorComponent().addKeyListener(new KeyListener()
			{
				@Override
				public void keyPressed(KeyEvent ke)
				{
				}
				
				@Override
				public void keyReleased(@NotNull KeyEvent ke)
				{
					if (ke.getKeyCode() == KeyEvent.VK_ENTER || ke.getKeyCode() == KeyEvent.VK_ESCAPE)
					{
						queryComboBox.hidePopup();
					}
					else if (!ke.isActionKey())
					{
						String filter = queryComboBoxEditor.getItem().toString();
						
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
				
				@Override
				public void keyTyped(KeyEvent ke)
				{
				}
			});

			queryComboBox = new QueryComboBox();
			queryComboBox.setEditable(true);
			queryComboBox.setEditor(queryComboBoxEditor);

			queryCommandPanel.add(commandComboBox, BorderLayout.WEST);
			queryCommandPanel.add(queryComboBox, BorderLayout.CENTER);

			return queryCommandPanel;
		}

		@NotNull
		private JPanel getQueryContentPanel()
		{
			@NotNull JPanel queryContentPanel = new JPanel(new BorderLayout());

			queryContentPanel.add(getQueryServerPanel(), BorderLayout.NORTH);
			queryContentPanel.add(getQueryCommandPanel(), BorderLayout.CENTER);
			queryContentPanel.add(getQueryFieldPanel(), BorderLayout.SOUTH);
			
			return queryContentPanel;
		}

		@NotNull
		private JPanel getQueryFieldPanel()
		{
			@NotNull JPanel queryFieldPanel = new JPanel();
			queryFieldPanel.setLayout(new BoxLayout(queryFieldPanel, BoxLayout.X_AXIS));

			fieldRadioGroup = new CustomButtonGroup();
			
			fieldDefaultRadioButton = new JRadioButton("Default Fields");
			fieldAllRadioButton = new JRadioButton("All Fields");
			fieldCustomRadioButton = new JRadioButton("Custom Fields");
			
			fieldRadioGroup.add(fieldDefaultRadioButton);
			fieldRadioGroup.add(fieldAllRadioButton);
			fieldRadioGroup.add(fieldCustomRadioButton);
			
			fieldDefaultRadioButton.setSelected(true);
				
			fieldCustomButton = new JButton(fieldCustomOff);
			fieldCustomButton.setBorder(BorderFactory.createEtchedBorder());
			fieldCustomButton.setFocusable(false);
			fieldCustomButton.setToolTipText("Choose the fields that queries will return");
			fieldCustomButton.addMouseListener(new MouseListener()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
				}

				@Override
				public void mouseEntered(MouseEvent e)
				{
					fieldCustomButton.setIcon(fieldCustomOn);
				}

				@Override
				public void mouseExited(MouseEvent e)
				{
					fieldCustomButton.setIcon(fieldCustomOff);
				}

				@Override
				public void mousePressed(MouseEvent e)
				{
				}

				@Override
				public void mouseReleased(MouseEvent e)
				{
				}
			});
			fieldCustomButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent ae)
				{	
					int[] prevSelections = fieldList.getSelectedIndices();
					int option = JOptionPane.showConfirmDialog(getDefaultPane(), fieldListPanel, "Field List for " + serverComboBox.getSelectedItem().toString(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
					
					if (option != JOptionPane.OK_OPTION)
					{
						fieldList.setSelectedIndices(prevSelections);
					}
					else
					{
						fieldCustomRadioButton.setSelected(true);
					}
				}
			});

			fieldLoadButton = new JButton(fieldLoadOff);
			fieldLoadButton.setBorder(BorderFactory.createEtchedBorder());
			fieldLoadButton.setFocusable(false);
			fieldLoadButton.addMouseListener(new MouseListener()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
				}

				@Override
				public void mouseEntered(MouseEvent e)
				{
					fieldLoadButton.setIcon(fieldLoadOn);
				}

				@Override
				public void mouseExited(MouseEvent e)
				{
					fieldLoadButton.setIcon(fieldLoadOff);
				}

				@Override
				public void mousePressed(MouseEvent e)
				{
				}

				@Override
				public void mouseReleased(MouseEvent e)
				{
				}
			});
			fieldLoadButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent ae)
				{
					@Nullable QiServer server = (QiServer)serverComboBox.getSelectedItem();
				
					if (server != null)
					{
						loadFieldsForServer(server);
						serverComboBox.repaint();
						populateFieldList(server);
					}
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

		@NotNull
		private JPanel getQueryLabelPanel()
		{
			@NotNull JPanel queryLabelPanel = new JPanel(new BorderLayout());

			queryLabelPanel.add(new JLabel(SERVER_LABEL_PREFIX), BorderLayout.NORTH);
			queryLabelPanel.add(new JLabel(QUERY_LABEL_PREFIX), BorderLayout.CENTER);
			queryLabelPanel.add(new JLabel(" "), BorderLayout.SOUTH);
			
			return queryLabelPanel;
		}

		@NotNull
		private JPanel getQueryServerPanel()
		{
			@NotNull JPanel queryServerPanel = new JPanel(new BorderLayout());

			serverComboBox = new JComboBox<>(servers);
			serverComboBox.setRenderer(new ServerRenderer(parent));
			serverComboBox.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent ae)
				{
					@NotNull QiServer server = (QiServer)serverComboBox.getSelectedItem();
					String serverText = server.getServer();
					
					@Nullable Integer portInt = server.getPort();
					@NotNull String portText = portInt.toString();
					
					serverStatusLabel.setText(SERVER_LABEL_PREFIX + serverText + SERVER_LABEL_SUFFIX);
					portStatusLabel.setText(PORT_LABEL_PREFIX + portText + PORT_LABEL_SUFFIX);
					
					connection = new QiConnection(serverText, portInt.intValue());

					if (getLoadFields() == LOAD_FIELDS_SELECTED && !(server.getFieldState() == QiServer.FIELD_LOAD_ERROR) && !(server.getFieldState() == QiServer.FIELD_LOAD_TRUE))
					{
						serverComboBox.hidePopup();
						loadFieldsForServer(server);
					}
					
					populateFieldList(server);
				}
			});
			QiServer.setDefaultServer(getProperty(PROP_DEFAULT_SERVER));

			queryServerPanel.add(serverComboBox, BorderLayout.CENTER);

			return queryServerPanel;
		}
		
		private void initFieldListPanel()
		{
			// This panel is not visible in the main window
			// It will be in a separate dialog that is displayed
			// when the custom field button is activated
			fieldListPanel = new JPanel(new BorderLayout());
			
			@NotNull JPanel fieldListControlPanel = new JPanel(new BorderLayout());
			fieldListControlPanel.setBorder(BorderFactory.createEtchedBorder());
			
			@NotNull JPanel fieldListButtonPanel = new JPanel(new FlowLayout());

			fieldListSelectAllButton = new JButton("Select All");
			fieldListSelectAllButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent ae)
				{
					fieldList.setSelectionInterval(0, fieldList.getModel().getSize() - 1);
				}
			});
			
			fieldListSelectNoneButton = new JButton("Delselect All");
			fieldListSelectNoneButton.addActionListener(ae -> fieldList.clearSelection());

		
			fieldListMoveUpButton = new JButton("Move Up");
			fieldListMoveUpButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent ae)
				{
					int[] selections = fieldList.getSelectedIndices();
					
					for (int i = 0; i < selections.length; i++)
					{
						Object toMove = fieldList.getModel().getElementAt(selections[i]);
						((DefaultListModel)fieldList.getModel()).removeElementAt(selections[i]);
						selections[i]--;
						((DefaultListModel)fieldList.getModel()).insertElementAt(toMove, selections[i]);
					}
					
					fieldList.setSelectedIndices(selections);
				}
			});
			
			fieldListMoveDnButton = new JButton("Move Down");
			fieldListMoveDnButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent ae)
				{
					int[] selections = fieldList.getSelectedIndices();
				
					for (int i = selections.length - 1; i >= 0; i--)
					{
						Object toMove = fieldList.getModel().getElementAt(selections[i]);
						((DefaultListModel)fieldList.getModel()).removeElementAt(selections[i]);
						selections[i]++;
						((DefaultListModel)fieldList.getModel()).insertElementAt(toMove, selections[i]);
					}
				
					fieldList.setSelectedIndices(selections);
				}
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

			fieldList = new JList(new DefaultListModel());
			fieldList.addListSelectionListener(new ListSelectionListener()
			{
				@Override
				public void valueChanged(ListSelectionEvent lse)
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
				}
			});
			fieldList.getModel().addListDataListener(new ListDataListener()
			{
				@Override
				public void contentsChanged(ListDataEvent lde)
				{
					updateSelectButtons();
				}

				@Override
				public void intervalAdded(ListDataEvent lde)
				{
					updateSelectButtons();
				}

				@Override
				public void intervalRemoved(ListDataEvent lde)
				{
					updateSelectButtons();
				}
				
				private void updateSelectButtons()
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
	}

	class ResultPanel extends JTabbedPane
	{
		ResultPanel(final JavaPH javaph)
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

		@NotNull
		private JPanel getResultTablePanel()
		{
			@NotNull JPanel resultTablePanel = new JPanel(new BorderLayout());

			resultTable = new ResultTable();
			resultTable.setColumnSelectionAllowed(false);
			resultTable.setRowSelectionAllowed(false);
			resultTable.setCellSelectionEnabled(true);
			resultTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			resultTable.getModel().addTableModelListener(new TableModelListener()
			{
				@Override
				public void tableChanged(TableModelEvent tme)
				{
					resultTableColButton.setEnabled(resultTable.getRowCount() > 0);
					resultTableColWidthButton.setEnabled(resultTable.getRowCount() > 0);
					populateColumnList();
				}
			});
				
			@NotNull JTableHeader resultTableHeader = (JTableHeader)resultTable.getTableHeader();
			resultTableHeader.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(@NotNull MouseEvent me)
				{
					TableColumnModel tcm = resultTable.getColumnModel();
					int vc = tcm.getColumnIndexAtX(me.getX());
					int mc = resultTable.convertColumnIndexToModel(vc);
						
					((TableSorter)resultTable.getModel()).sort(mc);
				}
			});

			@NotNull JPanel resultControlPanel = new JPanel();
			resultTableColButton = new JButton("Show/Hide Columns");
			resultTableColButton.setEnabled(false);
			resultTableColButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent ae)
				{	
					int[] prevSelections = colList.getSelectedIndices();
					int option = JOptionPane.showConfirmDialog(getDefaultPane(), colListPanel, "Column List for Result Table", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
					
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
				}
			});

			resultTableColWidthButton = new JButton("Reset Column Widths");
			resultTableColWidthButton.setEnabled(false);
			resultTableColWidthButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent ae)
				{	
					for (int i = 0; i < colList.getModel().getSize(); i++)
					{
						// If it is a shown column and not hidden (selected in list)
						// then reset its preferred width to its maximum width
						if (colList.isSelectedIndex(i))
						{
							resultTable.getColumn(colList.getModel().getElementAt(i).toString()).setPreferredWidth(resultTable.getColumn(colList.getModel().getElementAt(i).toString()).getMaxWidth());
						}
					}
				}
			});

			resultTableScroll = new JScrollPane(resultTable);
			resultControlPanel.add(resultTableColButton);
			resultControlPanel.add(resultTableColWidthButton);

			resultTablePanel.add(resultTableScroll, BorderLayout.CENTER);
			resultTablePanel.add(resultControlPanel, BorderLayout.SOUTH);
	
			return resultTablePanel;
		}
		
		@NotNull
		private JPanel getResultTextPanel()
		{
			@NotNull JPanel resultTextPanel = new JPanel(new BorderLayout());

			resultText = new JTextArea();
			resultText.setEditable(false);
			resultText.setFont(fixedWidthFont);
			resultTextScroll = new JScrollPane(resultText);

			@NotNull JButton selectAllButton = new JButton("Select All");
			selectAllButton.setMnemonic(KeyEvent.VK_A);
			selectAllButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent ae)
				{
					resultText.selectAll();
					resultText.getCaret().setSelectionVisible(true);
				}
			});

			resultWrapCheckBox = new JCheckBox("Line Wrap");		
			resultWrapCheckBox.addItemListener(new ItemListener()
			{
				@Override
				public void itemStateChanged(ItemEvent ie)
				{
					resultText.setLineWrap(resultWrapCheckBox.isSelected());
					resultText.setWrapStyleWord(resultWrapCheckBox.isSelected());
				}
			});
	
			@NotNull JPanel resultControlPanel = new JPanel();
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
			colListPanel = new JPanel(new BorderLayout());
			
			@NotNull JPanel colListControlPanel = new JPanel(new BorderLayout());
			colListControlPanel.setBorder(BorderFactory.createEtchedBorder());

			@NotNull JPanel colListButtonPanel = new JPanel(new FlowLayout());

			colListSelectAllButton = new JButton("Select All");
			colListSelectAllButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent ae)
				{
					colList.setSelectionInterval(0, colList.getModel().getSize() - 1);
				}
			});
			
			colListSelectNoneButton = new JButton("Delselect All");
			colListSelectNoneButton.addActionListener(ae -> colList.clearSelection());

			colListSelectAllButton.setEnabled(false);
			colListSelectNoneButton.setEnabled(false);

			colListButtonPanel.add(colListSelectAllButton);
			colListButtonPanel.add(colListSelectNoneButton);
		
			colList = new JList(new DefaultListModel());
			colList.getModel().addListDataListener(new ListDataListener()
			{
				@Override
				public void contentsChanged(ListDataEvent lde)
				{
					updateSelectButtons();
				}

				@Override
				public void intervalAdded(ListDataEvent lde)
				{
					updateSelectButtons();
				}

				@Override
				public void intervalRemoved(ListDataEvent lde)
				{
					updateSelectButtons();
				}
				
				private void updateSelectButtons()
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
			logTextPanel = new JPanel(new BorderLayout());
			
			logText = new JTextArea();
			logText.setEditable(false);
			logText.setFont(fixedWidthFont);
			logTextScroll = new JScrollPane(logText);

			@NotNull JButton selectAllButton = new JButton("Select All");
			selectAllButton.setMnemonic(KeyEvent.VK_A);
			selectAllButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent ae)
				{
					logText.selectAll();
					logText.getCaret().setSelectionVisible(true);
				}
			});

			logWrapCheckBox = new JCheckBox("Line Wrap");
			logWrapCheckBox.addItemListener(new ItemListener()
			{
				@Override
				public void itemStateChanged(ItemEvent ie)
				{
					logText.setLineWrap(logWrapCheckBox.isSelected());
					logText.setWrapStyleWord(logWrapCheckBox.isSelected());
				}
			});
	
			logClearButton = new JButton("Clear Log");
			logClearButton.setMnemonic(KeyEvent.VK_C);
			logClearButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent ae)
				{
					logText.setText("");
					log("Log cleared");
				}
			});
	
			@NotNull JPanel logControlPanel = new JPanel();
			logControlPanel.add(logClearButton);
			logControlPanel.add(selectAllButton);
			logControlPanel.add(logWrapCheckBox);
	
			logTextPanel.add(logTextScroll, BorderLayout.CENTER);
			logTextPanel.add(logControlPanel, BorderLayout.SOUTH);
		}

	}

	class StatusPanel extends JPanel
	{	
		StatusPanel()
		{
			setLayout(new BorderLayout());

			@NotNull JPanel leftPanel = new JPanel();
			leftPanel.setBorder(BorderFactory.createEtchedBorder());
			leftPanel.setLayout(new BorderLayout());

			@NotNull JPanel rightPanel = new JPanel();
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

	public static void main(String[] args)
	{
		@NotNull final JavaPH applet = new JavaPH();
		@NotNull final JFrame frame = new JFrame();

		int frameHeight = applet.getIntProperty(PROP_APP_HEIGHT, APP_DEFAULT_HEIGHT);
		int frameWidth = applet.getIntProperty(PROP_APP_WIDTH, APP_DEFAULT_WIDTH);
		int frameX = applet.getIntProperty(PROP_APP_X_POSITION, -1);
		int frameY = applet.getIntProperty(PROP_APP_Y_POSITION, -1);

		if (frameHeight < APP_MIN_HEIGHT) {
			frameHeight = APP_DEFAULT_HEIGHT;
		}
		if (frameWidth < APP_MIN_WIDTH) {
			frameWidth = APP_DEFAULT_WIDTH;
		}

		applet.defaultPane = frame.getRootPane();
		applet.init();
		applet.restoreLookAndFeel(applet.getContentPane());
		
		frame.setContentPane(applet.getContentPane());
		frame.setIconImage((new ImageIcon(applet.getURL("img/ph-icon-smaller.gif"))).getImage());
		frame.setSize(frameWidth, frameHeight);
		frame.setTitle("JavaPH");

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		// if no previous X/Y position specified or if the previous X/Y
		// position is somehow off the screen then just center the frame
		frameX = (frameX < 0 || frameX > screenSize.width) ? (screenSize.width/2 - (frameWidth/2)) : frameX;
		frameY = (frameY < 0 || frameY > screenSize.height) ? (screenSize.height/2 - (frameHeight/2)) : frameY;
		
		frame.setLocation(frameX, frameY);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent we)
			{
				boolean needToStore = false;
				
				if (applet.savePosition)
				{
					needToStore = true;
					applet.setProperty(PROP_APP_HEIGHT, "" + frame.getHeight());
					applet.setProperty(PROP_APP_WIDTH, "" + frame.getWidth());
					applet.setProperty(PROP_APP_X_POSITION, "" + frame.getX());
					applet.setProperty(PROP_APP_Y_POSITION, "" + frame.getY());
				}
				else
				{
					if (applet.getIntProperty(PROP_APP_HEIGHT, APP_DEFAULT_HEIGHT) != APP_DEFAULT_HEIGHT)
					{
						needToStore = true;
						applet.setProperty(PROP_APP_HEIGHT, "" + APP_DEFAULT_HEIGHT);
					}
					if (applet.getIntProperty(PROP_APP_WIDTH, APP_DEFAULT_WIDTH) != APP_DEFAULT_WIDTH)
					{
						needToStore = true;
						applet.setProperty(PROP_APP_WIDTH, "" + APP_DEFAULT_WIDTH);
					}
					if (applet.getIntProperty(PROP_APP_X_POSITION, -1) != -1)
					{
						needToStore = true;
						applet.setProperty(PROP_APP_X_POSITION, "" + -1);
					}
					if (applet.getIntProperty(PROP_APP_Y_POSITION, -1) != -1)
					{
						needToStore = true;
						applet.setProperty(PROP_APP_Y_POSITION, "" + -1);
					}
				}
				
				if (needToStore) {
					applet.storeProperties();
				}

				System.exit(0);
			}
		});

		((JavaPH)applet).queryComboBox.getEditor().getEditorComponent().requestFocus();
	}

	public JavaPH()
	{
	}

	public void findText(@NotNull String text, boolean caseSensitive, boolean wrap)
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

	private void findTextInTable(@NotNull JTable table, @NotNull String text, boolean caseSensitive, boolean wrap)
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

		Object anObject;

		if (!inLastCell)
		{
			for (int r = startRow; r < table.getRowCount(); r++)
			{
				// if at the starting row then continue from the starting column
				// otherwise start from the first column
				for (int c = (r == startRow) ? startCol : 0; c < table.getColumnCount(); c++)
				{
					anObject = table.getValueAt(r, c);
					
					if (anObject == null)
					{
						continue;
					}
					else if (caseSensitive && anObject.toString().contains(text))
					{
						table.changeSelection(r, c, false, false);
						return;
					}
					else if (!caseSensitive && anObject.toString().toLowerCase().contains(text.toLowerCase()))
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
					anObject = table.getValueAt(r, c);
				
					if (anObject == null)
					{
						continue;
					}
					else if (caseSensitive && anObject.toString().contains(text))
					{
						table.changeSelection(r, c, false, false);
						return;
					}
					else if (!caseSensitive && anObject.toString().toLowerCase().contains(text.toLowerCase()))
					{
						table.changeSelection(r, c, false, false);
						return;
					}
				}
			}
		}
	}

	private void findTextInTextArea(@NotNull JTextArea textArea, @NotNull String text, boolean caseSensitive, boolean wrap)
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

		int location;
		
		if (caseSensitive) {
			location = textArea.getText().indexOf(text, startIndex);
		} else {
			location = textArea.getText().toLowerCase().indexOf(text.toLowerCase(), startIndex);
		}
		
		if (location > 0)
		{
			textArea.select(location, location + text.length());
			textArea.getCaret().setSelectionVisible(true);
		}
		else if (wrap && startIndex > 0)
		{
			startIndex = 0;
			
			if (caseSensitive) {
				location = textArea.getText().indexOf(text, startIndex);
			} else {
				location = textArea.getText().toLowerCase().indexOf(text.toLowerCase(), startIndex);
			}

			if (location > 0)
			{
				textArea.select(location, location + text.length());
				textArea.getCaret().setSelectionVisible(true);
			}
		}
	}

	@NotNull
	public String getCommand()
	{
		@NotNull StringBuffer out = new StringBuffer();
		
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
				int[] selectedFields = fieldList.getSelectedIndices();
				
				// will return default list of fields if nothing selected
				if (selectedFields != null &&  selectedFields.length > 0)
				{
					out.append(" return");
					
					for (int i = 0; i < selectedFields.length; i++)
					{
						out.append(" ");
						out.append(((QiField)fieldList.getModel().getElementAt(selectedFields[i])).getName());
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

	public JComboBox<QiCommand> getCommandComboBox()
	{
		return commandComboBox;
	}

	public JRootPane getDefaultPane()
	{
		return defaultPane;
	}

	public QiConnection getConnection()
	{
		return connection;
	}

	@Nullable
	public String getFieldSeparator()
	{
		return fieldSeparator;
	}

	private int getIntProperty(@NotNull String key, int defaultValue)
	{
		int intValue;

		try
		{
			@Nullable String stringValue = getProperty(key);

			if (stringValue == null) {
				throw new NumberFormatException();
			}
			
			intValue = Integer.parseInt(stringValue);
		}
		catch (NumberFormatException e)
		{
			intValue = defaultValue;
		}
		
		return intValue;
	}

	public int getIntPropertyDefault(@NotNull String key, int defaultValue)
	{
		int intValue;

		try
		{
			@Nullable String stringValue = getPropertyDefault(key);

			if (stringValue == null) {
				throw new NumberFormatException();
			}
			
			intValue = Integer.parseInt(stringValue);
		}
		catch (NumberFormatException e)
		{
			intValue = defaultValue;
		}
		
		return intValue;
	}

	@Nullable
	public String getLastCustomSeparator()
	{
		return customFieldSeparator;
	}

	public int getLoadFields()
	{
		return loadFields;
	}

	public JTextArea getLogText()
	{
		return logText;
	}

	@Nullable
	private String getProperty(@NotNull String key)
	{
		return properties.getProperty(key);
	}

	private String getProperty(String key, String defaultValue)
	{
		return properties.getProperty(key, defaultValue);
	}

	@Nullable
	public String getPropertyDefault(@NotNull String key)
	{
		return defaultProperties.getProperty(key);
	}

	private String getPropertyDefault(String key, String defaultValue)
	{
		return defaultProperties.getProperty(key, defaultValue);
	}

	public int getQueryRuntime()
	{
		return queryRuntime;
	}

	public JButton getQueryButton()
	{
		return queryButton;
	}

	public QueryComboBox getQueryComboBox()
	{
		return queryComboBox;
	}

	public ProgressMonitor getQueryProgressMonitor()
	{
		return queryProgressMonitor;
	}

	public JToolBar getQueryToolBar()
	{
		return queryToolBar;
	}

	public JTabbedPane getResultPanel()
	{
		return resultPanel;
	}

	public ResultTable getResultTable()
	{
		return resultTable;
	}

	public JTextArea getResultText()
	{
		return resultText;
	}

	@Nullable
	public QiServer getServer()
	{
		return (QiServer)serverComboBox.getSelectedItem();
	}

	@Nullable
	public URL getURL(@NotNull String location)
	{
		try
		{
			return (new File(location)).toURI().toURL();
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void init()
	{
		Browser.init();
	
		initStuff();

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
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		// Need to add status panel first so later panels
		// can have access to the status labels
		contentPane.add(new StatusPanel(), BorderLayout.SOUTH);
		contentPane.add(queryToolBar, BorderLayout.NORTH);
		contentPane.add(new ContentPanel(this), BorderLayout.CENTER);

		showToolBar(propertyEquals(PROP_DISPLAY_TOOLBAR, "true", "true"));

		defaultPane.setJMenuBar(new MainMenu(this));
		
		log("JavaPH initialized (" + ("Application") + " Mode)");
		log("Initializing default server");

		serverComboBox.setSelectedItem(QiServer.getDefaultServer());
		
		log("Default server initialized");
		
		showDefaultStatus();
	}

	private void initDialogs()
	{
		aboutDialog      = new AboutDialog(this);
		findDialog       = new FindDialog(this);
		propertiesDialog = new PropertiesDialog(this);
		queryToolBar     = new QueryToolBar(this);
		splashWindow     = new SplashWindow(this);
	}

	private void initIcons()
	{
		fieldCustomOff = new ImageIcon(getURL("img/field-custom-off.gif"));
		fieldCustomOn  = new ImageIcon(getURL("img/field-custom-on.gif"));
		fieldLoadOff   = new ImageIcon(getURL("img/field-load-off.gif"));
		fieldLoadOn    = new ImageIcon(getURL("img/field-load-on.gif"));
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
		QiServer.loadAllServers();

		commands = QiCommand.commands;
		servers  = QiServer.getServers();
	}

	private void initStuff()
	{
		initProperties();
		initServers();
		initIcons();
		initDialogs();
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
			defaultProperties.load((new File(PROP_FILE_DEF)).toURI().toURL().openStream());
			properties.load((new File(PROP_FILE_DEF)).toURI().toURL().openStream());
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.out.println("FileNotFound occurred when trying to load properties from " + PROP_FILE_DEF);
			System.exit(1);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.out.println("IOException occurred when trying to load properties from " + PROP_FILE_DEF);
			System.exit(1);
		}
	}

	private void loadFieldsForAllServers()
	{
		if (servers != null && !servers.isEmpty())
		{

			fieldProgressMonitor = new ProgressMonitor(defaultPane, "Loading Fields for Server", "", 0, servers.size());
			
			for (int i = 0; i < servers.size(); i++)
			{
				QiServer server = servers.elementAt(i);
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

	private void loadFieldsForServer(@NotNull QiServer server)
	{
		showStatus("Loading fields for " + server.getExpandedName(), true);

		Component component = ((RootPaneContainer)defaultPane.getTopLevelAncestor()).getGlassPane();

		component.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		component.setVisible(true);
		component.addMouseListener(new MouseAdapter() {});
		component.addKeyListener(new KeyAdapter() {});

		server.loadFields();
						
		component.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		component.setVisible(false);
						
		showStatus(server.getFieldStateMessage(), true);
	}

	private void loadProperties()
	{

		try
		{
			properties.load((new File(PROP_FILE)).toURI().toURL().openStream());
		}
		catch (FileNotFoundException e)
		{
		}
		catch (IOException e)
		{
		}
	}

	public void log(String logMessage)
	{
		if (logText != null)
		{
			logText.append((new Date(System.currentTimeMillis())).toString());
			logText.append(" :: ");
			logText.append(logMessage);
			logText.append("\n");
		}
	}

	private void populateColumnList()
	{
		@NotNull TableSorter tableModel = (TableSorter)resultTable.getModel();
		@NotNull DefaultListModel listModel = (DefaultListModel)colList.getModel();
		listModel.removeAllElements();
		
		for (int i = 0; i < tableModel.getColumnCount(); i++)
		{
			listModel.addElement(tableModel.getColumnName(i));
		}
		
		if (listModel.getSize() > 0) {
			colList.setSelectionInterval(0, listModel.getSize() - 1);
		}
	}

	private void populateFieldList(@NotNull QiServer server)
	{
		@NotNull DefaultListModel model = (DefaultListModel)fieldList.getModel();
		model.removeAllElements();
					
		if (server.getFieldState() == QiServer.FIELD_LOAD_TRUE)
		{
			@NotNull Vector fields = server.getFields();
						
			for (int i = 0; i < fields.size(); i++) {
				model.addElement(fields.elementAt(i));
			}
		}
					
		if (model.getSize() > 0) {
			fieldList.setSelectionInterval(0, model.getSize() - 1);
		}
						
		if (server.getFieldState() == QiServer.FIELD_LOAD_TRUE) {
			fieldLoadButton.setToolTipText("Reload fields for the selected server");
		} else {
			fieldLoadButton.setToolTipText("Load fields for the selected server");
		}
	}

	public boolean propertyDefaultEquals(@NotNull String key, String equalsValue)
	{
		return propertyDefaultEquals(key, null, equalsValue);
	}

	public boolean propertyDefaultEquals(@NotNull String key, @Nullable String defaultValue, String equalsValue)
	{
		@Nullable String value = (defaultValue == null) ? getPropertyDefault(key) : getPropertyDefault(key, defaultValue);
		
		if (value == null) {
			return false;
		} else {
			return value.equals(equalsValue);
		}
	}

	public boolean propertyEquals(@NotNull String key, String equalsValue)
	{
		return propertyEquals(key, null, equalsValue);
	}

	public boolean propertyEquals(@NotNull String key, @Nullable String defaultValue, String equalsValue)
	{
		@Nullable String value = (defaultValue == null) ? getProperty(key) : getProperty(key, defaultValue);
		
		if (value == null) {
			return false;
		} else {
			return value.equals(equalsValue);
		}
	}

	public void restoreLookAndFeel()
	{
		restoreLookAndFeel(getProperty(PROP_DEFAULT_LNF), defaultPane);
	}

	private void restoreLookAndFeel(@NotNull Component component)
	{
		restoreLookAndFeel(getProperty(PROP_DEFAULT_LNF), component);
	}

	public void restoreLookAndFeel(String lookAndFeel)
	{
		restoreLookAndFeel(lookAndFeel, defaultPane);
	}

	private void restoreLookAndFeel(@Nullable String lookAndFeel, @NotNull Component component)
	{

		@NotNull Component[] otherComponents = new Component[]
		{
			aboutDialog, colListPanel, fieldListPanel, findDialog, propertiesDialog, queryToolBar, splashWindow
		};
		
		if (lookAndFeel == null)
		{
			lookAndFeel = UIManager.getSystemLookAndFeelClassName();
			System.out.println("No look and feel specified, using system default (" + lookAndFeel + ")");	
		}

		int i;
		try
		{
			UIManager.setLookAndFeel(lookAndFeel);
			SwingUtilities.updateComponentTreeUI(component);
			
			// Must also include the dialogs and toolbars
			// due to the weirdness of potentially running
			// as either an application or an applet
			for (i = 0; i < otherComponents.length; i++) {
				if (otherComponents[i] != null) {
					SwingUtilities.updateComponentTreeUI(otherComponents[i]);
				}
			}
			
			return;
		}
		catch(Exception e)
		{
			// If we cannot set the look and feel to
			// what was specified in the properties file
			// then just continue and set system default
			e.printStackTrace();
			System.out.println("Exception occurred when trying to set custom look and feel");
		}
		
		try
		{
			// UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(component);

			// Must also include the dialogs and toolbars
			// due to the weirdness of potentially running
			// as either an application or an applet
			for (i = 0; i < otherComponents.length; i++) {
				if (otherComponents[i] != null) {
					SwingUtilities.updateComponentTreeUI(otherComponents[i]);
				}
			}

			return;
		}
		catch(Exception e)
		{
			// If we cannot set the look and feel to
			// what is set as the system look and feel
			// then just continue and allow the default
			// cross platform look at feel to be used (metal)
			e.printStackTrace();
			System.out.println("Exception occurred when trying to set default look and feel");
		}
	}

	public void setFieldQuoted(boolean quoted)
	{
		fieldQuoted = quoted;
	}

	public void setFieldSeparator(@NotNull String separator)
	{
		if (separator == null || separator.equals("")) {
			throw new IllegalArgumentException("Error: null or empty string passed to setFieldSeparator");
		}
			
		// log("Changing field separator string from " + fieldSeparator + " to " + separator);
		fieldSeparator = separator;
		
		// if not a standard separator (comma or tab) then set the custom separator as well
		if (!separator.equals(COMMA_SEPARATOR) && !separator.equals(TAB_SEPARATOR)) {
			customFieldSeparator = separator;
		}
	}

	public void setLoadFields(int load)
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

	public void setProperty(String key, String value)
	{
		properties.setProperty(key, value);
	}

	public void setQueryRuntime(int runtime)
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

	public void setSavePosition(boolean save)
	{
		savePosition = save;
	}

	public void showAboutDialog()
	{
		aboutDialog.setLocationRelativeTo(defaultPane);
		aboutDialog.setVisible(true);
	}

	public void showBrowserDialog()
	{
		Browser.dialogConfiguration(defaultPane);
	}

	public void showDefaultStatus()
	{
		showStatus(APP_DEFAULT_STATUS);
	}

	public void showFindDialog()
	{
		findDialog.setLocationRelativeTo(defaultPane);
		findDialog.setTitle("Find Text in " + JavaPH.TAB_LABELS[getResultPanel().getSelectedIndex()]);
		findDialog.setVisible(true);
	}

	public void showLog(boolean show)
	{
		if (logTextPanel != null)
		{
			int location = resultPanel.indexOfTab(SYSTEM_LOG_LABEL);

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
	}

	public void showPropertiesDialog()
	{
		propertiesDialog.setLocationRelativeTo(defaultPane);
		propertiesDialog.show();
	}

	private void showSplashWindow()
	{
		splashWindow.show();
			
		try
		{
			Thread.sleep(SPLASH_DISPLAY);
		}
		catch (InterruptedException e)
		{
		}
			
		splashWindow.dispose();
	}

	@Override
	public void showStatus(String status)
	{
		showStatus(status, false);
	}

	private void showStatus(String status, boolean logAlso)
	{
		if (statusLabel != null) {
			statusLabel.setText(" " + status);
		}
			
		if (logAlso) {
			log(status);
		}
	}

	public void showToolBar(boolean show)
	{
		if (queryToolBar != null) {
			queryToolBar.setVisible(show);
		}
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
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.out.println("FileNotFound occurred when trying to store properties to " + PROP_FILE);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.out.println("IOException occurred when trying to store properties to " + PROP_FILE);
		}
	}
}
