package com.bovilexics.javaph.ui;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public abstract class ListDataAdapter implements ListDataListener {
    @Override
    public final void intervalAdded(ListDataEvent e) {
        changed(e);
    }

    @Override
    public final void intervalRemoved(ListDataEvent e) {
        changed(e);
    }

    @Override
    public final void contentsChanged(ListDataEvent e) {
        changed(e);
    }

    protected abstract void changed(ListDataEvent e);
}
