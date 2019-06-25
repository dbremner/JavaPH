package com.bovilexics.javaph.ui;

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

    public int getValue()
    {
        return value;
    }

    public static boolean overMaximum(final int input)
    {
        return input > MAX.getValue();
    }

    public static boolean underMinimum(final int input)
    {
        return input < MIN.getValue();
    }
}
