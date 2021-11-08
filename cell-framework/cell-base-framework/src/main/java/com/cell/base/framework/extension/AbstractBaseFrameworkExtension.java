package com.cell.base.framework.extension;

import com.cell.base.framework.dispatcher.IDispatcher;
import com.cell.base.framework.server.IServer;
import com.cell.node.core.context.INodeContext;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;
import com.cell.proxy.IProxy;

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
