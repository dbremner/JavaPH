package com.bovilexics.javaph.actions;

import org.jetbrains.annotations.NotNull;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

public abstract class RunnableAction extends AbstractAction
{
    private final @NotNull Runnable runnable;

    protected RunnableAction(@NotNull String title, @NotNull Runnable runnable)
    {
        super(title);
        this.runnable = runnable;
    }

    @Override
    public final void actionPerformed(ActionEvent e)
    {
        runnable.run();
    }
}
