package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.NotNull;
import sun.jvm.hotspot.utilities.AssertionFailure;

import java.util.EnumMap;
import java.util.Map;

@SuppressWarnings("SpellCheckingInspection")
public enum QiReplyCode
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

    LR_PROGRESS	(100), // in progress
    LR_ECHO		(101), // echoing cmd
    LR_NUMRET	(102), // how many entries are being returned
    LR_NONAME	(103), // no hostname found for IP address

    LR_OK		(200), // success
    LR_RONLY	(201), // database ready in read only mode

    LR_MORE		(300), // need more info
    LR_LOGIN	(301), // encrypt this string
    LR_XLOGIN	(302), // print this prompt

    LR_TEMP		(400), // temporary error
    LR_INTERNAL	(401), // database error, possibly temporary
    LR_LOCK		(402), // lock not obtained within timeout period
    LR_COULDA	(403), // login would have been ok but db read only
    LR_DOWN		(475), // database unavailable; try again later

    LR_ERROR	(500), // hard error; general
    LR_NOMATCH	(501), // no matches to query
    LR_TOOMANY	(502), // too many matches to query
    LR_AINFO	(503), // may not see that field
    LR_ASEARCH	(504), // may not search on that field
    LR_ACHANGE	(505), // may not change field
    LR_NOTLOG	(506), // must be logged in
    LR_FIELD	(507), // field unknown
    LR_ABSENT	(508), // field not present in entry
    LR_ALIAS	(509), // requested alias is already in use
    LR_AENTRY	(510), // may not change entry
    LR_ADD		(511), // may not add entries
    LR_VALUE	(512), // illegal value
    LR_OPTION	(513), // unknown option
    LR_UNKNOWN	(514), // unknown command
    LR_NOKEY	(515), // no indexed field found in query
    LR_AUTH		(516), // no authorization for query
    LR_READONLY	(517), // operation failed; database is read-only
    LR_LIMIT	(518), // too many entries selected for change
    LR_HISTORY	(519), // history substitution failed (obsolete)
    LR_XCPU		(520), // too much cpu used
    LR_ADDONLY	(521), // addonly option set and change command applied to a field with data
    LR_ISCRYPT	(522), // attempt to view encrypted field
    LR_NOANSWER	(523), // "answer" was expected but not gotten
    LR_BADHELP	(524), // help topics cannot contain slashes
    LR_NOEMAIL	(525), // email authentication failed
    LR_NOADDR	(526), // host name address not found in DNS
    LR_MISMATCH	(527), // host = gethostbyaddr(foo); foo != gethostbyname(host)
    LR_KDB5		(528), // general kerberos database error
    LR_NOAUTH	(529), // selected authentication method not avail
    LR_OFFCAMPUS	(590), // remote queries not allowed
    LR_NOCMD	(598), // no such command
    LR_SYNTAX	(599), // syntax error
    LR_AMBIGUOUS	(600); // ambiguous or multiple match

    private final int value;

    private final @NotNull String description;

    QiReplyCode(final int value)
    {
        this(value, "");
    }

    QiReplyCode(final int value, final @NotNull String description)
    {
        this.value = value;
        this.description = description;
    }

    public int getValue()
    {
        return value;
    }
    
    public @NotNull String getDescription()
    {
        return description;
    }

    public @NotNull QiReplyCodeType getReplyCodeType()
    {
        switch (this)
        {
            case LR_PROGRESS:
            case LR_ECHO:
            case LR_NUMRET:
            case LR_NONAME:
                return QiReplyCodeType.Status;
            case LR_OK:
            case LR_RONLY:
                return QiReplyCodeType.Information;
            case LR_MORE:
            case LR_LOGIN:
            case LR_XLOGIN:
                return QiReplyCodeType.AdditionalInformationOrAction;
            case LR_TEMP:
            case LR_INTERNAL:
            case LR_LOCK:
            case LR_COULDA:
            case LR_DOWN:
                return QiReplyCodeType.TemporaryError;
            case LR_ERROR:
            case LR_TOOMANY:
            case LR_NOMATCH:
            case LR_AINFO:
            case LR_NOTLOG:
            case LR_ACHANGE:
            case LR_ASEARCH:
            case LR_FIELD:
            case LR_ABSENT:
            case LR_ALIAS:
            case LR_AENTRY:
            case LR_ADD:
            case LR_VALUE:
            case LR_OPTION:
            case LR_UNKNOWN:
            case LR_NOKEY:
            case LR_AUTH:
            case LR_READONLY:
            case LR_XCPU:
            case LR_HISTORY:
            case LR_ADDONLY:
            case LR_LIMIT:
            case LR_MISMATCH:
            case LR_NOADDR:
            case LR_NOEMAIL:
            case LR_BADHELP:
            case LR_NOANSWER:
            case LR_ISCRYPT:
            case LR_KDB5:
            case LR_NOAUTH:
            case LR_OFFCAMPUS:
            case LR_NOCMD:
            case LR_SYNTAX:
                return QiReplyCodeType.PermanentError;
            case LR_AMBIGUOUS:
                return QiReplyCodeType.PhQueryCode;
            default:
                throw new AssertionFailure("unreachable");
        }
    }

    private static final @NotNull Map<QiReplyCode, String> QiCodes = new EnumMap<>(QiReplyCode.class);

    static
    {
        QiCodes.put(LR_PROGRESS,	"Nameserver search in progress");
        QiCodes.put(LR_ECHO,		"Echoing nameserver cmd");
        QiCodes.put(LR_NUMRET,	"How many entries are being returned");
        QiCodes.put(LR_NONAME,	"No hostname found for IP address");
        QiCodes.put(LR_OK,		"Success");
        QiCodes.put(LR_RONLY,		"Nameserver database ready in read only mode");
        QiCodes.put(LR_MORE,		"More info needed to process nameserver query");
        QiCodes.put(LR_LOGIN,		"Encrypt this string");
        QiCodes.put(LR_XLOGIN,	"Prompt for password with enclosed challenge");
        QiCodes.put(LR_TEMP,		"Temporary nameserver error");
        QiCodes.put(LR_INTERNAL,	"Nameserver database error, possibly temporary");
        QiCodes.put(LR_LOCK,		"Nameserver lock not obtained within timeout period");
        QiCodes.put(LR_COULDA,	"Login would have been ok but db read only");
        QiCodes.put(LR_DOWN,		"Nameserver database unavailable; try again later");
        QiCodes.put(LR_ERROR,		"Nameserver hard error; general");
        QiCodes.put(LR_NOMATCH,	"No matches to nameserver query");
        QiCodes.put(LR_TOOMANY,	"Too many matches found to nameserver query");
        QiCodes.put(LR_AINFO,		"May not see that nameserver field");
        QiCodes.put(LR_ASEARCH,	"May not search on that nameserver field");
        QiCodes.put(LR_ACHANGE,	"May not change that nameserver field");
        QiCodes.put(LR_NOTLOG,	"Must be logged in to nameserver");
        QiCodes.put(LR_FIELD,		"Unknown nameserver field");
        QiCodes.put(LR_ABSENT,	"E-mail field not present in nameserver entry");
        QiCodes.put(LR_ALIAS,		"Requested nameserver alias is already in use");
        QiCodes.put(LR_AENTRY,	"May not change nameserver entry");
        QiCodes.put(LR_ADD,		"May not add nameserver entries");
        QiCodes.put(LR_VALUE,		"Illegal value");
        QiCodes.put(LR_OPTION,	"Unknown nameserver option");
        QiCodes.put(LR_UNKNOWN,	"Unknown nameserver command");
        QiCodes.put(LR_NOKEY,		"No indexed field found in nameserver query");
        QiCodes.put(LR_AUTH,		"No authorization for nameserver request");
        QiCodes.put(LR_READONLY,	"Nameserver operation failed; database is read-only");
        QiCodes.put(LR_LIMIT,		"Too many nameserver entries selected for change");
        QiCodes.put(LR_HISTORY,	"History substitution failed (obsolete)");
        QiCodes.put(LR_XCPU,		"Too much cpu used");
        QiCodes.put(LR_ADDONLY,	"Addonly option set and change command applied to a field with data");
        QiCodes.put(LR_ISCRYPT,	"Attempt to view encrypted field");
        QiCodes.put(LR_NOANSWER,	"\"answer\" was expected but not gotten");
        QiCodes.put(LR_BADHELP,	"Help topics cannot contain slashes");
        QiCodes.put(LR_NOEMAIL,	"Email authentication failed");
        QiCodes.put(LR_NOADDR,	"Host name address not found in DNS");
        QiCodes.put(LR_MISMATCH,	"Host = gethostbyaddr(foo); foo != gethostbyname(host)");
        QiCodes.put(LR_KDB5,		"General kerberos v5 database error");
        QiCodes.put(LR_NOAUTH,	"Selected authentication method not available");
        QiCodes.put(LR_OFFCAMPUS,	"Remote queries not allowed");
        QiCodes.put(LR_NOCMD,		"No such command");
        QiCodes.put(LR_SYNTAX,	"Syntax error");
        QiCodes.put(LR_AMBIGUOUS,	"Multiple matches found for nameserver query");
    }

    public static @NotNull QiReplyCode fromInt(final int value)
    {
        for(final @NotNull QiReplyCode replyCode : values())
        {
            if (replyCode.getValue() == value)
            {
                return replyCode;
            }
        }
        throw new AssertionFailure("unreachable");
    }

    public static @NotNull String toString(final int code)
    {
        final QiReplyCode value = fromInt(code);
        return QiCodes.get(value);
    }
}
