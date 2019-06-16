package com.bovilexics.javaph.ui;

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

    public int getIndex()
    {
        return index;
    }

    public @NotNull String getLabel()
    {
        return label;
    }

    public @NotNull String getFilename()
    {
        return filename;
    }
}
