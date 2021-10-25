package com.cell.discovery;

import java.util.Collection;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-25 12:59
 */
@FunctionalInterface
public interface RPCServiceDiscovery
{
    Collection<RPCServideDefinition> findGrpcServices();
}
