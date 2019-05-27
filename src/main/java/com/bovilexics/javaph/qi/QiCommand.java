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
package com.bovilexics.javaph.qi;

/*
	Command Usage
	---------------
	add field=value...
	answer code
	change [field=]value...  make field=value...
	clear password
	delete [field=]value...
	exit
	fields [field...]
	help [{native|client} [topic...]]
	id information
	login alias
	logout
	ph [field=]value...  [return field...]
	query [field=]value...  [return field...]
	quit
	set option[=value]...
	siteinfo
	status
	stop
*/

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 
 * Commands supported by Qi
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public class QiCommand
{
	// public static final String ADD		= "add";
	// public static final String ANSWER	= "answer";
	// public static final String CHANGE	= "change";
	public static final String CLEAR		= "clear";
	// public static final String DELETE	= "delete";
	public static final String EXIT		= "exit";
	public static final String FIELDS	= "fields";
	public static final String HELP		= "help";
	// public static final String ID		= "id";	
	public static final String LOGIN		= "login";
	public static final String LOGOUT	= "logout";
	public static final String PH		= "ph";
	public static final String QUERY		= "query";
	public static final String QUIT		= "quit";
	// public static final String SET		= "set";
	public static final String SITEINFO	= "siteinfo";
	public static final String STATUS	= "status";
	public static final String STOP		= "stop";

	@NotNull
	public static final QiCommand[] commands =
	{
		new QiCommand(QUERY, "Query", true, true),
		new QiCommand(FIELDS, "Fields", false, false),
		new QiCommand(STATUS, "Status", false, false),
		new QiCommand(SITEINFO, "SiteInfo", false, false),
		new QiCommand(HELP, "Help", true, false)
	};

	private boolean listEditable = false;
	private boolean textEditable = false;

	private final String description;
	@NotNull
	private final String name;

	public static boolean isValidCommand(String command)
	{
		for (int i = 0; i < commands.length; i++)
		{
			if (commands[i].getName().equals(command))
				return true;
		}
		return false;
	}
	
	private QiCommand(@NotNull String aName, String aDescription, boolean isTextEditable, boolean isListEditable)
	{
		name = aName;
		description = aDescription;
		textEditable = isTextEditable;
		listEditable = isListEditable;
	}

	@NotNull
	public String getCommand()
	{
		return name + " ";
	}
	
	@Nullable
	public String getDescription()
	{
		return (description == null) ? name : description;
	}

	@NotNull
	public String getName()
	{
		return name;
	}

	public boolean isListEditable()
	{
		return listEditable;
	}

	public boolean isTextEditable()
	{
		return textEditable;
	}
	
	public String toString()
	{
		return description;
	}
}
