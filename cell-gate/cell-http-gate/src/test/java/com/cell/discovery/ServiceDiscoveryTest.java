package com.cell.discovery;

import com.cell.Configuration;
import com.cell.concurrent.BaseDefaultEventLoop;
import com.cell.model.Instance;
import com.cell.utils.ReflectUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ServiceDiscoveryTest
{
    @Test
    public void testGetInstance() throws Exception
    {

        Configuration.autoInitialize();
        ServiceDiscovery discovery = (ServiceDiscovery) ReflectUtil.newInstance(ServiceDiscovery.class);
        discovery.setInstance(discovery);
        discovery.onInit(null);

        new BaseDefaultEventLoop().execute(() ->
        {
            try
            {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e)
            {
            }
            NacosNodeDiscoveryImpl nodeDiscovery = NacosNodeDiscoveryImpl.getInstance();
            nodeDiscovery.registerServerInstance(mockInstance());
            Instance ret1 = Instance.builder()
                    .clusterName("cluster1")
                    .ip("127.0.0.3")
                    .port(8099)
                    .healthy(true)
                    .enable(true)
                    .metaData(new HashMap<>())
                    .serviceName("serviceName").build();

            nodeDiscovery.registerServerInstance(ret1);

            Instance ret = Instance.builder()
                    .clusterName("cluster1")
                    .ip("127.0.0.2")
                    .port(8089)
                    .healthy(true)
                    .enable(true)
                    .metaData(new HashMap<>())
                    .serviceName("serviceName2").build();
            nodeDiscovery.registerServerInstance(ret);
        });


        TimeUnit.MINUTES.sleep(10);
    }

    private Instance mockInstance()
    {
        Instance ret = Instance.builder()
                .clusterName("cluster1")
                .ip("127.0.0.1")
                .port(8088)
                .healthy(true)
                .enable(true)
                .metaData(new HashMap<>())
                .serviceName("serviceName").build();
        return ret;
    }
}