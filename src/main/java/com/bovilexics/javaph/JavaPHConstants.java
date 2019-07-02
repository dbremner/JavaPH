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

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @author Robert Fernandes robert@bovilexics.com
 */
@SuppressWarnings("HardCodedStringLiteral")
public enum JavaPHConstants
{
	;
	public static final int APP_DEFAULT_HEIGHT = 700;
	public static final int APP_DEFAULT_WIDTH  = 700;
	public static final int APP_MIN_HEIGHT     = 350;
	public static final int APP_MIN_WIDTH      = 350;

	public static final @NotNull String APP_DEFAULT_STATUS = "Waiting for Query";
	
	public static final int QUERY_COMMAND  = 0;
	public static final int SPLASH_DISPLAY = 3000; // display time in ms

	public static final @NotNull String FIELD_LABEL_PREFIX  = " Return : ";
	public static final @NotNull String QUERY_LABEL_PREFIX  = " Query : ";
	public static final @NotNull String SERVER_LABEL_PREFIX = " Server : ";

	@NonNls
	public static final @NotNull String PROP_FILE_DEF = "javaph.default.properties";
	public static final @NotNull String PROP_FILE     = "javaph.properties";
	public static final @NotNull String PROP_HEADER   = String.format("Generated %s Properties File", JavaPHConstants.INFO_NAME);

	public static final @NotNull String PROP_APP_HEIGHT      = "javaph.startup.height";
	public static final @NotNull String PROP_APP_WIDTH       = "javaph.startup.width";
	public static final @NotNull String PROP_APP_X_POSITION  = "javaph.startup.xPosition";
	public static final @NotNull String PROP_APP_Y_POSITION  = "javaph.startup.yPosition";
	public static final @NotNull String PROP_DEFAULT_SERVER  = "javaph.default.server";
	public static final @NotNull String PROP_DISPLAY_LOG     = "javaph.startup.displayLog";
	public static final @NotNull String PROP_DISPLAY_SPLASH  = "javaph.startup.displaySplashWindow";
	public static final @NotNull String PROP_DISPLAY_TOOLBAR = "javaph.startup.displayToolbar";
	public static final @NotNull String PROP_LOAD_FIELDS     = "javaph.servers.loadFields";
	public static final @NotNull String PROP_QUERY_RUNTIME   = "javaph.default.queryRuntime";
	public static final @NotNull String PROP_SAVE_POSITION   = "javaph.startup.savePosition";
	public static final @NotNull String PROP_ROLL_TOOLBAR    = "javaph.startup.rolloverToolbar";
	
	@NonNls
	public static final @NotNull String COMMA_SEPARATOR  = ",";
	public static final @NotNull String CUSTOM_SEPARATOR = "|";
	@NonNls
	public static final @NotNull String TAB_SEPARATOR    = "\t";

	public static final @NotNull String FIELD_QUOTE = "\"";

	public static final @NotNull String INFO_AUTHOR    = "Robert Fernandes";
	public static final @NotNull String INFO_CONTACT   = "robert@bovilexics.com";
	public static final @NotNull String INFO_EMAIL = String.format("mailto:%s", INFO_CONTACT);
	public static final @NotNull String INFO_COPYRIGHT = "Copyright \u00a9 2003  bovilexics.com";
	public static final @NotNull String INFO_DATE      = "April 9, 2003";
	public static final @NotNull String INFO_HOME      = "http://www.bovilexics.com/software/javaph/";
	public static final @NotNull String INFO_NAME      = "JavaPH";

