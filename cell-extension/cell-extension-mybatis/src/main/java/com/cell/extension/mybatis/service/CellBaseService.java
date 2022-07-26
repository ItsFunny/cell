package com.cell.extension.mybatis.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cell.base.common.decorators.IDecorator;
import com.cell.base.core.services.IFactory;
import com.cell.extension.mybatis.helper.DaoHelper;


public class CellBaseService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements ICellBaseService<T>
{
    @Override
    public T getAndInsertOrUpdate(IFactory<QueryWrapper<T>> queryWrapperFactory, IFactory<T> factory, IDecorator<T>... decorators)
    {
        return DaoHelper.getAndInsertOrUpdate(this, queryWrapperFactory, factory, decorators);
    }
}
