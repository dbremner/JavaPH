package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface LineFactory
{
    /** Given a line as read from Qi, construct a Line.
     *
     * @param buffer as read from Qi, unaltered.
     *
     * @exception QiProtocolException if the line can't be parsed.
     */
    @SuppressWarnings("RedundantThrows")
    @Contract(pure = true)
    @NotNull Line create(final @NotNull String buffer) throws QiProtocolException;
}
