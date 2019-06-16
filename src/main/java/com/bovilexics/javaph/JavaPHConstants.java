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

import com.bovilexics.javaph.ui.Tab;

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

	public static final String APP_DEFAULT_STATUS = "Waiting for Query";
	
	public static final int QUERY_COMMAND  = 0;
	public static final int SPLASH_DISPLAY = 3000; // display time in ms

	public static final int QUERY_RUNTIME_DEF = 20;
	public static final int QUERY_RUNTIME_MIN = 10;
	public static final int QUERY_RUNTIME_MAX = 180;

	public static final int RESULT_TEXT_TAB  = Tab.ResultText.getIndex();
	public static final int RESULT_TABLE_TAB = Tab.ResultTable.getIndex();
	public static final int SYSTEM_LOG_TAB   = Tab.SystemLog.getIndex();

	public static final String RESULT_TEXT_LABEL  = Tab.ResultText.getLabel();
	public static final String RESULT_TABLE_LABEL = Tab.ResultTable.getLabel();
	public static final String SYSTEM_LOG_LABEL   = Tab.SystemLog.getLabel();

	public static final String[] TAB_LABELS =
	{
		Tab.ResultText.getLabel(), Tab.ResultTable.getLabel(), Tab.SystemLog.getLabel()
	};

	public static final String[] TAB_FILENAMES =
	{
		Tab.ResultText.getFilename(), Tab.ResultTable.getFilename(), Tab.SystemLog.getFilename()
	};

	public static final int FIELD_DEFAULT = 0;
	public static final int FIELD_ALL     = 1;
	public static final int FIELD_CUSTOM  = 2;
	
	public static final int LOAD_FIELDS_DEF      = 1;
	public static final int LOAD_FIELDS_MANUAL   = 0;
	public static final int LOAD_FIELDS_SELECTED = 1;
	public static final int LOAD_FIELDS_STARTUP  = 2;
	
	public static final String FIELD_LABEL_PREFIX  = " Return : ";
	public static final String PORT_LABEL_PREFIX   = " Port : ";
	public static final String PORT_LABEL_SUFFIX   = " ";
	public static final String QUERY_LABEL_PREFIX  = " Query : ";
	public static final String SERVER_LABEL_PREFIX = " Server : ";
	public static final String SERVER_LABEL_SUFFIX = " ";
	
	public static final String PROP_FILE_DEF = "javaph.default.properties";
	public static final String PROP_FILE     = "javaph.properties";
	public static final String PROP_HEADER   = "Generated JavaPH Properties File";

	public static final String PROP_APP_HEIGHT      = "javaph.startup.height";
	public static final String PROP_APP_WIDTH       = "javaph.startup.width";
	public static final String PROP_APP_X_POSITION  = "javaph.startup.xPosition";
	public static final String PROP_APP_Y_POSITION  = "javaph.startup.yPosition";
	public static final String PROP_DEFAULT_LNF     = "javaph.default.lnf";
	public static final String PROP_DEFAULT_SERVER  = "javaph.default.server";
	public static final String PROP_DISPLAY_LOG     = "javaph.startup.displayLog";
	public static final String PROP_DISPLAY_SPLASH  = "javaph.startup.displaySplashWindow";
	public static final String PROP_DISPLAY_TOOLBAR = "javaph.startup.displayToolbar";
	public static final String PROP_LOAD_FIELDS     = "javaph.servers.loadFields";
	public static final String PROP_QUERY_RUNTIME   = "javaph.default.queryRuntime";
	public static final String PROP_SAVE_POSITION   = "javaph.startup.savePosition";
	public static final String PROP_ROLL_TOOLBAR    = "javaph.startup.rolloverToolbar";
	
	public static final String COMMA_SEPARATOR  = ",";
	public static final String CUSTOM_SEPARATOR = "|";
	public static final String TAB_SEPARATOR    = "\t";

	public static final String FIELD_QUOTE = "\"";

	public static final String INFO_AUTHOR    = "Robert Fernandes";
	public static final String INFO_CONTACT   = "robert@bovilexics.com";
	public static final String INFO_COPYRIGHT = "Copyright \u00a9 2003  bovilexics.com";
	public static final String INFO_DATE      = "April 9, 2003";
	public static final String INFO_HOME      = "http://www.bovilexics.com/software/javaph/";
	public static final String INFO_NAME      = "JavaPH";
	public static final String INFO_VERSION   = "v1.0";
}
