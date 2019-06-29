package com.bovilexics.javaph.threads;

public enum ResultThreadState
{
    // Starting
    Starting,

    // Multiline response.
    InProgress,

    // We're done.
    Ok,

    // Oops!
    Unknown,

    // Qi error response.
    Error,

    //Execution is complete, any method calls are an assertion failure
    Stopped
}