package com.cell.discovery.nacos.discovery;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.ListView;
import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.common.notify.Event;
import com.alibaba.nacos.common.notify.NotifyCenter;
import com.alibaba.nacos.common.notify.listener.Subscriber;
import com.cell.bee.loadbalance.model.ServerCmdMetaInfo;
import com.cell.config.AbstractInitOnce;
import com.cell.config.ConfigFactory;
import com.cell.context.InitCTX;
import com.cell.config.NacosConfiguration;
import com.cell.exception.CellDiscoveryException;
import com.cell.exceptions.ProgramaException;
import com.cell.log.LOG;
import com.cell.model.Instance;
import com.cell.models.Module;
import com.cell.service.INodeDiscovery;
import com.cell.http.framework.util.DiscoveryUtils;
import com.cell.utils.CollectionUtils;
import com.cell.utils.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-07 21:56
 */
// TODO ,FACTORY
public class NacosNodeDiscoveryImpl extends AbstractInitOnce implements INodeDiscovery, INacosNodeDiscovery
{
    private static NacosNodeDiscoveryImpl instance = null;

    private Map<String, List<ServerCmdMetaInfo>> serverMetas = new HashMap<>();
    private final Map<String, List<com.alibaba.nacos.api.naming.pojo.Instance>> delta = new HashMap<>();


    private NacosNodeDiscoveryImpl()
    {
    }

    public static void setupDiscovery()
    {
        instance = new NacosNodeDiscoveryImpl();
        String serverAddr = NacosConfiguration.getInstance().getServerAddr();
        InitCTX initCTX = new InitCTX();
        Map<String, Object> data = new HashMap<>();
        data.put(ConfigFactory.serverAddr, serverAddr);
        initCTX.setData(data);
        instance.initOnce(initCTX);
    }

    public static NacosNodeDiscoveryImpl getInstance()
    {
        return instance;
    }

    private NamingService namingService;
    private ConfigService configService;

    public void reigsterInstanceChangeHook(Subscriber<InstancesChangeEvent> hook)
    {

    }


    @Override
    public Map<String, List<Instance>> getServerInstanceList(String cluster)
    {
        try
        {
            ListView<String> servicesOfServer = this.namingService.getServicesOfServer(1, 10000);
            Map<String, List<com.alibaba.nacos.api.naming.pojo.Instance>> serviceMap = servicesOfServer
                    .getData()
                    .stream()
                    .collect(Collectors.toMap(k -> k, n ->
                    {
                        try
                        {
                            List<com.alibaba.nacos.api.naming.pojo.Instance> allInstances = this.namingService.getAllInstances(n);
                            if (CollectionUtils.isEmpty(allInstances))
                            {
                                allInstances = new ArrayList<>();
                            }
                            return StringUtils.isEmpty(cluster) ? allInstances : allInstances.stream().filter(p -> p.getClusterName().equalsIgnoreCase(cluster)).collect(Collectors.toList());
                        } catch (NacosException e)
                        {
                            throw new ProgramaException(e);
                        }
                    }));
            return DiscoveryUtils.convNacosMapInstanceToCellInstance(serviceMap);
        } catch (NacosException e)
        {
            throw new CellDiscoveryException(e);
        }
    }

    public List<Instance> getServiceAllInstance(String serviceName)
    {
        try
        {
            List<com.alibaba.nacos.api.naming.pojo.Instance> allInstances = this.namingService.getAllInstances(serviceName);
            return DiscoveryUtils.convNaocsInstance2CellInstance(allInstances);
        } catch (NacosException e)
        {
            throw new CellDiscoveryException(e);
        }
    }

    public List<String> getAllServices()
    {
        try
        {
            ListView<String> servicesOfServer = this.namingService.getServicesOfServer(1, 10000);
            return servicesOfServer.getData();
        } catch (NacosException e)
        {
            throw new CellDiscoveryException(e);
        }
    }

    @Override
    public void registerServerInstance(Instance instance)
    {
        com.alibaba.nacos.api.naming.pojo.Instance nacosInstance = new com.alibaba.nacos.api.naming.pojo.Instance();
        nacosInstance.setIp(instance.getIp());
        nacosInstance.setPort(instance.getPort());
        nacosInstance.setHealthy(instance.isHealthy());
        nacosInstance.setEnabled(instance.isEnable());
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

    public void registerListen(Subscriber<InstancesChangeEvent> subscriber)
    {
        NotifyCenter.registerSubscriber(subscriber);
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
