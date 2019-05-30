package com.bovilexics.javaph.threads;

public enum ResultThreadState {

    // Starting
    RS_START(0),

    // Multiline response.
    RS_INPROGRESS(1),

    // We're done.
    RS_OK(2),

    // Oops!
    RS_UNKNOWN(3),

    // Qi error response.
    RS_ERROR(4);

    private final int value;

    public int getValue() {
        return value;
    }

    ResultThreadState(int value) {
        this.value = value;
    }
}