package com.cell.services;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-10-06 13:59
 */
public interface IDataUnserialize<T>
{
    T unserizlize(byte[] data);


}
