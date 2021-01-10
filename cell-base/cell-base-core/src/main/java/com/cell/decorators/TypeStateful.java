package com.cell.decorators;


import com.cell.services.TypeFul;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-07 17:04
 */
public interface TypeStateful<T> extends Stateful<Long>, TypeFul<T>
{
}
