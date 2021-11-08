package com.cell.server.abs;

import com.cell.base.core.config.AbstractInitOnce;
import com.cell.concurrent.base.Promise;
import com.cell.event.IFrameworkEvent;
import com.cell.event.IProcessEvent;
import com.cell.manager.ProcessManager;
import com.cell.protocol.IServerRequest;
import com.cell.protocol.IServerResponse;
import com.cell.proxy.IFrameworkProxy;
import com.cell.proxy.IProxy;
import com.cell.rpc.grpc.client.framework.annotation.ProxyAnno;
import com.cell.server.IServer;
import com.cell.utils.ClassUtil;
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
    private short port = 8081;
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
