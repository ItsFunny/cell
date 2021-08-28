package com.cell.hooks;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-28 14:02
 */
public interface ITwoHook<T,R>
{
    // r 为返回值
    R hook(T t);
}
