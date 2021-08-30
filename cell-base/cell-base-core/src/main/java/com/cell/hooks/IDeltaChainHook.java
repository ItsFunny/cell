package com.cell.hooks;

import com.cell.manager.IManagerNode;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-28 11:30
 */
public interface IDeltaChainHook<T, R>
{
    R hook(T t);

    void exceptionCaught(Exception e);
    IDeltaChainHook<T, R> next();




    boolean active();
}
