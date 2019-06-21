package com.bovilexics.javaph.ui;

import org.jetbrains.annotations.NotNull;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public abstract class ListDataAdapter implements ListDataListener {
    @Override
    public final void intervalAdded(final @NotNull ListDataEvent e) {
        changed();
    }

    @Override
    public final void intervalRemoved(final @NotNull ListDataEvent e) {
        changed();
    }

    @Override
    public final void contentsChanged(final @NotNull ListDataEvent e) {
        changed();
    }

    protected abstract void changed();
}
