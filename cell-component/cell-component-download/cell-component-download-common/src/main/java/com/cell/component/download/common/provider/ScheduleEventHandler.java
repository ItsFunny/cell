package com.cell.component.download.common.provider;

import com.cell.base.core.annotations.ManagerNode;
import com.cell.component.download.common.pipeline.ProxyPipeline;
import com.cell.component.download.common.provider.event.ProxyWrapperEvent;

import java.util.List;

@ManagerNode(group = ProxyPipeline.proxyNode, name = "schedule")
public class ScheduleEventHandler extends AbstractProxyHandler
{

    @Override
    protected void doExecute(ProxyWrapperEvent event)
    {
        List<IBlockChainProvider> providers = event.getProvider().getProviders();
        providers.forEach(p -> p.dispatch(event));
    }
}