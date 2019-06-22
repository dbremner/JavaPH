package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.NotNull;

public interface Line
{
    int getCode();

    @NotNull String getField();

    int getIndex();

    @NotNull String getResponse();

    @NotNull String getTrimmedField();

    @NotNull String getTrimmedValue();

    @NotNull String getValue();

    @NotNull String getFieldValue();
}
