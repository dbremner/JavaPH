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

/**
 * 
 * Commands supported by Qi
 *
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
@SuppressWarnings("WeakerAccess")
public final class QiCommand
{
	// public static final String ADD		= "add";
	// public static final String ANSWER	= "answer";
	// public static final String CHANGE	= "change";
	public static final @NotNull String CLEAR		= "clear";
	// public static final String DELETE	= "delete";
	public static final @NotNull String EXIT		= "exit";
	public static final @NotNull String FIELDS	= "fields";
	public static final @NotNull String HELP		= "help";
	// public static final String ID		= "id";	
	public static final @NotNull String LOGIN		= "login";
	public static final @NotNull String LOGOUT	= "logout";
	public static final @NotNull String PH		= "ph";
	public static final @NotNull String QUERY		= "query";
	public static final @NotNull String QUIT		= "quit";
	// public static final String SET		= "set";
	public static final @NotNull String SITEINFO	= "siteinfo";
	public static final @NotNull String STATUS	= "status";
	public static final @NotNull String STOP		= "stop";

	public static final @NotNull QiCommand[] commands =
	{
		new QiCommand(QUERY, "Query", true, true),
		new QiCommand(FIELDS, "Fields", false, false),
		new QiCommand(STATUS, "Status", false, false),
		new QiCommand(SITEINFO, "SiteInfo", false, false),
		new QiCommand(HELP, "Help", true, false)
	};

	private final boolean listEditable;
	private final boolean textEditable;

	private final @NotNull String description;
	private final @NotNull String name;

	public static boolean isValidCommand(final @NotNull String command)
	{
		for (final @NotNull QiCommand qiCommand : commands) {
			if (qiCommand.getName().equals(command)) {
				return true;
			}
		}
		return false;
	}
	
	private QiCommand(final @NotNull String aName, final @NotNull String aDescription, final boolean isTextEditable, final boolean isListEditable)
	{
		name = aName;
		description = aDescription;
		textEditable = isTextEditable;
		listEditable = isListEditable;
	}

	public @NotNull String getCommand()
	{
		return name + " ";
	}
	
	public @NotNull String getDescription()
	{
		return description;
	}

	public @NotNull String getName()
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
	
	@Override
    public @NotNull String toString()
	{
		return description;
	}
}
