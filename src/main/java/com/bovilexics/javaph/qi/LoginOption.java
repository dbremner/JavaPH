package com.bovilexics.javaph.qi;

public enum LoginOption
{
    Auto(0x001),        // attempt auto login
    Inter(0x002),       // allowed to be interactive if needed.
    Fwtk(0x004),        // use FWTK auth server, if available
    Krb4(0x008),        // use v4 Kerberos login, if available
    Krb5(0x010),        // use v5 Kerberos login, if available
    Gss(0x020),         // use GSS-API login, if available
    Email(0x040),       // use email login, if available
    Password(0x080),    // password encrypted response to challenge
    Clear(0x100),       // use clear-text passwords
    All(0x004 | 0x008 | 0x010 | 0x020 | 0x040 | 0x080 | 0x100);

    private final int value;

    LoginOption(final int value)
    {
        this.value = value;
    }
}
