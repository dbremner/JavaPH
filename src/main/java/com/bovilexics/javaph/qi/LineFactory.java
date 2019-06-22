package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.NotNull;

public interface LineFactory
{
    /** Given a line as read from Qi, construct a Line.
     *
     * @param buffer as read from Qi, unaltered.
     *
     * @exception QiProtocolException if the line can't be parsed.
     */
    @NotNull Line create(final @NotNull String buffer) throws QiProtocolException;
}
