package com.cell.base.core.services;

import java.util.function.Consumer;

@Deprecated
public interface IFromV3<F>
{
    default void from(F f, Consumer<IFromV3> consumer)
    {
        this.onFrom(f);
        if (consumer != null)
        {
            consumer.accept(this);
        }
    }

    void onFrom(F f);
}
