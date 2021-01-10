package com.cell.services;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-10 22:18
 */
public interface IHandler<T>
{
    void handle(T t);
}
