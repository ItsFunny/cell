package com.cell.chain;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-28 11:00
 */
public interface IChain<T>
{
    IChain<T> getNext();

    boolean active();
}
