package com.cell.plugin.develop.context;

import java.util.function.Consumer;

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
