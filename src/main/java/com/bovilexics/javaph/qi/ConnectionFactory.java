package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface ConnectionFactory
{
    /**
     * Creates a QiConnection from a QiServer object which must then be initialized using
     * <b>connect(host, port)</b>
     */
    @Contract(pure = true)
    @NotNull Connection create(@NotNull Server server);
}
