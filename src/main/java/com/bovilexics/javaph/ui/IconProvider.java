package com.bovilexics.javaph.ui;

import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;

public interface IconProvider
{
    @NotNull String getURL(String location);

    @NotNull Icon getIcon(String location);
}
