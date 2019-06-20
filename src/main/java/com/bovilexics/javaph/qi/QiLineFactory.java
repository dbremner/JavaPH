package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.NotNull;

public final class QiLineFactory implements LineFactory
{
    public @NotNull Line create(final @NotNull String buffer) throws QiProtocolException
    {
        Line line = new QiLine(buffer);
        return line;
    }
}
