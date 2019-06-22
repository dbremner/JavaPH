package com.bovilexics.javaph.ui;

import org.jetbrains.annotations.NotNull;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public final class ListDataAdapter implements ListDataListener
{
    private final @NotNull Runnable runnable;

    public ListDataAdapter(final @NotNull Runnable runnable)
    {
        this.runnable = runnable;
    }

    @Override
    public void intervalAdded(final @NotNull ListDataEvent e)
    {
        changed();
    }

    @Override
    public void intervalRemoved(final @NotNull ListDataEvent e)
    {
        changed();
    }

    @Override
    public void contentsChanged(final @NotNull ListDataEvent e)
    {
        changed();
    }

    private void changed()
    {
        runnable.run();
    }
}