	public static final @NotNull String EXIT_INFO_NAME = String.format("Exit %s", INFO_NAME);
	public static final @NotNull String INFO_NAME_HOME_PAGE = String.format("%s Home Page", JavaPHConstants.INFO_NAME);
	public static final @NotNull String INFO_NAME_HELP = String.format("%s Help", INFO_NAME);
	public static final @NotNull String INFO_NAME_PROPERTIES = String.format("%s Properties", INFO_NAME);
	public static final @NotNull String INFO_NAME_INITIALIZED = String.format("%s initialized", INFO_NAME);
	public static final @NotNull String INFO_VERSION   = "v1.0";
	public static final @NotNull String INFO_NAME_VERSION = String.format("%s %s", INFO_NAME, INFO_VERSION);
	public static final @NotNull String ABOUT_JAVA_PH = String.format("About %s", INFO_NAME);
	public static final @NotNull String CONFIGURE_BROWSER_USED_TO_DISPLAY_INFO_NAME_HELP = String.format("Configure browser used to display %s help", INFO_NAME);
	public static final @NotNull String DISPLAYS_INFORMATION_ABOUT_INFO_NAME = String.format("Displays information about %s", INFO_NAME);
	public static final @NotNull String OPEN_INFO_NAME_HELP_WINDOW = String.format("Open %s help window", INFO_NAME);
	public static final @NotNull String SET_INFO_NAME_PREFERENCES = String.format("Set %s preferences", INFO_NAME);

	@SuppressWarnings("DuplicateStringLiteralInspection")
	public static final @NotNull String DEFAULT = "Default";

