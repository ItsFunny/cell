package com.cell.discovery.services.impl;

import com.cell.bee.loadbalance.model.ServerCmdMetaInfo;
import com.cell.config.AbstractInitOnce;
import com.cell.discovery.nacos.discovery.INacosNodeDiscovery;
import com.cell.discovery.services.IDiscoveryClientService;
import com.cell.lb.ILoadBalancer;
import com.cell.model.Instance;
import com.cell.resolver.IKeyResolver;
import com.cell.resolver.impl.DefaultStringKeyResolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-05 09:48
 */
public abstract class AbstractDiscvoeryClientServiceImpl<K1, K2> extends AbstractInitOnce implements IDiscoveryClientService
{
    private ILoadBalancer loadBalancer;
    private INacosNodeDiscovery nodeDiscovery;
    private IKeyResolver<K1, K2> resolver;
    private Map<String, List<ServerCmdMetaInfo>> serverMetas = new HashMap<>();
    private final Map<String, List<Instance>> delta = new HashMap<>();
    private volatile boolean onChange = false;
    private String cluster;


    @Override
    public void setCluster(String cluster)
    {
        this.cluster = cluster;
    }

    protected abstract byte filterType();
}
