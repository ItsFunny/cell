package com.cell.extension;

import com.cell.context.INodeContext;
import com.cell.dispatcher.IDispatcher;
import com.cell.proxy.IProxy;
import com.cell.server.IServer;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-26 05:04
 */
public abstract class AbstractBaseFrameworkExtension extends AbstractSpringNodeExtension
{
    protected IServer server;
    protected IDispatcher dispatcher;
    protected IProxy proxy;

    protected abstract IDispatcher newDispatcher();

    protected abstract IProxy newProxy();

    protected abstract IServer newServer();

    @Override
    protected void onInit(INodeContext ctx) throws Exception
    {
        this.dispatcher = this.newDispatcher();
        this.proxy = this.newProxy();
        this.server = this.newServer();
    }
}
