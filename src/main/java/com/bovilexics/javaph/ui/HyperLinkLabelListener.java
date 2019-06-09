package com.bovilexics.javaph.ui;

import com.Ostermiller.util.Browser;
import com.bovilexics.javaph.JavaPH;
import org.jetbrains.annotations.NotNull;

import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

final class HyperLinkLabelListener extends MouseAdapter
{
    private final @NotNull String url;
    private final @NotNull String text;
    private final @NotNull JavaPH parent;
    private final @NotNull JRootPane rootPane;

    HyperLinkLabelListener(final @NotNull String url, final @NotNull String text, final @NotNull JavaPH parent, final @NotNull JRootPane rootPane)
    {
        Browser.init();
        this.url = url;
        this.text = text;
        this.parent = parent;
        this.rootPane = rootPane;
    }

    @Override
    public void mouseClicked(final MouseEvent e)
    {
        try
        {
            Browser.displayURL(url, text);
        }
        catch (final @NotNull IOException ex)
        {
            final @NotNull String message = "Error: IOException received when trying to open " + url;
            parent.log(message);
            JOptionPane.showMessageDialog(rootPane, message, "Exception", JOptionPane.ERROR_MESSAGE);
        }
    }
}
