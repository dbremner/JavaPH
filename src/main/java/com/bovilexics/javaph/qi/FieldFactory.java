package com.bovilexics.javaph.qi;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface FieldFactory
{
    /**
     * TODO fill these in
     * @param name the name
     * @param someProperties some properties
     * @param description a description
     * @throws IllegalArgumentException for empty strings.
     * @return a Field
     */
    @Contract(pure = true)
    @NotNull Field create(@NotNull String name, @NotNull String someProperties, @NotNull String description);
}
