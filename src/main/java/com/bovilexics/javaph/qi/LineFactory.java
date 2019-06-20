package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.NotNull;

public interface LineFactory
{
    @NotNull Line create(final @NotNull String buffer) throws QiProtocolException;
}
