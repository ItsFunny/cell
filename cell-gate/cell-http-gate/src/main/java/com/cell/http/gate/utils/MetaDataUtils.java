package com.cell.http.gate.utils;

import com.cell.base.common.models.Module;
import com.cell.bee.loadbalance.model.ServerCmdMetaInfo;
import com.cell.bee.statistic.prometheus.services.IStatContextService;
import com.cell.http.gate.discovery.HttpGateServiceDiscovery;
import com.cell.sdk.log.LOG;

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
            ServerCmdMetaInfo info = HttpGateServiceDiscovery.getInstance().choseServer(method, uri);
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
