package com.cell.filters;

import com.cell.decorators.TypeStateful;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-07 17:08
 */
//public interface ITypeFilterManager<T> extends ITypeStatefulFilter<T>
public interface ITypeFilterManager<T>
{
    void filter(TypeStateful<T> stateful, FilterLogicHandlerTrue handlerTrue);
    void registerFilter(ITypeStatefulFilter<T>... filter);
}
