package com.bovilexics.javaph.qi;

public enum ReplyCodeType
{
    Status,
    /**
     * Information
     */
    Info,
    /**
     * Additional information or action
     */
    InfoOrAction,
    /**
     * temporary error
     */
    SoftError,
    /**
     * permanent error
     */
    HardError,
    PhQueryCode
}
