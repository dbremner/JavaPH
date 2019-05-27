/*
 * Copyright (C) 2001-2002 Stephen Ostermiller
 * http://ostermiller.org/contact.pl?regarding=Java+Utilities
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
package com.bovilexics.javaph.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

@SuppressWarnings("ALL")
class BrowserCommandLexer
{

    private static void main(@NotNull String args[])
    {
        try
        {
            final Object obj;
            if(args.length > 0)
            {
                @NotNull final File file = new File(args[0]);
                if(file.exists())
                {
                    if(file.canRead()) {
                        obj = new FileInputStream(file);
                    } else {
                        throw new IOException("Could not open " + args[0]);
                    }
                } else
                {
                    throw new IOException("Could not find " + args[0]);
                }
            } else
            {
                obj = System.in;
            }
            @NotNull final BrowserCommandLexer browsercommandlexer = new BrowserCommandLexer(((InputStream) (obj)));
            @Nullable String s;
            while((s = browsercommandlexer.getNextToken()) != null) {
                System.out.println(s);
            }
        }
        catch(IOException ioexception)
        {
            System.out.println(ioexception.getMessage());
        }
    }

    @NotNull
    private static String unescape(@NotNull String s)
    {
        @NotNull final StringBuffer stringbuffer = new StringBuffer(s.length());
        for(int i = 0; i < s.length(); i++)
        {
            if(s.charAt(i) == '\\' && i < s.length()) {
                i++;
            }
            stringbuffer.append(s.charAt(i));
        }

        return stringbuffer.toString();
    }

    BrowserCommandLexer(Reader reader)
    {
        yy_lexical_state = 0;
        yy_buffer = new char[16384];
        yy_atBOL = true;
        yy_reader = reader;
    }

    BrowserCommandLexer(@NotNull InputStream inputstream)
    {
        this(((Reader) (new InputStreamReader(inputstream))));
    }

    @NotNull
    private static int[] yy_unpack()
    {
        @NotNull final int[] ai = new int[36];
        int i = 0;
        i = yy_unpack("\001\002\001\003\001\004\001\005\001\002\001\006\001\000\005\002\004\000\001\005\001\007\001\b\001\t\001\005\001\n\001\005\001\013\001\b\001\f\001\b\001\r\001\005\001\007\001\b\001\013\001\b\001\f\001\b\001\016", i, ai);
        return ai;
    }

    private static int yy_unpack(@NotNull String s, int i, int ai[])
    {
        int j = 0;
        int k = i;
        for(int l = s.length(); j < l;)
        {
            int i1 = s.charAt(j++);
            int j1 = s.charAt(j++);
            j1--;
            do {
                ai[k++] = j1;
            }
            while(--i1 > 0);
        }

        return k;
    }

    @NotNull
    private static char[] yy_unpack_cmap(@NotNull String s)
    {
        @NotNull char ac[] = new char[0x10000];
        int i = 0;
        int j = 0;
        while(i < 26) 
        {
            int k = s.charAt(i++);
            char c = s.charAt(i++);
            do {
                ac[j++] = c;
            }
            while(--k > 0);
        }
        return ac;
    }

    private boolean yy_refill()
        throws IOException
    {
        if(yy_startRead > 0)
        {
            System.arraycopy(yy_buffer, yy_startRead, yy_buffer, 0, yy_endRead - yy_startRead);
            yy_endRead -= yy_startRead;
            yy_currentPos -= yy_startRead;
            yy_markedPos -= yy_startRead;
            yy_pushbackPos -= yy_startRead;
            yy_startRead = 0;
        }
        if(yy_currentPos >= yy_buffer.length)
        {
            @NotNull char ac[] = new char[yy_currentPos * 2];
            System.arraycopy(yy_buffer, 0, ac, 0, yy_buffer.length);
            yy_buffer = ac;
        }
        int i = yy_reader.read(yy_buffer, yy_endRead, yy_buffer.length - yy_endRead);
        if(i < 0)
        {
            return true;
        } else
        {
            yy_endRead += i;
            return false;
        }
    }

    public final void yyclose()
        throws IOException
    {
        yy_atEOF = true;
        yy_endRead = yy_startRead;
        if(yy_reader != null) {
            yy_reader.close();
        }
    }

    public final void yyreset(Reader reader)
        throws IOException
    {
        yyclose();
        yy_reader = reader;
        yy_atBOL = true;
        yy_atEOF = false;
        yy_endRead = yy_startRead = 0;
        yy_currentPos = yy_markedPos = yy_pushbackPos = 0;
        yyline = yychar = yycolumn = 0;
        yy_lexical_state = 0;
    }

    public final int yystate()
    {
        return yy_lexical_state;
    }

    public final void yybegin(int i)
    {
        yy_lexical_state = i;
    }

    @NotNull
    public final String yytext()
    {
        return new String(yy_buffer, yy_startRead, yy_markedPos - yy_startRead);
    }

    public final char yycharat(int i)
    {
        return yy_buffer[yy_startRead + i];
    }

    public final int yylength()
    {
        return yy_markedPos - yy_startRead;
    }

    private void yy_ScanError(int i)
    {
        String s;
        try
        {
            s = YY_ERROR_MSG[i];
        }
        catch(ArrayIndexOutOfBoundsException arrayindexoutofboundsexception)
        {
            s = YY_ERROR_MSG[0];
        }
        throw new Error(s);
    }

    private void yypushback(int i)
    {
        if(i > yylength()) {
            yy_ScanError(3);
        }
        yy_markedPos -= i;
    }

    @Nullable
    public String getNextToken()
        throws IOException
    {
        int i1 = yy_endRead;
        char ac[] = yy_buffer;
        @NotNull char ac1[] = yycmap;
        @NotNull int ai[] = yytrans;
        @NotNull int ai1[] = yy_rowMap;
        @NotNull byte abyte0[] = YY_ATTRIBUTE;
        do
        {
            int l = yy_markedPos;
            int i = -1;
			int j = yy_currentPos = yy_startRead = l;
            // int j;
            // int k = j = yy_currentPos = yy_startRead = l;
            yy_state = yy_lexical_state;
            char c;
            byte byte0;
label0:
            do
            {
                do
                {
                    if(j < i1)
                    {
                        c = ac[j++];
                    } else
                    {
                        if(yy_atEOF)
                        {
                            c = '\uFFFF';
                            break label0;
                        }
                        yy_currentPos = j;
                        yy_markedPos = l;
                        boolean flag = yy_refill();
                        j = yy_currentPos;
                        l = yy_markedPos;
                        ac = yy_buffer;
                        i1 = yy_endRead;
                        if(flag)
                        {
                            c = '\uFFFF';
                            break label0;
                        }
                        c = ac[j++];
                    }
                    int j1 = ai[ai1[yy_state] + ac1[c]];
                    if(j1 == -1) {
                        break label0;
                    }
                    yy_state = j1;
                    byte0 = abyte0[yy_state];
                } while((byte0 & 1) != 1);
                i = yy_state;
                l = j;
            } while((byte0 & 8) != 8);
            yy_markedPos = l;
            switch(i)
            {
            case 0: // '\0'
            case 1: // '\001'
            case 4: // '\004'
            case 9: // '\t'
                return unescape(yytext());

            case 8: // '\b'
            case 10: // '\n'
            case 12: // '\f'
            case 13: // '\r'
                return unescape(yytext().substring(1, yytext().length() - 1));

            case 5: // '\005'
            case 6: // '\006'
            case 7: // '\007'
            case 11: // '\013'
            case 14: // '\016'
            default:
                if(c == '\uFFFF' && yy_startRead == yy_currentPos)
                {
                    yy_atEOF = true;
                    return null;
                }
                yy_ScanError(2);
                break;

            case 2: // '\002'
            case 3: // '\003'
            case 15: // '\017'
            case 16: // '\020'
            case 17: // '\021'
                break;
            }
        } while(true);
    }

    public static final int YYEOF = -1;
    private static final int YY_BUFFERSIZE = 16384;
    public static final int YYINITIAL = 0;
    private static final String yycmap_packed = "\t\000\002\002\001\000\002\002\022\000\001\002\001\000\001\003\036\000\001\000\032\000\001\001\uFFA3\0";
    private static final char yycmap[] = yy_unpack_cmap("\t\000\002\002\001\000\002\002\022\000\001\002\001\000\001\003\036\000\001\000\032\000\001\001\uFFA3\0");
    private static final int yy_rowMap[] = {
        0, 4, 8, 12, 16, 8, 20, 24, 4, 28, 
        16, 32, 12, 24
    };
    private static final String yy_packed0 = "\001\002\001\003\001\004\001\005\001\002\001\006\001\000\005\002\004\000\001\005\001\007\001\b\001\t\001\005\001\n\001\005\001\013\001\b\001\f\001\b\001\r\001\005\001\007\001\b\001\013\001\b\001\f\001\b\001\016";
    private static final int yytrans[] = yy_unpack();
    private static final int YY_UNKNOWN_ERROR = 0;
    private static final int YY_ILLEGAL_STATE = 1;
    private static final int YY_NO_MATCH = 2;
    private static final int YY_PUSHBACK_2BIG = 3;
    private static final String YY_ERROR_MSG[] = {
        "Unkown internal scanner error", "Internal error: unknown state", "Error: could not match input", "Error: pushback value was too large"
    };
    private static final byte YY_ATTRIBUTE[] = {
        1, 1, 1, 9, 1, 0, 0, 0, 1, 1, 
        1, 0, 9, 1
    };
    private Reader yy_reader;
    private int yy_state;
    private int yy_lexical_state;
    private char yy_buffer[];
    private int yy_markedPos;
    private int yy_pushbackPos;
    private int yy_currentPos;
    private int yy_startRead;
    private int yy_endRead;
    private int yyline;
    private int yychar;
    private int yycolumn;
    private boolean yy_atBOL;
    private boolean yy_atEOF;

}
