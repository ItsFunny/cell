package com.cell.filters;

import com.cell.decorators.TypeStateful;
import com.cell.services.TypeFul;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-07 17:08
 */
//public interface ITypeFilterManager<T> extends ITypeStatefulFilter<T>
public interface ITypeFilterManager<T, TYPE> extends TypeFul<TYPE>
{
    void filter(TypeStateful<T> stateful, FilterLogicHandlerTrue handlerTrue);

    void registerFilter(ITypeStatefulFilter<T>... filter);
    // 获取id ,可以是模块id

}
