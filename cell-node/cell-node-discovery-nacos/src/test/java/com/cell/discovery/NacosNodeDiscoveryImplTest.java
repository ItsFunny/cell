package com.cell.discovery;

import com.cell.config.ConfigFactory;
import com.cell.context.InitCTX;
import com.cell.model.Instance;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class NacosNodeDiscoveryImplTest
{

    @Test
    public void registerServerInstance() throws Exception
    {
        NacosNodeDiscoveryImpl nacosNodeDiscovery = NacosNodeDiscoveryImpl.getInstance();
        nacosNodeDiscovery.registerServerInstance(mockInstance());

        Map<String, List<Instance>> serverInstanceList = nacosNodeDiscovery.getServerInstanceList();
        for (String s : serverInstanceList.keySet())
        {
            System.out.println(s);
        }
        TimeUnit.SECONDS.sleep(200);
    }

    private Instance mockInstance()
    {
        Instance ret = Instance.builder()
                .clusterName("cluster1")
                .ip("127.0.0.1")
                .port(8088)
                .metaData(new HashMap<>())
                .serviceName("serviceName").build();
        return ret;
    }
}