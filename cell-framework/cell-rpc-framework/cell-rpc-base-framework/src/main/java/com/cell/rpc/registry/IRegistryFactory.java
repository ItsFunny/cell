package com.cell.rpc.registry;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-20 22:21
 */
public interface IRegistryFactory
{
    IRPCRegistry create(String name);
}
