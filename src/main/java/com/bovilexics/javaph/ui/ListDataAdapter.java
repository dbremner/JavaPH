package com.bovilexics.javaph.ui;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public abstract class ListDataAdapter implements ListDataListener {
    @Override
    public final void intervalAdded(final ListDataEvent e) {
        changed(e);
    }

    @Override
    public final void intervalRemoved(final ListDataEvent e) {
        changed(e);
    }

    @Override
    public final void contentsChanged(final ListDataEvent e) {
        changed(e);
    }

    protected abstract void changed(ListDataEvent e);
}
