package com.bovilexics.javaph.ui;

import com.bovilexics.javaph.JavaPH;
import org.jetbrains.annotations.NotNull;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

final class DesktopHyperLinkLabelListener extends AbstractHyperLinkLabelListener
{
    private final @NotNull Desktop desktop;

    DesktopHyperLinkLabelListener(final @NotNull String url, final @NotNull String text, final @NotNull JavaPH parent)
    {
        super(url, text, parent);
        desktop = Desktop.getDesktop();
    }

    @Override
    protected void displayUrl(@NotNull String url, @NotNull String text) throws IOException
    {
        URI uri;
        try
        {
            uri = new URI(url);
        }
        catch (URISyntaxException e)
        {
            throw new IOException(e);
        }
        desktop.browse(uri);
    }
}
