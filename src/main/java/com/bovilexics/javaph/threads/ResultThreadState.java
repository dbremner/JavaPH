package com.bovilexics.javaph.threads;

public enum ResultThreadState
{
    // Starting
    RS_START,

    // Multiline response.
    RS_INPROGRESS,

    // We're done.
    RS_OK,

    // Oops!
    RS_UNKNOWN,

    // Qi error response.
    RS_ERROR
}