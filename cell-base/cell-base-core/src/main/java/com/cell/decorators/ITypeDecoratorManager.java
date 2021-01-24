package com.cell.decorators;

import com.cell.comparators.CompareSatisfiedFunc;
import com.cell.manager.IManager;

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
    TypeStateful<T> decorate(TypeStateful<T> t, CompareSatisfiedFunc<T> compareFunc );

    void registerDecorator(ITypeStatefulDecorator<T>... filter);
}

