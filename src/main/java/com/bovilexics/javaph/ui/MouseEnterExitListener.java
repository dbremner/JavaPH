package com.bovilexics.javaph.ui;

import org.jetbrains.annotations.NotNull;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseEnterExitListener extends MouseAdapter
{
    private final @NotNull AbstractButton button;
    private final @NotNull Icon on;
    private final @NotNull Icon off;

    public MouseEnterExitListener(final @NotNull AbstractButton button, final @NotNull Icon on, final @NotNull Icon off)
    {
        this.button = button;
        this.on = on;
        this.off = off;
    }

    @Override
    public final void mouseEntered(final @NotNull MouseEvent e)
    {
        button.setIcon(on);
    }

    @Override
    public final void mouseExited(final @NotNull MouseEvent e)
    {
        button.setIcon(off);
    }
}
