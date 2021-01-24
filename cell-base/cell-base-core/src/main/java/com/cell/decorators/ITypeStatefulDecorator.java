package com.cell.decorators;

import com.cell.services.TypeFul;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-23 23:07
 */
public interface ITypeStatefulDecorator<T> extends IDataDecorator<TypeStateful<T>> ,Stateful<Long>, TypeFul<T>
{
}
