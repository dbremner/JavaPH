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
        final Object source = e.getSource();
        if (!(source instanceof JMenuItem))
        {
            return;
        }
        parent.showStatus(((JMenuItem)source).getActionCommand());
    }

    @Override
    public void mouseExited(final MouseEvent e)
    {
        parent.showDefaultStatus();
    }
}
