package com.cell.services;


import com.cell.base.common.exceptions.SerializeException;

/**
 * @author Charlie
 * @When
 * @Description 只用于工厂类, 将实体对象转化为XXX
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-12-10 13:05
 */
public interface IFactortyTO<M, T>
{
    T to(M f) throws SerializeException;
}
