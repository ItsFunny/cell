package com.cell.discovery;

import com.cell.config.ConfigFactory;
import com.cell.context.InitCTX;
import com.cell.model.Instance;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class NacosNodeDiscoveryImplTest
{

    @Test
    public void registerServerInstance() throws Exception
    {
        NacosNodeDiscoveryImpl nacosNodeDiscovery = new NacosNodeDiscoveryImpl();
        InitCTX ctx = new InitCTX();
        Map<String, Object> data = new HashMap<>();
        data.put(ConfigFactory.serverAddr, "127.0.0.1:8848");
        ctx.setData(data);
        nacosNodeDiscovery.initOnce(ctx);

        nacosNodeDiscovery.registerServerInstance(mockInstance());
    }

    private Instance mockInstance()
    {
        Instance ret = Instance.builder()
                .appName("app")
                .clusterName("cluster1")
                .ip("127.0.0.1")
                .port(8088)
                .metaData(new HashMap<>())
                .serviceName("serviceName").build();
        return ret;
    }
}