package com.cell;

import com.cell.manager.ProcessManager;
import com.cell.server.IServer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 23:19
 */
public class Root
{
    private Map<Class<? extends IServer>, IServer> servers = new HashMap<>();

    private AtomicBoolean started = new AtomicBoolean(false);

    private static final Root instance = new Root();

    public static Root getInstance()
    {
        return instance;
    }

    public IServer getServer(Class<? extends IServer> c)
    {
        return this.servers.get(c);
    }

    public void start()
    {
        if (!started.compareAndSet(false, true))
        {
            return;
        }
        for (Class<? extends IServer> aClass : servers.keySet())
        {
            IServer iServer = servers.get(aClass);
            iServer.start();
        }
    }


    public synchronized void addServer(IServer server)
    {
        ProcessManager instance = ProcessManager.getInstance();
        server.setSwitch(instance);
        instance.addProxy(server.getProxy());
        this.servers.put(server.getClass(), server);
    }
}
