package com.bovilexics.javaph.ui;

import org.jetbrains.annotations.NotNull;

import javax.swing.ImageIcon;

public interface IconProvider
{
    @NotNull String getURL(String location);

    @NotNull ImageIcon getImageIcon(String location);
}
