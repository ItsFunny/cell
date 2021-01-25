package com.cell.decorators;

/**
 * @author Charlie
 * @When
 * @Description 表明是有状态的
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-07 17:04
 */
public interface Stateful<T>
{
    T getState();
}
