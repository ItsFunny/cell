package com.cell.manager;

import com.cell.concurrent.BaseDefaultEventLoopGroup;
import com.cell.concurrent.base.BasePromise;
import com.cell.concurrent.base.EventExecutor;
import com.cell.concurrent.base.EventLoopGroup;
import com.cell.concurrent.base.Promise;
import com.cell.event.IProcessEvent;
import com.cell.event.ProcessEvent;
import com.cell.exceptions.ProgramaException;
import com.cell.proxy.IProxy;
import com.cell.rpc.grpc.client.framework.annotation.ProxyAnno;
import com.cell.utils.ClassUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 16:16
 */
@Data
public class ProcessManager
{
    private Logger logger = LoggerFactory.getLogger(ProcessManager.class);

    private static final ProcessManager instance = new ProcessManager();

    public static ProcessManager getInstance()
    {
        return instance;
    }

    private boolean dynamicAddProxy = false;
    private List<IProxy> proxies = new ArrayList<>();
    private ArrayBlockingQueue<ProcessEvent> epoll = new ArrayBlockingQueue<>(128);
    private AtomicBoolean started = new AtomicBoolean(false);
    private EventLoopGroup eventLoopGroup;

    private ProcessManager() {}

    public ProcessManager initAndStart(boolean dynamicAddProxy, int threadCount)
    {
        if (!this.started.compareAndSet(false, true))
        {
            return this;
        }
        this.dynamicAddProxy = dynamicAddProxy;
        this.eventLoopGroup = new BaseDefaultEventLoopGroup(threadCount);
        this.runThread.start();
        return this;
    }

    private Thread runThread = new Thread(() ->
    {
        while (!Thread.currentThread().isInterrupted())
        {
            try
            {
                ProcessEvent event = epoll.take();
                this.safeExecute(event);
            } catch (Exception e)
            {
                // TODO
                logger.error("获取event失败", e);
            }
        }
    });

    private void safeExecute(ProcessEvent event)
    {
        byte proxyId = event.getProxyId();
        if (proxyId >= proxies.size())
        {
            event.getPromise().tryFailure(new ProgramaException("no such proxy"));
            // TODO
            return;
        }
        IProxy proxy = proxies.get(proxyId);
        this.getEventExecutor().execute(() ->
                {
                    try
                    {
                        proxy.proxy(event.getInternalProcessEvent(), event.getPromise());
                    } catch (Exception e)
                    {
                        Promise<Object> promise = event.getPromise();
                        if (!promise.isDone())
                        {
                            promise.tryFailure(e);
                        }
                    }
                }
        );
    }


    public Promise<Object> addEvent(byte proxyId, IProcessEvent event)
    {
        Promise<Object> promise = new BasePromise<>(this.getEventExecutor());
        ProcessEvent e = new ProcessEvent();
        e.setPromise(promise);
        e.setProxyId(proxyId);
        e.setInternalProcessEvent(event);
        this.epoll.add(e);
        return promise;
    }

    public void addProxy(IProxy proxy)
    {
        ProxyAnno anno = (ProxyAnno) ClassUtil.mustGetAnnotation(proxy.getClass(), ProxyAnno.class);
        byte b = anno.proxyId();
        if (b > this.proxies.size())
        {
            int delta = b - this.proxies.size() + 1;
            for (int i = 0; i < delta; i++)
            {
                this.proxies.add(null);
            }
        }
        // TODO
        this.proxies.set(b, proxy);
    }

    private List<IProxy> newList()
    {
        List<IProxy> ret = null;
        if (this.dynamicAddProxy)
        {
            ret = new CopyOnWriteArrayList<>();
        } else
        {
            ret = new ArrayList<>();
        }
        return ret;
    }

    public EventExecutor getEventExecutor()
    {
        return this.getEventLoopGroup().next();
    }


}
