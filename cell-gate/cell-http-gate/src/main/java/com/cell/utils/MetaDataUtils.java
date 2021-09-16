package com.cell.utils;

import com.cell.discovery.ServiceDiscovery;
import com.cell.log.LOG;
import com.cell.model.Instance;
import com.cell.model.ServerCmdMetaInfo;
import com.cell.model.ServerMetaInfo;
import com.cell.models.Couple;
import com.cell.models.Module;
import com.cell.services.IStatContextService;
import com.cell.transport.model.ServerMetaData;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-12 18:36
 */
public class MetaDataUtils
{

    public static Couple<ServerMetaInfo, ServerMetaData> fromInstance(Instance inst)
    {
        final ServerMetaInfo info = new ServerMetaInfo();
        info.setIp(inst.getIp());
        info.setPort(Short.valueOf(String.valueOf(inst.getPort())));
        info.setServiceName(inst.getServiceName());
        info.setHealthy(inst.isHealthy());
        info.setEnable(inst.isEnable());
        ServerMetaData metaData = ServerMetaData.fromMetaData(inst.getMetaData());
        return new Couple<>(info, metaData);
    }

    public static String[] getHttpLabels(String method, String uri, IStatContextService httpGateContextService)
    {
        String module = MetaDataUtils.getModuleByUri(method, uri);
        return new String[]{httpGateContextService.getNodeName(), httpGateContextService.getHostName(), httpGateContextService.getClusterName(), uri, module};
    }

    public static String getModuleByUri(String method, String uri)
    {
        String module = "UNKNOW";
        try
        {
            ServerCmdMetaInfo info = ServiceDiscovery.getInstance().choseServer(method, uri);
            if (info != null)
            {
                module = info.getModule();
            }
        } catch (Exception e)
        {
            LOG.warning(Module.HTTP_GATEWAY, e, "根据uri查找module失败:{}", uri);
        }
        return module;
    }

}
