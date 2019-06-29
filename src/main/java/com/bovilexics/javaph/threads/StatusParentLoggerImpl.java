package com.bovilexics.javaph.threads;

import com.bovilexics.javaph.JavaPH;
import com.bovilexics.javaph.logging.StatusLogger;
import org.jetbrains.annotations.NotNull;

import javax.swing.SwingUtilities;

public final class StatusParentLoggerImpl implements StatusLogger
{
    private final @NotNull JavaPH parent;

    StatusParentLoggerImpl(final @NotNull JavaPH parent)
    {
        this.parent = parent;
    }

    @Override
    public void log(final @NotNull String message)
    {
        SwingUtilities.invokeLater(() ->parent.log(message));
    }

    @Override
    public void showStatus(final @NotNull String status)
    {
        SwingUtilities.invokeLater(() -> {
            parent.showStatus(status);
            parent.log(status);
        });
    }
}
