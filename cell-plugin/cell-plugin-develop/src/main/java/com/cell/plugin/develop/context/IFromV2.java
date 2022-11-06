package com.cell.plugin.develop.context;

import java.util.function.Consumer;

@Deprecated
public interface IFromV2<F>
{
    default void from(F f, Consumer<F>... consumers)
    {
        this.onFrom(f);
        for (Consumer<F> consumer : consumers)
        {
            consumer.accept(f);
        }
    }

    void onFrom(F f);

}
