package com.cell.extension.mybatis.helper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cell.base.common.decorators.IDecorator;
import com.cell.base.core.services.IFactory;
import com.cell.extension.mybatis.service.ICellBaseService;

public class DaoHelper
{
    public static <T> T getAndInsertOrUpdate(IService<T> service, IFactory<QueryWrapper<T>> queryWrapperFactory, IFactory<T> factory, IDecorator<T>... decorators)
    {
        QueryWrapper<T> wrapper = queryWrapperFactory.create();
        T one = service.getOne(wrapper);
        if (one == null)
        {
            one = factory.create();
            for (IDecorator<T> decorator : decorators)
            {
                one = decorator.decorate(one);
            }
            service.save(one);
        } else
        {
            for (IDecorator<T> decorator : decorators)
            {
                one = decorator.decorate(one);
            }
            service.updateById(one);
        }
        return one;
    }
}
