package com.bovilexics.javaph.ui;

import org.jetbrains.annotations.NotNull;

public enum LoadFields
{
    Manual(0),
    Selected(1),
    Startup(2);

    private final int value;

    LoadFields(final int value)
    {

        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    @SuppressWarnings("SameReturnValue")
    public static @NotNull LoadFields getDefault()
    {
        return Selected;
    }

    @SuppressWarnings("StaticMethodOnlyUsedInOneClass")
    public static @NotNull LoadFields fromOrDefault(final int value)
    {
        for(final @NotNull LoadFields loadFields : values())
        {
            if (loadFields.getValue() == value)
            {
                return loadFields;
            }
        }
        return getDefault();
    }
}
