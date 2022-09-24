package com.cell.component.download.bsc.handler;

import com.cell.base.core.annotations.ManagerNode;
import com.cell.base.core.protocol.IContext;
import com.cell.component.download.bsc.wrapper.BSCEventWrapper;
import com.cell.component.download.common.event.ScheduleChainInfoEvent;

@ManagerNode(group = BSCPipeline.bscNode, name = "schedule")
public class DefaultScheduleFetchChainHandler extends AbstractBSCHandler
{

    @Override
    public boolean predict(IContext context)
    {
        return context instanceof ScheduleChainInfoEvent;
    }

    @Override
    protected void doExecute(IContext ctx)
    {
        ScheduleChainInfoEvent event= (ScheduleChainInfoEvent) ctx;


    }
}