	public static final @NotNull String ADD_LEADING_AND_TRAILING_QUOTES_TO_ALL_TABLE_VALUES = "Add leading and trailing quotes to all table values";
	public static final @NotNull String ADD_QUOTES = "Add Quotes";
	public static final @NotNull String ALL_FIELDS = "All Fields";
	public static final @NotNull String APPLY = "Apply";
	public static final @NotNull String ARGUMENT_IS_OUT_OF_RANGE = "argument is out of range";
	public static final @NotNull String BROWSER_OPTIONS = "Browser Options";
	public static final @NotNull String CANCEL = "Cancel";
	public static final @NotNull String CANCELED = "Canceled";
	public static final @NotNull String CASE_SENSITIVE = "Case Sensitive";
	public static final @NotNull String CHOOSE_THE_FIELDS_THAT_QUERIES_WILL_RETURN = "Choose the fields that queries will return";
	public static final @NotNull String CLOSE = "Close";
	public static final @NotNull String COMMA = "Comma";
	public static final @NotNull String COPY = "Copy";
	public static final @NotNull String CORRUPT_FIELD_RESULTS_FOR_S = "Corrupt field results for %s";
	public static final @NotNull String COULDN_T_FIND_HEADER_FOR_THIS_COLUMN_S = "Couldn't find header for this column: %s";
	public static final @NotNull String CREATE_A_NEW_QUERY = "Create a New Query";
	public static final @NotNull String CUSTOM = "Custom";
	public static final @NotNull String CUSTOM_FIELDS = "Custom Fields";
	public static final @NotNull String CUT = "Cut";
	public static final @NotNull String DEFAULT_FIELDS = "Default Fields";
	public static final @NotNull String DEFAULT_SERVER_INITIALIZED = "Default server initialized";
	public static final @NotNull String DEFAULTS = "Defaults";
	public static final @NotNull String DESCRIPTION = "Description";
	public static final @NotNull String DESELECT_ALL = "Deselect All";
	public static final @NotNull String DISPLAY_SPLASH_SCREEN = "Display Splash Screen";
	public static final @NotNull String DISPLAY_SYSTEM_LOG = "Display System Log";
	public static final @NotNull String DISPLAY_TOOLBAR = "Display Toolbar";
	public static final @NotNull String EDIT = "Edit";
	public static final @NotNull String ERROR_COULD_NOT_CONNECT_TO_S = "Error: Could not connect to %s";
	public static final @NotNull String ERROR_FILE_NOT_FOUND_EXCEPTION_RECEIVED_WHEN_TRYING_TO_READ_FILE_S = "Error: FileNotFoundException received when trying to read file %s";
	public static final @NotNull String ERROR_INVALID_CONNECTION_QUERY_STOPPED = "Error: Invalid connection, query stopped.";
	public static final @NotNull String ERROR_INVALID_SERVER_ENTRY_IN_S_ON_LINE_D_S = "Error: Invalid server entry in %s on line %d --> %s";
	public static final @NotNull String ERROR_IOEXCEPTION_RECEIVED_WHEN_TRYING_TO_OPEN_S = "Error: IOException received when trying to open %s";
	public static final @NotNull String ERROR_IOEXCEPTION_RECEIVED_WHEN_TRYING_TO_READ_FILE_S = "Error: IOException received when trying to read file %s";
	public static final @NotNull String ERROR_IOEXCEPTION_RECEIVED_WHEN_TRYING_TO_SAVE_FILE_S = "Error: IOException received when trying to save file %s";
	public static final @NotNull String ERROR_IOEXCEPTION_RECEIVED_WHEN_TRYING_TO_WRITE_FILE_S = "Error: IOException received when trying to write file %s";
	public static final @NotNull String ERROR_NULL_OR_EMPTY_STRING_PASSED_TO_SET_FIELD_SEPARATOR = "Error: null or empty string passed to setFieldSeparator";
	public static final @NotNull String ERROR_ON_QI_LOGIN_S = "Error on Qi login: %s";
	public static final @NotNull String ERROR_ON_QI_LOGOUT_S = "Error on Qi logout: %s";
	public static final @NotNull String ERROR_PROPERTY_AND_DESCRIPTION_LINES_DO_NOT_REFER_TO_THE_SAME_FIELD_FOR_S = "Error: property and description lines do not refer to the same field for %s";
	public static final @NotNull String ERROR_QI_PROTOCOL_EXCEPTION_RECEIVED_WHEN_TRYING_TO_ADD_FIELD_TO_S = "Error: QiProtocolException received when trying to add field to %s";
	public static final @NotNull String ERROR_S = "Error: %s";
	public static final @NotNull String EXCEPTION = "Exception";
	public static final @NotNull String EXCEPTION_OCCURRED_WHEN_TRYING_TO_SET_DEFAULT_LOOK_AND_FEEL = "Exception occurred when trying to set default look and feel";
	public static final @NotNull String EXECUTE_QUERY = "Execute Query";
	public static final @NotNull String EXECUTING_QUERY = "Executing Query";
	public static final @NotNull String FIELD_LIST_FOR_S = "Field List for %s";
	public static final @NotNull String FIELD_SEPARATOR = "Field Separator";
	public static final @NotNull String FIELDS_NOT_YET_LOADED_FOR_S = "Fields not yet loaded for %s";
	public static final @NotNull String FILE_NOT_FOUND_OCCURRED_WHEN_TRYING_TO_LOAD_PROPERTIES_FROM_S = "FileNotFound occurred when trying to load properties from %s";
	public static final @NotNull String FILE_NOT_FOUND_OCCURRED_WHEN_TRYING_TO_STORE_PROPERTIES_TO_S = "FileNotFound occurred when trying to store properties to %s";
	public static final @NotNull String FILE_SAVE_FINISHED = "File save finished";
	public static final @NotNull String FIND = "Find";
	public static final @NotNull String FIND_NEXT = "Find Next";
	public static final @NotNull String FIND_TEXT = "Find Text";
	public static final @NotNull String FIND_TEXT_IN_S = "Find Text in %s";
	public static final @NotNull String FINISHED = "Finished";
	public static final @NotNull String GOT_ERROR_D_ON_LINE_S = "Got error %d on line --> %s";
	public static final @NotNull String HELP = "Help";
	public static final @NotNull String INITIALIZING_DEFAULT_SERVER = "Initializing default server";
	public static final @NotNull String IOEXCEPTION_OCCURRED_WHEN_TRYING_TO_LOAD_PROPERTIES_FROM_S = "IOException occurred when trying to load properties from %s";
	public static final @NotNull String IOEXCEPTION_OCCURRED_WHEN_TRYING_TO_STORE_PROPERTIES_TO_S = "IOException occurred when trying to store properties to %s";
	public static final @NotNull String LINE_WRAP = "Line Wrap";
	public static final @NotNull String LOAD_FIELDS_FOR_THE_SELECTED_SERVER = "Load fields for the selected server";
	public static final @NotNull String LOADING_FIELDS_FOR_S = "Loading fields for %s";
	public static final @NotNull String LOADING_FIELDS_FOR_SERVER = "Loading fields for Server";
	public static final @NotNull String MOVE_DOWN = "Move Down";
	public static final @NotNull String MOVE_UP = "Move Up";
	public static final @NotNull String NAME = "Name";
	public static final @NotNull String NEW_QUERY = "New Query";
	public static final @NotNull String NO_FIELDS_AVAILABLE_FOR_S = "No fields available for %s";
	public static final @NotNull String NOTHING_TO_SAVE_IN_S_TAB = "Nothing to save in %s tab";
	public static final @NotNull String OK = "OK";
	public static final @NotNull String PASTE = "Paste";
	public static final @NotNull String PORT = "Port";
	public static final @NotNull String PREFERENCES = "Preferences";
	public static final @NotNull String PROPERTIES = "Properties";
	public static final @NotNull String QI_SOCKET_UNINITIALIZED = "Qi socket uninitialized";
	public static final @NotNull String QUERY_CANCELED = "Query Canceled";
	public static final @NotNull String QUERY_FINISHED = "Query Finished";
	public static final @NotNull String QUERY_RUNNING_FOR_D_SECONDS = "Query running for %d seconds";
	public static final @NotNull String QUERY_RUNNING_PLEASE_WAIT = "Query Running... Please Wait";
	public static final @NotNull String QUERY_RUNTIME_D_TOO_HIGH_USING_MAXIMUM_VALUE_D = "Query runtime %d too high, using maximum value %d";
	public static final @NotNull String QUERY_RUNTIME_D_TOO_LOW_USING_MINIMUM_VALUE_D = "Query runtime %d too low, using minimum value %d";
	public static final @NotNull String QUERY_TIMED_OUT = "Query Timed Out";
	public static final @NotNull String RELOAD_FIELDS_FOR_THE_SELECTED_SERVER = "Reload fields for the selected server";
	public static final @NotNull String ROLLOVER_TOOLBAR = "Rollover Toolbar";
	public static final @NotNull String RUNNING_QUERY_S = "Running query \"%s\"";
	public static final @NotNull String SAVE_A_QUERY_RESULTS = "Save a Query Results";
	public static final @NotNull String SAVE_RESULTS = "Save Results";
	public static final @NotNull String SAVE_S = "Save %s";
	public static final @NotNull String SAVING_FILE_S = "Saving file %s";
	public static final @NotNull String SELECT_ALL = "Select All";
	public static final @NotNull String SELECT_THE_FIELDS_TO_BE_RETURNED = "Select the fields to be returned";
	public static final @NotNull String SERVER = "Server";
	public static final @NotNull String SERVER_ERROR_TRYING_TO_LOAD_FIELDS_FOR_S = "Server error trying to load fields for %s";
	public static final @NotNull String SUCCESSFULLY_LOADED_FIELDS_FOR_S = "Successfully loaded fields for %s";
	public static final @NotNull String SYSTEM_LOG = "System Log";
	public static final @NotNull String TAB = "Tab";
	public static final @NotNull String TABLE_RESULTS = "Table Results";
	public static final @NotNull String TEXT_RESULTS = "Text Results";
	public static final @NotNull String TIMEOUT = "Timeout";
	public static final @NotNull String TIMEOUT_TRYING_TO_LOAD_FIELDS_FOR_S = "Timeout trying to load fields for %s";
	public static final @NotNull String UNEXPECTED_END_OF_STREAM = "Unexpected end of stream";
	public static final @NotNull String UNKNOWN = "unknown";
	public static final @NotNull String UNKNOWN_STATE_S = "Unknown State: %s";
	public static final @NotNull String UNREACHABLE = "unreachable";
	public static final @NotNull String VIEW_SYSTEM_LOG = "View System Log";
	public static final @NotNull String VIEW_TOOLBAR = "View Toolbar";
	public static final @NotNull String WRAP_SEARCH = "Wrap Search";
	public static final @NotNull String SERVER_S = " Server : %s ";
	public static final @NotNull String PORT_S = " Port : %s ";
	public static final @NotNull String HELP_CONTENTS = "Help Contents";
	public static final @NotNull String CLICK_THIS_TO_START_RUNNING_THE_QUERY = "Click this to start running the query.";
	public static final @NotNull String EITHER_NO_FIELDS_AVAILABLE_OR_NO_FIELDS_SELECTED_RETURNING_DEFAULT_FIELDS_INSTEAD = "Either no fields available or no fields selected, returning default fields instead";
	public static final @NotNull String SHOW_HIDE_COLUMNS = "Show/Hide Columns";
	public static final @NotNull String RESET_COLUMN_WIDTHS = "Reset Column Widths";
	public static final @NotNull String COLUMN_LIST_FOR_RESULT_TABLE = "Column List for Result Table";
	public static final @NotNull String SELECT_THE_COLUMNS_TO_BE_DISPLAYED = "Select the columns to be displayed";
	public static final @NotNull String CLEAR_LOG = "Clear Log";
	public static final @NotNull String LOG_CLEARED = "Log cleared";
	public static final @NotNull String COPIES_THE_SELECTION_AND_PUTS_IT_ON_THE_CLIPBOARD = "Copies the selection and puts it on the clipboard";
	public static final @NotNull String INSERTS_CLIPBOARD_CONTENTS = "Inserts clipboard contents";
	public static final @NotNull String CUTS_THE_SELECTION_AND_PUTS_IT_ON_THE_CLIPBOARD = "Cuts the selection and puts it on the clipboard";
	public static final @NotNull String FINDS_THE_SPECIFIED_TEXT_IN_THE_QUERY_RESULTS = "Finds the specified text in the query results";
	public static final @NotNull String WINDOW = "Window";
	public static final @NotNull String SET_VISIBILITY_OF_SYSTEM_LOG = "Set visibility of system log";
	public static final @NotNull String SET_VISIBILITY_OF_TOOLBAR = "Set visibility of toolbar";
	public static final @NotNull String SET_ROLLOVER_PROPERTY_OF_TOOLBAR = "Set rollover property of toolbar";
	public static final @NotNull String NEW_QUERY_REQUESTED_RESULTS_CLEARED = "New query requested, results cleared";
	public static final @NotNull String SAVE_WINDOW_SIZE_POSITION = "Save Window Size / Position";
	public static final @NotNull String LOAD_FIELDS_MANUALLY = "Load Fields Manually";
	public static final @NotNull String LOAD_FIELDS_ON_SELECT = "Load Fields on Select";
	public static final @NotNull String LOAD_FIELDS_ON_STARTUP_SLOW = "Load Fields on Startup  ( Slow! )";
	public static final @NotNull String QUERY_RUNTIME_SECONDS = " Query Runtime (Seconds) ";
	public static final @NotNull String SERVER_OPTIONS = " Server Options ";
	public static final @NotNull String DEFAULT_SERVER = "Default Server : ";
	public static final @NotNull String LOAD_FIELDS_FOR_SERVERS_MANUALLY_USING_THE_BUTTON_HERE = "Load fields for servers manually using the button here";
	public static final @NotNull String LOAD_FIELDS_FOR_SERVERS_ONLY_WHEN_SELECTED_FROM_THE_SERVER_LIST = "Load fields for servers only when selected from the server list";
	public static final @NotNull String LOAD_FIELDS_FOR_ALL_SERVERS_WHEN_STARTING_THE_APPLICATION = "Load fields for all servers when starting the application";
	public static final @NotNull String STARTUP_OPTIONS = " Startup Options ";
    public static final @NotNull String INFO_CANNOT_PERFORM_A_SEARCH_ON_AN_EMPTY_TABLE = "Info: Cannot perform a search on an empty table";
	public static final @NotNull String INFO_CANNOT_SEARCH_PAST_THE_LAST_CELL_OF_A_TABLE = "Info: Cannot search past the last cell of a table";
	public static final @NotNull String BLANK_HOST_IS_NOT_PERMITTED = "Blank host is not permitted.";
	public static final @NotNull String BLANK_SERVER_IS_NOT_PERMITTED = "Blank server is not permitted.";
	public static final @NotNull String BLANK_NAME_IS_NOT_PERMITTED = "Blank name is not permitted.";
	public static final @NotNull String ERROR_PROP_AND_DESC_LINES_DO_NOT_MATCH_FOR_S_PROPS_S_DESC_S = "Error: property and description lines do not match for %s\nProps - %s\nDesc - %s";
}
