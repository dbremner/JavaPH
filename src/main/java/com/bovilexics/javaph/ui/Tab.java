package com.bovilexics.javaph.ui;

import com.bovilexics.javaph.JavaPHConstants;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum Tab
{
    ResultText(0, "Text Results", "javaph-result.txt"),
    ResultTable(1, "Table Results", "javaph-result.csv"),
    SystemLog(2, "System Log", "javaph-log.txt");

    private final int index;

    private final @NotNull String label;

    private final @NotNull String filename;

    Tab(final int index, final @NotNull String label, final @NotNull String filename)
    {
        this.index = index;
        this.label = label;
        this.filename = filename;
    }

    private int getIndex()
    {
        return index;
    }

    @Contract(pure = true)
    public @NotNull String getLabel()
    {
        return label;
    }

    @Contract(pure = true)
    public @NotNull String getFilename()
    {
        return filename;
    }

    public static @NotNull Tab fromIndex(final int index)
    {
        for(final @NotNull Tab value : values())
        {
            if (value.getIndex() == index)
            {
                return value;
            }
        }
        throw new IllegalArgumentException(JavaPHConstants.ARGUMENT_IS_OUT_OF_RANGE);
    }
}
