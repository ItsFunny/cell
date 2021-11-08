package com.cell.base.core.filters;

import com.cell.base.core.decorators.TypeStateful;
import com.cell.base.core.manager.IManager;
import com.cell.base.core.comparators.CompareSatisfiedFunc;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-07 17:08
 */
//public interface ITypeFilterManager<T> extends ITypeStatefulFilter<T>
public interface ITypeFilterManager<T, TYPE, GROUP> extends IManager<TYPE, GROUP>
{
    void filter(TypeStateful<T> stateful, CompareSatisfiedFunc<T> compareSatisfiedFunc, FilterLogicHandlerTrue handlerTrue);

    void registerFilter(ITypeStatefulFilter<T>... filter);
    // 获取id ,可以是模块id

}
