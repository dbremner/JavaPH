package com.bovilexics.javaph.logging;

import org.jetbrains.annotations.NotNull;

public final class StatusErrorLogger extends PrintStreamLogger implements StatusLogger
{
    public StatusErrorLogger()
    {
        super(System.err);
    }

    @Override
    public void showStatus(final @NotNull String status)
    {
        log(status);
    }
}
