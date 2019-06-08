package com.bovilexics.javaph.ui;

import com.bovilexics.javaph.JavaPH;
import org.jetbrains.annotations.NotNull;

import javax.swing.JMenuItem;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

final class StatusMouseListener extends MouseAdapter
{
    private final @NotNull JavaPH parent;

    StatusMouseListener(final @NotNull JavaPH parent)
    {
        this.parent = parent;
    }

    @Override
    public void mouseEntered(final @NotNull MouseEvent e)
    {
        if (e.getSource() instanceof JMenuItem) {
            parent.showStatus(((JMenuItem)e.getSource()).getActionCommand());
        }
    }

    @Override
    public void mouseExited(final MouseEvent e)
    {
        parent.showDefaultStatus();
    }
}
