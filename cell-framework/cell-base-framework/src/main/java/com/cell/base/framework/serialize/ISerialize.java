package com.cell.base.framework.serialize;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021/10/29 13:49
 */
public interface ISerialize<T>
{
    byte[] toBytes(T t);

    T fromBytes(byte[] data);

    boolean support(byte b);
}
