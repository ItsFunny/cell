package com.cell.discovery;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.ListView;
import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.common.notify.Event;
import com.alibaba.nacos.common.notify.NotifyCenter;
import com.alibaba.nacos.common.notify.listener.Subscriber;
import com.cell.config.AbstractInitOnce;
import com.cell.config.ConfigFactory;
import com.cell.context.InitCTX;
import com.cell.exception.CellDiscoveryException;
import com.cell.exceptions.ProgramaException;
import com.cell.log.LOG;
import com.cell.model.Instance;
import com.cell.models.Module;
import com.cell.service.INodeDiscovery;
import com.cell.utils.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-07 21:56
 */
public class NacosNodeDiscoveryImpl extends AbstractInitOnce implements INodeDiscovery
{
    private NamingService namingService;
    private Subscriber<InstancesChangeEvent> subscriber;
    private ConfigService configService;

    public NacosNodeDiscoveryImpl(Subscriber<InstancesChangeEvent> subscriber)
    {
        if (subscriber == null)
        {
            subscriber = new DefaultInstanceLogSubscriber();
        }
        this.subscriber = subscriber;
        NotifyCenter.registerSubscriber(this.subscriber);
    }

    @Override
    public Map<String, List<Instance>> getServerInstanceList()
    {
        Map<String, List<Instance>> ret = new HashMap<>();
        try
        {
            ListView<String> servicesOfServer = this.namingService.getServicesOfServer(1, 10000);
            List<String> data = servicesOfServer.getData();
            for (String datum : data)
            {
                List<com.alibaba.nacos.api.naming.pojo.Instance> allInstances = this.namingService.getAllInstances(datum);
                if (CollectionUtils.isEmpty(allInstances))
                {
                    continue;
                }
                List<Instance> instances = allInstances.stream().map(p ->
                        Instance.builder()
                                .serviceName(p.getServiceName())
                                .port(p.getPort())
                                .ip(p.getIp())
                                .clusterName(p.getClusterName())
                                .metaData(p.getMetadata())
                                .weight((byte) p.getWeight()).build()).collect(Collectors.toList());
                ret.put(datum, instances);
            }
        } catch (NacosException e)
        {
            throw new CellDiscoveryException(e);
        }

        return ret;
    }

    @Override
    public void registerServerInstance(Instance instance)
    {
        com.alibaba.nacos.api.naming.pojo.Instance nacosInstance = new com.alibaba.nacos.api.naming.pojo.Instance();
        nacosInstance.setIp(instance.getIp());
        nacosInstance.setPort(instance.getPort());
        nacosInstance.setHealthy(false);
        nacosInstance.setEnabled(false);
        nacosInstance.setMetadata(instance.getMetaData());
        nacosInstance.setServiceName(instance.getServiceName());
        nacosInstance.setClusterName(instance.getClusterName());
        try
        {
            namingService.registerInstance(instance.getServiceName(), nacosInstance);
        } catch (NacosException e)
        {
            LOG.erroring(Module.DISCOVERY, "注册失败,{},instance={}", e.getMessage(), instance);
            throw new CellDiscoveryException(e);
        }
        LOG.info(Module.DISCOVERY, "成功注册server instance:{},{}", instance.getServiceName(), instance);
    }

    private static class DefaultInstanceLogSubscriber extends Subscriber<InstancesChangeEvent>
    {
        @Override
        public void onEvent(InstancesChangeEvent event)
        {
            LOG.info(Module.DISCOVERY, "收到event:{}", event);
        }

        @Override
        public Class<? extends Event> subscribeType()
        {
            return InstancesChangeEvent.class;
        }
    }

    public void listen()
    {
    }

    @Override
    protected void onInit(InitCTX ctx)
    {
        Map<String, Object> data = ctx.getData();
        Properties properties = new Properties();
        if (data.get(ConfigFactory.dataId) != null)
        {
            properties.put(ConfigFactory.dataId, data.get(ConfigFactory.dataId));
        }
        if (data.get(ConfigFactory.group) != null)
        {
            properties.put(ConfigFactory.group, data.get(ConfigFactory.group));
        }
        properties.put(ConfigFactory.serverAddr, data.get(ConfigFactory.serverAddr));
        try
        {
            this.namingService = NamingFactory.createNamingService(properties);
//            this.configService = NacosFactory.createConfigService(properties);
        } catch (NacosException e)
        {
            throw new ProgramaException(e);
        }
    }
}
