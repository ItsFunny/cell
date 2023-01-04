package com.cell.component.mybatis.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cell.base.common.decorators.IDecorator;
import com.cell.base.core.services.IFactory;
import com.cell.component.mybatis.helper.DaoHelper;
import com.cell.node.core.context.CellContext;
import com.cell.node.core.exception.BusinessException;
import com.cell.node.core.exception.ErrorConstant;
import com.cell.node.core.exception.WrapContextException;

import java.io.Serializable;
import java.util.Optional;

public interface ICellBaseService<T> extends IService<T> {
    default T getAndInsertOrUpdate(IFactory<QueryWrapper<T>> queryWrapperFactory, IFactory<T> factory, IDecorator<T>... decorators) {
        return DaoHelper.getAndInsertOrUpdate(this, queryWrapperFactory, factory, decorators);
    }

    default T mustGetById(CellContext context, Serializable id) {
        T ret = this.getById(id);
        if (ret == null) {
            throw new WrapContextException(context, new BusinessException(ErrorConstant.RECORD_NOT_EXISTS));
        }
        return ret;
    }

    default Optional<T> getAndUpdate(IFactory<QueryWrapper<T>> queryWrapperFactory, IDecorator<T>... decorators) {
        QueryWrapper<T> wrapper = queryWrapperFactory.create();
        T one = this.getOne(wrapper);
        if (one == null) {
            return Optional.empty();
        }
        for (IDecorator<T> decorator : decorators) {
            T ret = decorator.decorate(one);
            if (ret == null) {
                return Optional.of(one);
            }
            one = ret;
        }
        this.updateById(one);
        return Optional.of(one);
    }
}
