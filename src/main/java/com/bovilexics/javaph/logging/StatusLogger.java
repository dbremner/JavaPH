package com.bovilexics.javaph.logging;

import org.jetbrains.annotations.NotNull;

public interface StatusLogger extends Logger
{
    @Deprecated
    void showStatus(@NotNull String status);
}
