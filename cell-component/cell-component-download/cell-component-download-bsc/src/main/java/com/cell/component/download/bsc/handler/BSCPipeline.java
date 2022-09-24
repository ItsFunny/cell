package com.cell.component.download.bsc.handler;

import com.cell.base.core.annotations.Manager;
import com.cell.bee.event.center.AbstractEventCenter;
import com.cell.component.download.common.pipeline.ProxyPipeline;
import com.cell.plugin.pipeline.manager.IReflectManager;

@Manager(name = BSCPipeline.bscNode)
public class BSCPipeline extends AbstractEventCenter
{
    private static final BSCPipeline instance = new BSCPipeline();
    public static final String bscNode = "bscDownloader";

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
