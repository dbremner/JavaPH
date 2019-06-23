package com.bovilexics.javaph.ui;

import org.jetbrains.annotations.NotNull;

import javax.swing.JTabbedPane;
import java.awt.Event;
import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

public final class ControlTabDispatcher implements KeyEventDispatcher
{
    private final @NotNull JTabbedPane resultPanel;

    public ControlTabDispatcher(final @NotNull JTabbedPane resultPanel)
    {
        this.resultPanel = resultPanel;
    }

    @Override
    public boolean dispatchKeyEvent(final @NotNull KeyEvent e)
    {
        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_TAB)
        {
            // If we don't have this condition then the tab will switch twice,
            // once for the key pressed event and again for the key released event.
            if (e.getID() == Event.KEY_RELEASE)
            {
                int selected = resultPanel.getSelectedIndex();
                final int last = resultPanel.getTabCount() - 1;

                if (e.isShiftDown())
                {
                    if (selected <= 0)
                    {
                        selected = last;
                    }
                    else
                    {
                        selected--;
                    }
                }
                else
                {
                    if (selected < last)
                    {
                        selected++;
                    }
                    else
                    {
                        selected = 0;
                    }
                }

                resultPanel.setSelectedIndex(selected);
            }

            return true;
        }
        else
        {
            return false;
        }
    }
}
