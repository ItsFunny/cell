package com.cell.discovery.nacos.base.extension;

import com.cell.context.INodeContext;
import com.cell.dispatcher.IDispatcher;
import com.cell.http.gate.extension.AbstractSpringNodeExtension;
import com.cell.proxy.IFrameworkProxy;
import com.cell.root.Root;
import com.cell.server.IServer;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-02 10:18
 */
public abstract class AbstractNacosDiscoveryExtension extends AbstractSpringNodeExtension
{

    protected abstract Class<? extends IServer> serverClz();

    @Override
    protected void onStart(INodeContext ctx) throws Exception
    {
        IServer server = Root.getInstance().getServer(this.serverClz());
        IFrameworkProxy proxy = (IFrameworkProxy) server.getProxy();
        IDispatcher dispatcher = proxy.getDispatcher();
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
