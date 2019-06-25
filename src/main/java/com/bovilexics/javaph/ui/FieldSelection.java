package com.bovilexics.javaph.ui;

import org.jetbrains.annotations.NotNull;

public enum FieldSelection
{
    DEFAULT(0),
    ALL(1),
    CUSTOM(2);

    private final int value;

    FieldSelection(final int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public static @NotNull FieldSelection fromValue(final int input)
    {
        for(final @NotNull FieldSelection fieldSelection : values())
        {
            if(fieldSelection.getValue() == input)
            {
                return fieldSelection;
            }
        }
        throw new AssertionError(new String[]{"argument is out of range"});
    }
}
