package com.cell.base.core.services;

import com.cell.base.common.consumers.IConsumer;
import com.cell.base.common.enums.ErrorEnums;

import java.util.ArrayList;
import java.util.List;

public interface IListFrom<F, T>
{
    T onFrom(F f);

    default List<T> from(List<F> f, IConsumer<T, ErrorEnums> consumer)
    {
        List<T> ret = new ArrayList<>();
        for (F f1 : f)
        {
            T t = this.onFrom(f1);
            if (null != consumer)
            {
                ErrorEnums consume = consumer.consume(t);
                if (consume == ErrorEnums.BREAK)
                {
                    break;
                }else if (consume==ErrorEnums.CONTINUE){
                    continue;
                }
            }
            ret.add(t);
        }
        return ret;
    }
}
