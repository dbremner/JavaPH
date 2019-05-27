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

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Robert Fernandes robert@bovilexics.com
 * 
 */
public final class QiAPI
{
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
	public final static int LR_PROGRESS	= 100; // in progress
	public final static int LR_ECHO		= 101; // echoing cmd
	public final static int LR_NUMRET	= 102; // how many entries are being returned
	public final static int LR_NONAME	= 103; // no hostname found for IP address
	
	public final static int LR_OK		= 200; // success
	public final static int LR_RONLY		= 201; // database ready in read only mode

	public final static int LR_MORE		= 300; // need more info
	public final static int LR_LOGIN		= 301; // encrypt this string
	public final static int LR_XLOGIN	= 302; // print this prompt

	public final static int LR_TEMP		= 400; // temporary error
	public final static int LR_INTERNAL	= 401; // database error, possibly temporary
	public final static int LR_LOCK		= 402; // lock not obtained within timeout period
	public final static int LR_COULDA	= 403; // login would have been ok but db read only
	public final static int LR_DOWN		= 475; // database unavailable; try again later

	public final static int LR_ERROR		= 500; // hard error; general
	public final static int LR_NOMATCH	= 501; // no matches to query
	public final static int LR_TOOMANY	= 502; // too many matches to query
	public final static int LR_AINFO		= 503; // may not see that field
	public final static int LR_ASEARCH	= 504; // may not search on that field
	public final static int LR_ACHANGE	= 505; // may not change field
	public final static int LR_NOTLOG	= 506; // must be logged in
	public final static int LR_FIELD		= 507; // field unknown
	public final static int LR_ABSENT	= 508; // field not present in entry
	public final static int LR_ALIAS		= 509; // requested alias is already in use
	public final static int LR_AENTRY	= 510; // may not change entry
	public final static int LR_ADD		= 511; // may not add entries
	public final static int LR_VALUE		= 512; // illegal value
	public final static int LR_OPTION	= 513; // unknown option
	public final static int LR_UNKNOWN	= 514; // unknown command
	public final static int LR_NOKEY		= 515; // no indexed field found in query
	public final static int LR_AUTH		= 516; // no authorization for query
	public final static int LR_READONLY	= 517; // operation failed; database is read-only
	public final static int LR_LIMIT		= 518; // too many entries selected for change
	public final static int LR_HISTORY	= 519; // history substitution failed (obsolete)
	public final static int LR_XCPU		= 520; // too much cpu used
	public final static int LR_ADDONLY	= 521; // addonly option set and change command applied to a field with data
	public final static int LR_ISCRYPT	= 522; // attempt to view encrypted field
	public final static int LR_NOANSWER	= 523; // "answer" was expected but not gotten
	public final static int LR_BADHELP	= 524; // help topics cannot contain slashes
	public final static int LR_NOEMAIL	= 525; // email authentication failed
	public final static int LR_NOADDR	= 526; // host name address not found in DNS
	public final static int LR_MISMATCH	= 527; // host = gethostbyaddr(foo); foo != gethostbyname(host)
	public final static int LR_KDB5		= 528; // general kerberos database error
	public final static int LR_NOAUTH	= 529; // selected authentication method not avail
	public final static int LR_OFFCAMPUS	= 590; // remote queries not allowed
	public final static int LR_NOCMD		= 598; // no such command
	public final static int LR_SYNTAX	= 599; // syntax error
	
	public final static int LR_AMBIGUOUS	= 600; // ambiguous or multiple match
	
	// LoginQi options
	public final static int LQ_AUTO		= 0x001; // attempt auto login
	public final static int LQ_INTER		= 0x002; // allowed to be interactive if needed.
	public final static int LQ_FWTK		= 0x004; // use FWTK auth server, if available
	public final static int LQ_KRB4		= 0x008; // use v4 Kerberos login, if available
	public final static int LQ_KRB5		= 0x010; // use v5 Kerberos login, if available
	public final static int LQ_GSS		= 0x020; // use GSS-API login, if available
	public final static int LQ_EMAIL		= 0x040; // use email login, if available
	public final static int LQ_PASSWORD	= 0x080; // password encrypted response to challenge
	public final static int LQ_CLEAR		= 0x100; // use clear-text passwords
	
	public final static int LQ_ALL		= (LQ_FWTK | LQ_KRB4 | LQ_KRB5 | LQ_GSS | LQ_EMAIL | LQ_PASSWORD | LQ_CLEAR);
	
	private final static Map<Integer, String> QiCodes = new HashMap<>();
	
