package com.bovilexics.javaph.ui;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

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

    public static @NotNull LoadFields getDefault()
    {
        return Selected;
    }

    public static @NotNull LoadFields fromValue(final int value)
    {
        for(final @NotNull LoadFields loadFields : values())
        {
            if (loadFields.getValue() == value)
            {
                return loadFields;
            }
        }
        throw new AssertionError(new String[]{"argument is out of range"});
    }

    public static @NotNull Optional<LoadFields> tryFromValue(final int value)
    {
        for(final @NotNull LoadFields loadFields : values())
        {
            if (loadFields.getValue() == value)
            {
                return Optional.of(loadFields);
            }
        }
        return Optional.empty();
    }
}
