package com.bovilexics.javaph.ui;

import org.jetbrains.annotations.NotNull;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;

public final class ContentPanel extends JPanel
{
    public ContentPanel(final @NotNull JPanel queryPanel, final @NotNull JTabbedPane resultPanel)
    {
        setBorder(BorderFactory.createEtchedBorder());
        setLayout(new BorderLayout());
        add(queryPanel, BorderLayout.NORTH);
        add(resultPanel, BorderLayout.CENTER);
    }
}
