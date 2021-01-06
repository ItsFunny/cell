package com.cell.services;


import com.cell.exceptions.DeserializeException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-12-10 13:14
 */
public interface IFactoryFrom<F,M>
{
    M  from(F f) throws DeserializeException;
}
