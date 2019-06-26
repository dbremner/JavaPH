package com.bovilexics.javaph.ui;

import org.jetbrains.annotations.Contract;

public enum QueryRuntime
{
    MIN(10),
    DEFAULT(20),
    MAX(180);

    private final int value;

    QueryRuntime(final int value)
    {
        this.value = value;
    }

    @Contract(pure = true)
    public int getValue()
    {
        return value;
    }

    @Contract(pure = true)
    public static boolean overMaximum(final int input)
    {
        return input > MAX.getValue();
    }

    @Contract(pure = true)
    public static boolean underMinimum(final int input)
    {
        return input < MIN.getValue();
    }
}
