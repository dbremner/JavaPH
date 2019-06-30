package com.bovilexics.javaph.threads;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

final class ResultThreadResult
{
    private final @NotNull String rawResult;
    private final @NotNull ImmutableList<Object> headers;
    private final @NotNull Object[][] values;

    ResultThreadResult(
            final @NotNull String rawResult,
            final @NotNull Collection<Object> headers,
            final @NotNull Object[][] values)
    {

        this.rawResult = rawResult;
        this.headers = ImmutableList.copyOf(headers);
        this.values = values.clone();
    }

    @NotNull String getRawResult()
    {
        return rawResult;
    }

    @NotNull ImmutableList<Object> getHeaders()
    {
        return headers;
    }

    @NotNull Object[][] getValues()
    {
        return values.clone();
    }

    @Override
    public @NotNull String toString()
    {
        @NonNls final @NotNull String format =
                "ResultThreadResult{rawResult='%s', headers=%s, values=%s}";
        return String.format(format,
                rawResult,
                headers,
                Arrays.toString(values));
    }
}
