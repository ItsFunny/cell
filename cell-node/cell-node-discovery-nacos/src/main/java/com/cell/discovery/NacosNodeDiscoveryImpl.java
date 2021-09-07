package com.cell.discovery;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Service;
import com.cell.annotations.AutoPlugin;
import com.cell.config.AbstractInitOnce;
import com.cell.config.ConfigFactory;
import com.cell.context.InitCTX;
import com.cell.dispatcher.IHttpCommandDispatcher;
import com.cell.exception.CellDiscoveryException;
import com.cell.exceptions.ProgramaException;
import com.cell.log.LOG;
import com.cell.model.Instance;
import com.cell.models.Module;
import com.cell.service.INodeDiscovery;

import java.util.List;
import java.util.Map;
import java.util.Properties;

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
    private ConfigService configService;

    @Override
    public List<Instance> getServerInstanceList()
    {
        return null;
    }

    @Override
    public void registerServerInstance(Instance instance)
    {
        com.alibaba.nacos.api.naming.pojo.Instance nacosInstance = new com.alibaba.nacos.api.naming.pojo.Instance();
        nacosInstance.setIp(instance.getIp());
        nacosInstance.setPort(instance.getPort());
        nacosInstance.setHealthy(true);
        nacosInstance.setEnabled(true);
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
