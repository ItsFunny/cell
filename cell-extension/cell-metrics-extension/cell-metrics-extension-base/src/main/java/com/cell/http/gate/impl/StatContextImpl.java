package com.cell.http.gate.impl;

import com.cell.constants.CommandLineConstants;
import com.cell.node.core.context.INodeContext;
import com.cell.services.IStatContextService;

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
        return this.nodeContext.getNodeName();
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
