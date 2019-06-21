package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.NotNull;

public final class QiLineFactory implements LineFactory
{
    @Override
    public @NotNull Line create(final @NotNull String buffer) throws QiProtocolException
    {
        final @NotNull Line line = new QiLine(buffer);
        return line;
    }
}