	static
	{
		QiCodes.put(new Integer(LR_PROGRESS),	"Nameserver search in progress");
		QiCodes.put(new Integer(LR_ECHO),		"Echoing nameserver cmd");
		QiCodes.put(new Integer(LR_NUMRET),	"How many entries are being returned");
		QiCodes.put(new Integer(LR_NONAME),	"No hostname found for IP address");
		QiCodes.put(new Integer(LR_OK),		"Success");
		QiCodes.put(new Integer(LR_RONLY),		"Nameserver database ready in read only mode");
		QiCodes.put(new Integer(LR_MORE),		"More info needed to process nameserver query");
		QiCodes.put(new Integer(LR_LOGIN),		"Encrypt this string");
		QiCodes.put(new Integer(LR_XLOGIN),	"Prompt for password with enclosed challenge");
		QiCodes.put(new Integer(LR_TEMP),		"Temporary nameserver error");
		QiCodes.put(new Integer(LR_INTERNAL),	"Nameserver database error, possibly temporary");
		QiCodes.put(new Integer(LR_LOCK),		"Nameserver lock not obtained within timeout period");
		QiCodes.put(new Integer(LR_COULDA),	"Login would have been ok but db read only");
		QiCodes.put(new Integer(LR_DOWN),		"Nameserver database unavailable; try again later");
		QiCodes.put(new Integer(LR_ERROR),		"Nameserver hard error; general");
		QiCodes.put(new Integer(LR_NOMATCH),	"No matches to nameserver query");
		QiCodes.put(new Integer(LR_TOOMANY),	"Too many matches found to nameserver query");
		QiCodes.put(new Integer(LR_AINFO),		"May not see that nameserver field");
		QiCodes.put(new Integer(LR_ASEARCH),	"May not search on that nameserver field");
		QiCodes.put(new Integer(LR_ACHANGE),	"May not change that nameserver field");
		QiCodes.put(new Integer(LR_NOTLOG),	"Must be logged in to nameserver");
		QiCodes.put(new Integer(LR_FIELD),		"Unknown nameserver field");
		QiCodes.put(new Integer(LR_ABSENT),	"E-mail field not present in nameserver entry");
		QiCodes.put(new Integer(LR_ALIAS),		"Requested nameserver alias is already in use");
		QiCodes.put(new Integer(LR_AENTRY),	"May not change nameserver entry");
		QiCodes.put(new Integer(LR_ADD),		"May not add nameserver entries");
		QiCodes.put(new Integer(LR_VALUE),		"Illegal value");
		QiCodes.put(new Integer(LR_OPTION),	"Unknown nameserver option");
		QiCodes.put(new Integer(LR_UNKNOWN),	"Unknown nameserver command");
		QiCodes.put(new Integer(LR_NOKEY),		"No indexed field found in nameserver query");
		QiCodes.put(new Integer(LR_AUTH),		"No authorization for nameserver request");
		QiCodes.put(new Integer(LR_READONLY),	"Nameserver operation failed; database is read-only");
		QiCodes.put(new Integer(LR_LIMIT),		"Too many nameserver entries selected for change");
		QiCodes.put(new Integer(LR_HISTORY),	"History substitution failed (obsolete)");
		QiCodes.put(new Integer(LR_XCPU),		"Too much cpu used");
		QiCodes.put(new Integer(LR_ADDONLY),	"Addonly option set and change command applied to a field with data");
		QiCodes.put(new Integer(LR_ISCRYPT),	"Attempt to view encrypted field");
		QiCodes.put(new Integer(LR_NOANSWER),	"\"answer\" was expected but not gotten");
		QiCodes.put(new Integer(LR_BADHELP),	"Help topics cannot contain slashes");
		QiCodes.put(new Integer(LR_NOEMAIL),	"Email authentication failed");
		QiCodes.put(new Integer(LR_NOADDR),	"Host name address not found in DNS");
		QiCodes.put(new Integer(LR_MISMATCH),	"Host = gethostbyaddr(foo); foo != gethostbyname(host)");
		QiCodes.put(new Integer(LR_KDB5),		"General kerberos v5 database error");
		QiCodes.put(new Integer(LR_NOAUTH),	"Selected authentication method not available");
		QiCodes.put(new Integer(LR_OFFCAMPUS),	"Remote queries not allowed");
		QiCodes.put(new Integer(LR_NOCMD),		"No such command");
		QiCodes.put(new Integer(LR_SYNTAX),	"Syntax error");
		QiCodes.put(new Integer(LR_AMBIGUOUS),	"Multiple matches found for nameserver query");
	}

	public static String toString(int code)
	{
		final Integer boxedCode = code;
		return QiAPI.QiCodes.get(boxedCode);
	}
}
