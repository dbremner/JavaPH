package com.bovilexics.javaph.qi;

public enum LoginOption
{
    AUTO(0x001),        // attempt auto login
    INTER(0x002),       // allowed to be interactive if needed.
    FWTK(0x004),        // use FWTK auth server, if available
    KRB4(0x008),        // use v4 Kerberos login, if available
    KRB5(0x010),        // use v5 Kerberos login, if available
    GSS(0x020),         // use GSS-API login, if available
    EMAIL(0x040),       // use email login, if available
    PASSWORD(0x080),    // password encrypted response to challenge
    CLEAR(0x100),       // use clear-text passwords
    ALL(0x004 | 0x008 | 0x010 | 0x020 | 0x040 | 0x080 | 0x100);

    private final int value;

    LoginOption(final int value)
    {
        this.value = value;
    }
}
