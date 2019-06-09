package com.bovilexics.javaph.ui;

import com.bovilexics.javaph.JavaPH;
import org.jetbrains.annotations.NotNull;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

abstract class AbstractHyperLinkLabelListener extends MouseAdapter
{
    private final @NotNull String url;
    private final @NotNull String text;
    private final @NotNull JavaPH parent;

    protected AbstractHyperLinkLabelListener(final @NotNull String url, final @NotNull String text, final @NotNull JavaPH parent)
    {
        this.url = url;
        this.text = text;
        this.parent = parent;
    }

    @Override
    public final void mouseClicked(final MouseEvent e)
    {
        try
        {
            displayUrl(url, text);
        }
        catch (final @NotNull IOException ex)
        {
            final @NotNull String message = "Error: IOException received when trying to open " + url;
            parent.log(message);
            parent.showErrorDialog(message, "Exception");
        }
    }

    protected abstract void displayUrl(final @NotNull String url, final @NotNull String text) throws IOException;

}
