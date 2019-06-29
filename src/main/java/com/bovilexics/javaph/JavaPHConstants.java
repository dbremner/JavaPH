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
	public static final @NotNull String PORT_LABEL_PREFIX   = " Port : ";
	public static final @NotNull String PORT_LABEL_SUFFIX   = " ";
	public static final @NotNull String QUERY_LABEL_PREFIX  = " Query : ";
	public static final @NotNull String SERVER_LABEL_PREFIX = " Server : ";
	public static final @NotNull String SERVER_LABEL_SUFFIX = " ";
	
	@NonNls
	public static final @NotNull String PROP_FILE_DEF = "javaph.default.properties";
	public static final @NotNull String PROP_FILE     = "javaph.properties";
	public static final @NotNull String PROP_HEADER   = String.format("Generated %s Properties File", JavaPHConstants.INFO_NAME);

	public static final @NotNull String PROP_APP_HEIGHT      = "javaph.startup.height";
	public static final @NotNull String PROP_APP_WIDTH       = "javaph.startup.width";
	public static final @NotNull String PROP_APP_X_POSITION  = "javaph.startup.xPosition";
	public static final @NotNull String PROP_APP_Y_POSITION  = "javaph.startup.yPosition";
	public static final @NotNull String PROP_DEFAULT_LNF     = "javaph.default.lnf";
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
	public static final @NotNull String INFO_COPYRIGHT = "Copyright \u00a9 2003  bovilexics.com";
	public static final @NotNull String INFO_DATE      = "April 9, 2003";
	public static final @NotNull String INFO_HOME      = "http://www.bovilexics.com/software/javaph/";
	public static final @NotNull String INFO_NAME      = "JavaPH";
	public static final @NotNull String INFO_VERSION   = "v1.0";
	public static final @NotNull String ABOUT_JAVA_PH = String.format("About %s", INFO_NAME);
	public static final @NotNull String SELECT_ALL = "Select All";
	public static final @NotNull String DESELECT_ALL = "Deselect All";
	public static final @NotNull String QUERY_FINISHED = "Query Finished";
	public static final @NotNull String PASTE = "Paste";
	public static final @NotNull String COPY = "Copy";
    public static final @NotNull String QI_SOCKET_UNINITIALIZED = "Qi socket uninitialized";
    public static final @NotNull String UNEXPECTED_END_OF_STREAM = "Unexpected end of stream";
	public static final @NotNull String FINISHED = "Finished";
	public static final @NotNull String FILE_SAVE_FINISHED = "File save finished";
	public static final @NotNull String UNREACHABLE = "unreachable";
	public static final @NotNull String ARGUMENT_IS_OUT_OF_RANGE = "argument is out of range";
	public static final @NotNull String NOTHING_TO_SAVE_IN_S_TAB = "Nothing to save in %s tab";
	public static final @NotNull String ERROR_IOEXCEPTION_RECEIVED_WHEN_TRYING_TO_SAVE_FILE_S = "Error: IOException received when trying to save file %s";
	public static final @NotNull String ERROR_S = "Error: %s";
	public static final @NotNull String ROLLOVER_TOOLBAR = "Rollover Toolbar";
	public static final @NotNull String DISPLAY_TOOLBAR = "Display Toolbar";
	public static final @NotNull String CHANGE_TO_S_LOOK_AND_FEEL = "Change to %s look and feel";
	public static final @NotNull String ERROR_IOEXCEPTION_RECEIVED_WHEN_TRYING_TO_OPEN_S = "Error: IOException received when trying to open %s";
	public static final @NotNull String UNKNOWN_STATE_S = "Unknown State: %s";
	public static final @NotNull String UNKNOWN = "unknown";
	public static final @NotNull String OK = "OK";
	public static final @NotNull String LINE_WRAP = "Line Wrap";
	public static final @NotNull String EDIT = "Edit";
	public static final @NotNull String CUT = "Cut";
	public static final @NotNull String ERROR_ON_QI_LOGIN_S = "Error on Qi login: %s";
    public static final @NotNull String CANCELED = "Canceled";
	public static final @NotNull String MOTIF = "Motif";
	public static final @NotNull String WINDOWS = "Windows";
	@SuppressWarnings("DuplicateStringLiteralInspection")
	public static final @NotNull String DEFAULT = "Default";
	public static final @NotNull String METAL = "Metal";
	public static final @NotNull String MAC = "Mac";
	public static final @NotNull String SAVE_S = "Save %s";
	public static final @NotNull String HELP = "Help";
	public static final @NotNull String APPLY = "Apply";
	public static final @NotNull String CANCEL = "Cancel";
	public static final @NotNull String DEFAULTS = "Defaults";
}
