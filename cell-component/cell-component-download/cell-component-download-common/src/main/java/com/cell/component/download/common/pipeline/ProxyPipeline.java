package com.cell.component.download.common.pipeline;

import com.cell.base.core.annotations.Manager;
import com.cell.bee.event.center.AbstractEventCenter;
import com.cell.plugin.pipeline.manager.IReflectManager;

@Manager(name = ProxyPipeline.proxyNode)
public class ProxyPipeline extends AbstractEventCenter
{
    private static final ProxyPipeline instance = new ProxyPipeline();
    public static final String proxyNode = "proxy";

    public static ProxyPipeline getInstance(){
        return instance;
    }
    @Override
    protected void afterInvoke()
    {

    }

    @Override
    public IReflectManager createOrDefault()
    {
        return instance;
    }

}
