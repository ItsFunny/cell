package com.cell.base.framework.server.abs;

import com.cell.base.common.context.AbstractInitOnce;
import com.cell.base.core.concurrent.base.Promise;
import com.cell.base.core.protocol.IServerRequest;
import com.cell.base.core.protocol.IServerResponse;
import com.cell.base.core.utils.ClassUtil;
import com.cell.base.framework.event.IFrameworkEvent;
import com.cell.base.framework.proxy.IFrameworkProxy;
import com.cell.base.framework.server.IServer;
import com.cell.event.IProcessEvent;
import com.cell.manager.ProcessManager;
import com.cell.proxy.IProxy;
import com.cell.rpc.grpc.client.framework.annotation.ProxyAnno;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 23:34
 */
@Data
public abstract class AbstractServer extends AbstractInitOnce implements IServer
{
    private short port = 8080;
    private ProxyAnno anno;
    private IFrameworkProxy proxy;
    private ProcessManager manager;


    public AbstractServer(IFrameworkProxy proxy)
    {
        this.proxy = proxy;
        this.anno = (ProxyAnno) ClassUtil.mustGetAnnotation(proxy.getClass(), ProxyAnno.class);
    }

    public Promise<Object> fire(IProcessEvent event)
    {
        return this.manager.addEvent(this.anno.proxyId(), event);
    }

    @Override
    public IProxy getProxy()
    {
        return this.proxy;
    }

    @Override
    public void start()
    {
        this.onStart();
    }

    @Override
    public void serve(IServerRequest request, IServerResponse response)
    {
        IFrameworkEvent.DefaultRequestResponseEvent event = new IFrameworkEvent.DefaultRequestResponseEvent(request, response);
        this.proxy.proxy(event);
    }

    @Override
    public void setSwitch(ProcessManager manager)
    {
        this.manager = manager;
    }

    @Override
    public void shutdown()
    {
        this.onShutdown();
    }

    protected abstract void onStart();

    protected abstract void onShutdown();


}
