package com.cell.filters;

import com.cell.comparators.CompareSatisfiedFunc;
import com.cell.decorators.TypeStateful;
import com.cell.manager.IManager;

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
