package com.cell.base.core.services;


import com.cell.base.common.consumers.IConsumer;
import com.cell.base.common.enums.ErrorEnums;
import lombok.Data;

import java.util.List;

@Data
public abstract class AbstractListFrom<F, T> implements IListFrom<F, T>
{
    protected List<T> items;

    @Override
    public List<T> from(List<F> f, IConsumer<T, ErrorEnums> consumer)
    {
        List<T> ret = IListFrom.super.from(f, consumer);
        this.items = ret;
        return ret;
    }
}
