package com.cell.base.core.comparators;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-24 08:14
 */
public interface CompareSatisfiedFunc<T>
{
    boolean satisfied(T t);
}
