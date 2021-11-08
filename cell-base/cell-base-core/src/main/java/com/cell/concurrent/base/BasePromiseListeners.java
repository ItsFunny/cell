package com.cell.concurrent.base;

import java.util.Arrays;

public class BasePromiseListeners
{
    private GenericFutureListener<? extends Future<?>>[] listeners;
    private int size;

    @SuppressWarnings("unchecked")
    BasePromiseListeners(GenericFutureListener<? extends Future<?>> first,
                         GenericFutureListener<? extends Future<?>> second)
    {
        listeners = new GenericFutureListener[2];
        listeners[0] = first;
        listeners[1] = second;
        size = 2;
    }

    public void add(GenericFutureListener<? extends Future<?>> l)
    {
        GenericFutureListener<? extends Future<?>>[] listeners = this.listeners;
        final int size = this.size;
        if (size == listeners.length)
        {
            this.listeners = listeners = Arrays.copyOf(listeners, size << 1);
        }
        listeners[size] = l;
        this.size = size + 1;

    }

    public void remove(GenericFutureListener<? extends Future<?>> l)
    {
        final GenericFutureListener<? extends Future<?>>[] listeners = this.listeners;
        int size = this.size;
        for (int i = 0; i < size; i++)
        {
            if (listeners[i] == l)
            {
                int listenersToMove = size - i - 1;
                if (listenersToMove > 0)
                {
                    System.arraycopy(listeners, i + 1, listeners, i, listenersToMove);
                }
                listeners[--size] = null;
                this.size = size;
                return;
            }
        }
    }

    public GenericFutureListener<? extends Future<?>>[] listeners()
    {
        return listeners;
    }

    public int size()
    {
        return size;
    }
}

