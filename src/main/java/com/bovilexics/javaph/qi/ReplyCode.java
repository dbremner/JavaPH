package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

import static com.bovilexics.javaph.qi.ReplyCodeType.InfoOrAction;
import static com.bovilexics.javaph.qi.ReplyCodeType.Info;
import static com.bovilexics.javaph.qi.ReplyCodeType.HardError;
import static com.bovilexics.javaph.qi.ReplyCodeType.PhQueryCode;
import static com.bovilexics.javaph.qi.ReplyCodeType.Status;
import static com.bovilexics.javaph.qi.ReplyCodeType.SoftError;

@SuppressWarnings("SpellCheckingInspection")
public enum ReplyCode
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

    LR_PROGRESS	(100, "Nameserver search in progress", Status), // in progress
    LR_ECHO		(101, "Echoing nameserver cmd", Status), // echoing cmd
    LR_NUMRET	(102, "How many entries are being returned", Status), // how many entries are being returned
    LR_NONAME	(103, "No hostname found for IP address", Status), // no hostname found for IP address

    LR_OK		(200, "Success", Info), // success
    LR_RONLY	(201, "Nameserver database ready in read only mode", Info), // database ready in read only mode

    LR_MORE		(300, InfoOrAction), // need more info
    LR_LOGIN	(301, InfoOrAction), // encrypt this string
    LR_XLOGIN	(302, InfoOrAction), // print this prompt

    LR_TEMP		(400, SoftError), // temporary error
    LR_INTERNAL	(401, SoftError), // database error, possibly temporary
    LR_LOCK		(402, SoftError), // lock not obtained within timeout period
    LR_COULDA	(403, SoftError), // login would have been ok but db read only
    LR_DOWN		(475, SoftError), // database unavailable; try again later

    LR_ERROR	(500, HardError), // hard error; general
    LR_NOMATCH	(501, HardError), // no matches to query
    LR_TOOMANY	(502, HardError), // too many matches to query
    LR_AINFO	(503, HardError), // may not see that field
    LR_ASEARCH	(504, HardError), // may not search on that field
    LR_ACHANGE	(505, HardError), // may not change field
    LR_NOTLOG	(506, HardError), // must be logged in
    LR_FIELD	(507, HardError), // field unknown
    LR_ABSENT	(508, HardError), // field not present in entry
    LR_ALIAS	(509, HardError), // requested alias is already in use
    LR_AENTRY	(510, HardError), // may not change entry
    LR_ADD		(511, HardError), // may not add entries
    LR_VALUE	(512, HardError), // illegal value
    LR_OPTION	(513, HardError), // unknown option
    LR_UNKNOWN	(514, HardError), // unknown command
    LR_NOKEY	(515, HardError), // no indexed field found in query
    LR_AUTH		(516, HardError), // no authorization for query
    LR_READONLY	(517, HardError), // operation failed; database is read-only
    LR_LIMIT	(518, HardError), // too many entries selected for change
    LR_HISTORY	(519, HardError), // history substitution failed (obsolete)
    LR_XCPU		(520, HardError), // too much cpu used
    LR_ADDONLY	(521, HardError), // addonly option set and change command applied to a field with data
    LR_ISCRYPT	(522, HardError), // attempt to view encrypted field
    LR_NOANSWER	(523, HardError), // "answer" was expected but not gotten
    LR_BADHELP	(524, HardError), // help topics cannot contain slashes
    LR_NOEMAIL	(525, HardError), // email authentication failed
    LR_NOADDR	(526, HardError), // host name address not found in DNS
    LR_MISMATCH	(527, HardError), // host = gethostbyaddr(foo); foo != gethostbyname(host)
    LR_KDB5		(528, HardError), // general kerberos database error
    LR_NOAUTH	(529, HardError), // selected authentication method not avail
    LR_OFFCAMPUS	(590, HardError), // remote queries not allowed
    LR_NOCMD	(598, HardError), // no such command
    LR_SYNTAX	(599, HardError), // syntax error
    LR_AMBIGUOUS	(600, PhQueryCode); // ambiguous or multiple match

    private final int value;

    private final @NotNull String description;
    private final @NotNull ReplyCodeType type;

    ReplyCode(final int value, final @NotNull ReplyCodeType type)
    {
        this(value, "", type);
    }

    ReplyCode(final int value, final @NotNull String description, final @NotNull ReplyCodeType type)
    {
        this.value = value;
        this.description = description;
        this.type = type;
    }

    @Contract(pure = true)
    public int getValue()
    {
        return value;
    }

    @Contract(pure = true)
    public @NotNull String getDescription()
    {
        return description;
    }

    @Contract(pure = true)
    public @NotNull ReplyCodeType getReplyCodeType()
    {
        return type;
    }

    private static final @NotNull Map<ReplyCode, String> QiCodes = new EnumMap<>(ReplyCode.class);

    static
    {
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

    public static @NotNull ReplyCode fromInt(final int value)
    {
        for(final @NotNull ReplyCode replyCode : values())
        {
            if (replyCode.getValue() == value)
            {
                return replyCode;
            }
        }
        throw new IllegalArgumentException("unrecognized reply code");
    }

    public static @NotNull String toString(final int code)
    {
        final ReplyCode value = fromInt(code);
        return QiCodes.get(value);
    }
}
