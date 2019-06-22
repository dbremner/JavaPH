package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.NotNull;

public final class QiLineFactory implements LineFactory
{
    @Override
    public @NotNull Line create(final @NotNull String buffer) throws QiProtocolException
    {
        final String verbatim = buffer;

		/*
		TODO : this code doesn't deal with all of the scenarios listed here

		ph myname return what
		-507:what:unknown field.
		500:Did not understand ph.

		:
		598:::Command not recognized.

		what:
		598:what::Command not recognized.

		what
		598:what:Command not recognized.

		ph myname return email
		102:There was 1 match to your request.
		-200:1:    email: myemail
		200:Ok.

		quit
		200:Bye!
		*/

        // Get the result code.
        // Index of first colon --> -200:
        final int colon1Index = verbatim.indexOf(':');
        if (colon1Index == -1) {
            throw new QiProtocolException(verbatim);
        }

        final int code = getCode(verbatim, colon1Index);

        // Get the index count, if there is one.
        // Index of second colon --> -200:1:
        final int colon2Index = verbatim.indexOf(':', colon1Index + 1);
        if (colon2Index == -1)
        {
            // This isn't a field:value response but rather a one line description.
            // Just record it and return.
            final String description = verbatim.substring(colon1Index + 1);
            return new QiLine(verbatim, description, code);
        }
        final int index;
        try
        {
            index = Integer.parseInt(verbatim.substring(colon1Index + 1, colon2Index));
        }
        catch (final @NotNull NumberFormatException e)
        {
            // This isn't a field:value response but rather a one line description.
            // Just record it and return.
            final String description = verbatim.substring(colon1Index + 1);
            return new QiLine(verbatim, description, code);
        }
        final String response = "";

        // This should be a field:value response.
        // Get field, value and return.
        // Index of third colon --> -200:1:    email:
        final int colon3Index = verbatim.indexOf(':', colon2Index + 1);
        if (colon3Index == -1) {
            throw new QiProtocolException(verbatim);
        }

        final String field = verbatim.substring(colon2Index + 1, colon3Index);
        final String value = verbatim.substring(colon3Index + 1);
        return new QiLine(verbatim, field, value, response, code, index);
    }

    private int getCode(final String verbatim, final int colon1Index) throws QiProtocolException
    {
        try
        {
            return Integer.parseInt(verbatim.substring(0, colon1Index));
        }
        catch (final @NotNull NumberFormatException e)
        {
            throw new QiProtocolException(verbatim);
        }
    }
}
