package com.bovilexics.javaph.ui;

import org.jetbrains.annotations.NotNull;

import javax.swing.ListModel;
import java.util.AbstractList;

public enum ListModels
{
    ;

    public static @NotNull <E> Iterable<E> asIterable(final @NotNull ListModel<E> model)
    {
        return new ListModelList<>(model);
    }

    private static final class ListModelList<E> extends AbstractList<E>
    {
        private final @NotNull ListModel<E> model;

        ListModelList(final @NotNull ListModel<E> model)
        {
            this.model = model;
        }

        @Override
        public E get(final int index)
        {
            return model.getElementAt(index);
        }

        @Override
        public int size()
        {
            return model.getSize();
        }
    }
}