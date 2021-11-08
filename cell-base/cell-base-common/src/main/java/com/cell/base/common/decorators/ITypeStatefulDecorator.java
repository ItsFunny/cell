package com.cell.base.common.decorators;

import com.cell.base.common.enums.BeeEnums;
import com.cell.base.common.services.TypeFul;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-23 23:07
 */
public interface ITypeStatefulDecorator<T> extends IDecorator<TypeStateful<T>>, Stateful<Long>, TypeFul<T>
{
    BeeEnums getBee();
}
