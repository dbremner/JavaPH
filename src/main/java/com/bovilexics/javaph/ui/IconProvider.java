package com.bovilexics.javaph.ui;

import org.jetbrains.annotations.NotNull;

import javax.swing.ImageIcon;

public interface IconProvider
{
    @NotNull String getURL(@NotNull String location);

    @NotNull ImageIcon getImageIcon(@NotNull String location);
}
