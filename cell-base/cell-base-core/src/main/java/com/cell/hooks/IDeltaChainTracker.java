package com.cell.hooks;


/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-28 11:30
 */
public interface IDeltaChainTracker<T, R>
{
    R trackBegin(T t);

    void trackEnd(R r);

    void exceptionCaught(Exception e);

    IDeltaChainTracker<T, R> prev();

    IDeltaChainTracker<T, R> next();


    boolean active();
}
