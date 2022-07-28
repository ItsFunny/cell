package com.cell.node.spring.internal;

import com.cell.node.core.config.NodeConfig;
import com.cell.node.core.context.INodeContext;
import com.cell.node.core.extension.AbstractNodeExtension;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;

public class DefaultTraceExtension extends AbstractSpringNodeExtension
{

    @Override
    protected void onInit(INodeContext ctx) throws Exception
    {
        NodeConfig.getInstance().seal(ctx);
    }

    @Override
    protected void onStart(INodeContext ctx) throws Exception
    {

    }

    @Override
    protected void onReady(INodeContext ctx) throws Exception
    {

    }

    @Override
    protected void onClose(INodeContext ctx) throws Exception
    {

    }
}
