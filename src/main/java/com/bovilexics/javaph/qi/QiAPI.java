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

/**
 * 
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
@SuppressWarnings({"WeakerAccess", "SpellCheckingInspection"})
public enum QiAPI
{
    ;
/*
	Reply codes
	-----------
	1XX - status
	2XX - information
	3XX - additional information or action needed
	4XX - temporary errors
	5XX - permanent errors
	6XX - phquery specific codes
*/

    public static final int LR_PROGRESS	= 100; // in progress
	public static final int LR_ECHO		= 101; // echoing cmd
	public static final int LR_NUMRET	= 102; // how many entries are being returned
	public static final int LR_NONAME	= 103; // no hostname found for IP address
	
	public static final int LR_OK		= 200; // success
	public static final int LR_RONLY		= 201; // database ready in read only mode

	public static final int LR_MORE		= 300; // need more info
	public static final int LR_LOGIN		= 301; // encrypt this string
	public static final int LR_XLOGIN	= 302; // print this prompt

	public static final int LR_TEMP		= 400; // temporary error
	public static final int LR_INTERNAL	= 401; // database error, possibly temporary
	public static final int LR_LOCK		= 402; // lock not obtained within timeout period
	public static final int LR_COULDA	= 403; // login would have been ok but db read only
	public static final int LR_DOWN		= 475; // database unavailable; try again later

	public static final int LR_ERROR		= 500; // hard error; general
	public static final int LR_NOMATCH	= 501; // no matches to query
	public static final int LR_TOOMANY	= 502; // too many matches to query
	public static final int LR_AINFO		= 503; // may not see that field
	public static final int LR_ASEARCH	= 504; // may not search on that field
	public static final int LR_ACHANGE	= 505; // may not change field
	public static final int LR_NOTLOG	= 506; // must be logged in
	public static final int LR_FIELD		= 507; // field unknown
	public static final int LR_ABSENT	= 508; // field not present in entry
	public static final int LR_ALIAS		= 509; // requested alias is already in use
	public static final int LR_AENTRY	= 510; // may not change entry
	public static final int LR_ADD		= 511; // may not add entries
	public static final int LR_VALUE		= 512; // illegal value
	public static final int LR_OPTION	= 513; // unknown option
	public static final int LR_UNKNOWN	= 514; // unknown command
	public static final int LR_NOKEY		= 515; // no indexed field found in query
	public static final int LR_AUTH		= 516; // no authorization for query
	public static final int LR_READONLY	= 517; // operation failed; database is read-only
	public static final int LR_LIMIT		= 518; // too many entries selected for change
	public static final int LR_HISTORY	= 519; // history substitution failed (obsolete)
	public static final int LR_XCPU		= 520; // too much cpu used
	public static final int LR_ADDONLY	= 521; // addonly option set and change command applied to a field with data
	public static final int LR_ISCRYPT	= 522; // attempt to view encrypted field
	public static final int LR_NOANSWER	= 523; // "answer" was expected but not gotten
	public static final int LR_BADHELP	= 524; // help topics cannot contain slashes
	public static final int LR_NOEMAIL	= 525; // email authentication failed
	public static final int LR_NOADDR	= 526; // host name address not found in DNS
	public static final int LR_MISMATCH	= 527; // host = gethostbyaddr(foo); foo != gethostbyname(host)
	public static final int LR_KDB5		= 528; // general kerberos database error
	public static final int LR_NOAUTH	= 529; // selected authentication method not avail
	public static final int LR_OFFCAMPUS	= 590; // remote queries not allowed
	public static final int LR_NOCMD		= 598; // no such command
	public static final int LR_SYNTAX	= 599; // syntax error
	
	public static final int LR_AMBIGUOUS	= 600; // ambiguous or multiple match
}
