package com.cell.extension.mybatis.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cell.base.common.decorators.IDecorator;
import com.cell.base.core.services.IFactory;

import java.util.concurrent.Callable;

public interface ICellBaseService<T> extends IService<T>
{
    T getAndInsertOrUpdate(IFactory<QueryWrapper<T>>queryWrapperFactory, IFactory<T> factory, IDecorator<T>... decorators);
}
