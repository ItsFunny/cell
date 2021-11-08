package com.cell.base.common.decorators;

import com.cell.base.common.comparators.CompareSatisfiedFunc;
import com.cell.base.common.services.IManager;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-23 23:03
 */
public interface ITypeDecoratorManager<T, TYPE, GROUP> extends IManager<TYPE, GROUP>
{
    TypeStateful<T> decorate(TypeStateful<T> t, CompareSatisfiedFunc<T> compareFunc);

    void registerDecorator(ITypeStatefulDecorator<T>... filter);
}

