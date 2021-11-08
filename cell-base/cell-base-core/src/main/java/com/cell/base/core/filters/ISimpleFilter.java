package com.cell.base.core.filters;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-07 11:14
 */
public interface ISimpleFilter<T>
{
    boolean filter(T t);
}
