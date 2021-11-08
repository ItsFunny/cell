package com.cell.base.core.filters;

import com.cell.base.core.decorators.TypeStateful;
import com.cell.base.core.services.TypeFul;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-07 17:15
 */
public interface ITypeStatefulFilter<T> extends IFilter<TypeStateful<T>>, TypeFul<T>
{

}
