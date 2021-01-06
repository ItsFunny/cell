package com.cell.services;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-12-10 13:16
 */
public interface IFactoryBase<F,M,T> extends  IFactoryFrom<F,M> ,IFactortyTO<M,T>
{
}
