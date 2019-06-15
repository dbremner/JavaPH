package com.bovilexics.javaph.qi;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.StringTokenizer;

class QiFieldFactory implements FieldFactory
{
    @Override
    @Contract(pure = true)
    public @NotNull Field create(final @NotNull String name, final @NotNull String someProperties, final @NotNull String description) throws QiProtocolException
    {
        final @NotNull ParseResult parseResult = QiFieldFactory.parseProperties(someProperties);
        return new QiField(name, parseResult.getLength(), parseResult.getProperties(), description);
    }

    /**
     * Parse a QI field property.
     *
     * @param someProperties The properties string which Qi returns in response to a
     *         "fields" command. The protocol stuff should be stripped
     *         leaving just the field description
     *  	   (e.g. "max 64 Indexed Lookup Public Default Any")
     *
     * @exception QiProtocolException in the event of an error parsing the data.
     *
     * @return properties collection
     */
    @Contract(pure = true)
    private static @NotNull ParseResult parseProperties(final @NotNull String someProperties) throws QiProtocolException
    {
        final @NotNull StringTokenizer tokenizer = new StringTokenizer(someProperties);
        final @NotNull String token = (String) tokenizer.nextElement();
        final int length;

        if (token.startsWith("max"))
        {
            final @NotNull String lengthString = (String) tokenizer.nextElement();
            try
            {
                length = Integer.valueOf(lengthString);
            }
            catch (final @NotNull NumberFormatException e)
            {
                throw new QiProtocolException("Invalid value for max length property: " + someProperties);
            }
        }
        else
        {
            length = -1;
        }

        final @NotNull ImmutableList.Builder<String> builder = new ImmutableList.Builder<>();
        // Okay, here come the properties...
        while (tokenizer.hasMoreElements())
        {
            builder.add((String) tokenizer.nextElement());
        }
        final @NotNull ImmutableList<String> list = builder.build();
        return new ParseResult(length, list);
    }

    private static final class ParseResult
    {
        private final int length;
        private final @NotNull ImmutableList<String> properties;

        ParseResult(final int length, final @NotNull ImmutableList<String> properties)
        {
            this.length = length;
            this.properties = properties;
        }

        int getLength()
        {
            return length;
        }

        @NotNull ImmutableList<String> getProperties()
        {
            return properties;
        }
    }
}
