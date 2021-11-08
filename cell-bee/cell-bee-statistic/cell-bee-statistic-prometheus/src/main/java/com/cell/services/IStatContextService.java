package com.cell.services;


import com.cell.node.core.context.INodeContext;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-15 21:12
 */
public interface IStatContextService
{
    INodeContext getNodeContext();

    String getNodeName();

    String getClusterName();

    String getHostName();
}
