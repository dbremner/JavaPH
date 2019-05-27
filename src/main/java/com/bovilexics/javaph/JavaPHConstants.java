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

/**
 * @author Robert Fernandes robert@bovilexics.com
 */
public interface JavaPHConstants
{	
	int APP_DEFAULT_HEIGHT = 700;
	int APP_DEFAULT_WIDTH  = 700;
	int APP_MIN_HEIGHT     = 350;
	int APP_MIN_WIDTH      = 350;

	String APP_DEFAULT_STATUS = "Waiting for Query";
	
	int QUERY_COMMAND  = 0;
	int SPLASH_DISPLAY = 3000; // display time in ms

	int QUERY_RUNTIME_DEF = 20;
	int QUERY_RUNTIME_MIN = 10;
	int QUERY_RUNTIME_MAX = 180;

	int RESULT_TEXT_TAB  = 0;
	int RESULT_TABLE_TAB = 1;
	int SYSTEM_LOG_TAB   = 2;

	String RESULT_TEXT_LABEL  = "Text Results";
	String RESULT_TABLE_LABEL = "Table Results";
	String SYSTEM_LOG_LABEL   = "System Log";

	String[] TAB_LABELS =
	{
		RESULT_TEXT_LABEL, RESULT_TABLE_LABEL, SYSTEM_LOG_LABEL
	};

	String[] TAB_FILENAMES =
	{
		"javaph-result.txt", "javaph-result.csv", "javaph-log.txt"
	};

	int FIELD_DEFAULT = 0;
	int FIELD_ALL     = 1;
	int FIELD_CUSTOM  = 2;
	
	int LOAD_FIELDS_DEF      = 1;
	int LOAD_FIELDS_MANUAL   = 0;
	int LOAD_FIELDS_SELECTED = 1;
	int LOAD_FIELDS_STARTUP  = 2;
	
	String FIELD_LABEL_PREFIX  = " Return : ";
	String PORT_LABEL_PREFIX   = " Port : ";
	String PORT_LABEL_SUFFIX   = " ";
	String QUERY_LABEL_PREFIX  = " Query : ";
	String SERVER_LABEL_PREFIX = " Server : ";
	String SERVER_LABEL_SUFFIX = " ";
	
	String PROP_FILE_DEF = "javaph.default.properties";
	String PROP_FILE     = "javaph.properties";
	String PROP_HEADER   = "Generated JavaPH Properties File";

	String PROP_APP_HEIGHT      = "javaph.startup.height";
	String PROP_APP_WIDTH       = "javaph.startup.width";
	String PROP_APP_X_POSITION  = "javaph.startup.xPosition";
	String PROP_APP_Y_POSITION  = "javaph.startup.yPosition";
	String PROP_DEFAULT_LNF     = "javaph.default.lnf";
	String PROP_DEFAULT_SERVER  = "javaph.default.server";
	String PROP_DISPLAY_LOG     = "javaph.startup.displayLog";
	String PROP_DISPLAY_SPLASH  = "javaph.startup.displaySplashWindow";
	String PROP_DISPLAY_TOOLBAR = "javaph.startup.displayToolbar";
	String PROP_LOAD_FIELDS     = "javaph.servers.loadFields";
	String PROP_QUERY_RUNTIME   = "javaph.default.queryRuntime";
	String PROP_SAVE_POSITION   = "javaph.startup.savePosition";
	String PROP_ROLL_TOOLBAR    = "javaph.startup.rolloverToolbar";
	
	String COMMA_SEPARATOR  = ",";
	String CUSTOM_SEPARATOR = "|";
	String TAB_SEPARATOR    = "\t";

	String FIELD_QUOTE = "\"";

	String INFO_AUTHOR    = "Robert Fernandes";
	String INFO_CONTACT   = "robert@bovilexics.com";
	String INFO_COPYRIGHT = "Copyright \u00a9 2003  bovilexics.com";
	String INFO_DATE      = "April 9, 2003";
	String INFO_HOME      = "http://www.bovilexics.com/software/javaph/";
	String INFO_NAME      = "JavaPH";
	String INFO_VERSION   = "v1.0";
}
