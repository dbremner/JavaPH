package com.bovilexics.javaph.actions;

import org.jetbrains.annotations.NotNull;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;

abstract class RunnableAction extends AbstractAction
{
    private final @NotNull String title;

    private final @NotNull Runnable runnable;

    RunnableAction(final @NotNull String title, final @NotNull Runnable runnable)
    {
        super(title);
        this.title = title;
        this.runnable = runnable;
    }

    RunnableAction(final @NotNull String title, final @NotNull Runnable runnable, final @NotNull Icon icon)
    {
        super(title, icon);
        this.title = title;
        this.runnable = runnable;
    }

    @Override
    public final void actionPerformed(final @NotNull ActionEvent e)
    {
        runnable.run();
    }

    @Override
    public final @NotNull String toString()
    {
        return title;
    }
}
