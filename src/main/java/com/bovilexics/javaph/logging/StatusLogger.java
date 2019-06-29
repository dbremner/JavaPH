package com.bovilexics.javaph.logging;

import org.jetbrains.annotations.NotNull;

public interface StatusLogger extends Logger
{
    void showStatus(@NotNull String status);
}
