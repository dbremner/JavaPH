package com.bovilexics.javaph.logging;

import org.jetbrains.annotations.NotNull;

public final class StatusErrorLogger implements StatusLogger
{
    private final @NotNull Logger logger;

    StatusErrorLogger()
    {
        this(new ErrLoggerImpl());
    }

    StatusErrorLogger(final @NotNull Logger logger)
    {
        this.logger = logger;
    }

    @Override
    public void log(final @NotNull String message)
    {
        logger.log(message);
    }

    @Override
    public void showStatus(final @NotNull String status)
    {
        logger.log(status);
    }
}
