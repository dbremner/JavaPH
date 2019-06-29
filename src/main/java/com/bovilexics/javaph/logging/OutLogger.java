package com.bovilexics.javaph.logging;

public final class OutLogger extends PrintStreamLogger
{
    OutLogger()
    {
        super(System.out);
    }
}
