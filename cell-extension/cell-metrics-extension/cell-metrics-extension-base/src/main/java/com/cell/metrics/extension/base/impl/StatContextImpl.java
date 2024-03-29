package com.cell.metrics.extension.base.impl;

import com.cell.base.common.constants.CommandLineConstants;
import com.cell.bee.statistic.prometheus.services.IStatContextService;
import com.cell.node.core.context.INodeContext;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-15 21:20
 */
public class StatContextImpl implements IStatContextService
{
    private INodeContext nodeContext;

    public StatContextImpl(INodeContext nodeContext)
    {
        super();
        this.nodeContext = nodeContext;
    }

    @Override
    public INodeContext getNodeContext()
    {
        return this.nodeContext;
    }

    @Override
    public String getNodeName()
    {
        return this.nodeContext.getNodeId();
    }

    @Override
    public String getClusterName()
    {
        return this.nodeContext.getCommandLine().getOptionValue(CommandLineConstants.CLUSTER, CommandLineConstants.DEFAULT_CLSUTER_VALUE);
    }

    @Override
    public String getHostName()
    {
        return this.nodeContext.getIp();
    }
}
