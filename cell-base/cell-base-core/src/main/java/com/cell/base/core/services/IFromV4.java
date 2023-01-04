package com.cell.base.core.services;

public interface IFromV4<F>
{
    default void from(F f, Runnable consumer)
    {
        this.onFrom(f);
        if (consumer != null)
        {
            consumer.run();
        }
    }

    void onFrom(F f);
}
