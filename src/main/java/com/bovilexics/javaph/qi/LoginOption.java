package com.bovilexics.javaph.qi;

public enum LoginOption
{
    ;
    // LoginQi options
    public static final int LQ_AUTO		= 0x001; // attempt auto login
    public static final int LQ_INTER		= 0x002; // allowed to be interactive if needed.
    public static final int LQ_FWTK		= 0x004; // use FWTK auth server, if available
    public static final int LQ_KRB4		= 0x008; // use v4 Kerberos login, if available
    public static final int LQ_KRB5		= 0x010; // use v5 Kerberos login, if available
    public static final int LQ_GSS		= 0x020; // use GSS-API login, if available
    public static final int LQ_EMAIL		= 0x040; // use email login, if available
    public static final int LQ_PASSWORD	= 0x080; // password encrypted response to challenge
    public static final int LQ_CLEAR		= 0x100; // use clear-text passwords

    public static final int LQ_ALL		= (LQ_FWTK | LQ_KRB4 | LQ_KRB5 | LQ_GSS | LQ_EMAIL | LQ_PASSWORD | LQ_CLEAR);
}
