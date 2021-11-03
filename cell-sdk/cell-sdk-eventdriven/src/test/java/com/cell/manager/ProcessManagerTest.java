package com.cell.manager;

import com.cell.http.framework.annotation.ProxyAnno;
import com.cell.concurrent.base.Promise;
import com.cell.event.IProcessEvent;
import com.cell.log.LOG;
import com.cell.proxy.IProxy;
import org.junit.Test;

public class ProcessManagerTest
{
    @ProxyAnno(proxyId = 1)
    public static class MockProxy implements IProxy
    {

        @Override
        public void proxy(IProcessEvent event, Promise<Object> promise)
        {
            LOG.info("收到event,{}", event);
            promise.trySuccess(123);
        }

        @Override
        public void proxy(IProcessEvent event)
        {

        }
    }


    @Test
    public void addPorxy()
    {
        ProcessManager processManager = ProcessManager.getInstance().initAndStart(false, 2);
        processManager.addProxy(new MockProxy());
        Promise<Object> promise = processManager.addEvent((byte) 1, new IProcessEvent.EmptyNotifyEvent());
        try
        {
            Object o = promise.get();
            System.out.println(o);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}