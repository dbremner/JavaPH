package com.bovilexics.javaph.ui;

import com.Ostermiller.util.Browser;
import com.bovilexics.javaph.JavaPH;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

final class HyperLinkLabelListener extends AbstractHyperLinkLabelListener
{
    HyperLinkLabelListener(final @NotNull String url, final @NotNull String text, final @NotNull JavaPH parent)
    {
        super(url, text, parent);
        Browser.init();
    }

    @Override
    protected void displayUrl(@NotNull final String url, @NotNull final String text) throws IOException
    {
        Browser.displayURL(url, text);
    }
}
