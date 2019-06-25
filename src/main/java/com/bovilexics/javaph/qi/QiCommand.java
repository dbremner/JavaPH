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
public enum QiCommand
{
	;
	public static final @NotNull String ADD		= "add";
	public static final @NotNull String ANSWER	= "answer";
	public static final @NotNull String CHANGE	= "change";
	public static final @NotNull String CLEAR		= "clear";
	public static final @NotNull String DELETE	= "delete";
	public static final @NotNull String EXIT		= "exit";
	public static final @NotNull String FIELDS	= "fields";
	public static final @NotNull String HELP		= "help";
	public static final @NotNull String ID		= "id";
	public static final @NotNull String LOGIN		= "login";
	public static final @NotNull String LOGOUT	= "logout";
	public static final @NotNull String PH		= "ph";
	public static final @NotNull String QUERY		= "query";
	public static final @NotNull String QUIT		= "quit";
	public static final @NotNull String SET		= "set";
	public static final @NotNull String SITEINFO	= "siteinfo";
	public static final @NotNull String STATUS	= "status";
	public static final @NotNull String STOP		= "stop";
}
