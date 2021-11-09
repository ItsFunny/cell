package com.cell.discovery;

import com.cell.base.core.concurrent.BaseDefaultEventLoop;
import com.cell.bee.loadbalance.model.ServerMetaInfo;
import com.cell.node.discovery.nacos.discovery.NacosNodeDiscoveryImpl;
import com.cell.http.gate.discovery.ServiceDiscovery;
import com.cell.node.discovery.model.Instance;
import com.cell.bee.transport.model.ServerMetaData;
import com.cell.base.core.utils.ClassUtil;
import com.cell.base.core.utils.ReflectUtil;
import com.cell.sdk.configuration.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ServiceDiscoveryTest
{
    private final String post1 = "/post1";
    private final String post2 = "/post2";

    @Before
    public void before()
    {
        Configuration.autoInitialize();
        ServiceDiscovery discovery = (ServiceDiscovery) ReflectUtil.newInstance(ServiceDiscovery.class);
        discovery.setInstance(discovery);
        discovery.initOnce(null);

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
                    .metaData(mockMetaData(post1))
                    .serviceName("serviceName").build();

            nodeDiscovery.registerServerInstance(ret1);

            Instance ret = Instance.builder()
                    .clusterName("cluster1")
                    .ip("127.0.0.2")
                    .port(8089)
                    .healthy(true)
                    .enable(true)
                    .metaData(mockMetaData(post2))
                    .serviceName("serviceName2").build();
            nodeDiscovery.registerServerInstance(ret);
        });

    }

    private Map<String, String> mockMetaData(String uri)
    {
        ServerMetaData data = new ServerMetaData();
        List<ServerMetaData.ServerMetaReactor> reactors = new ArrayList<>();
        List<ServerMetaData.ServerMetaCmd> cmds = new ArrayList<>();
        ServerMetaData.ServerMetaCmd cmd = new ServerMetaData.ServerMetaCmd();
        cmd.setProtocol(uri);
        cmds.add(cmd);
        ServerMetaData.ServerMetaReactor reactor = new ServerMetaData.ServerMetaReactor();
        reactor.setCmds(cmds);
        reactors.add(reactor);
        data.setReactors(reactors);

        return ServerMetaData.toMetaData(data);
    }

    @Test
    public void testGetInstance() throws Exception
    {
//        ServiceDiscovery discovery = ServiceDiscovery.getInstance();
//        TimeUnit.SECONDS.sleep(5);
//        Map<String, List<Instance>> currentDelta = discovery.getCurrentDelta();
//        Assert.assertEquals(currentDelta.isEmpty(), false);
//        Assert.assertEquals(currentDelta.size(), 2);
    }

    @Test
    public void testClear() throws Exception
    {
        ServiceDiscovery discovery = ServiceDiscovery.getInstance();
        TimeUnit.SECONDS.sleep(5);
        List<ServerMetaInfo> asd = (List<ServerMetaInfo>) ClassUtil.invokeMethodValue(discovery, "getServerByUri", "asd");
        Assert.assertNull(asd);
//        Map<String, List<Instance>> currentDelta = discovery.getCurrentDelta();
//        Assert.assertEquals(0, currentDelta.size());
    }

    @Test
    public void testNormalGet() throws Exception
    {
        ServiceDiscovery discovery = ServiceDiscovery.getInstance();
        TimeUnit.SECONDS.sleep(5);
        List<ServerMetaInfo> asd = (List<ServerMetaInfo>) ClassUtil.invokeMethodValue(discovery, "getServerByUri", post1);
        Assert.assertNotNull(asd);
//        Map<String, List<Instance>> currentDelta = discovery.getCurrentDelta();
//        Assert.assertEquals(0, currentDelta.size());
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