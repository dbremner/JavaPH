package com.bovilexics.javaph.qi;

public enum QiFieldState
{
    FIELD_LOAD_ERROR(0),
    FIELD_LOAD_FALSE(1),
    FIELD_LOAD_TRUE(2),
    SERVER_ERROR(3);

    private final int value;

    public int getValue() {
        return value;
    }

    QiFieldState(final int value) {
        this.value = value;
    }
}