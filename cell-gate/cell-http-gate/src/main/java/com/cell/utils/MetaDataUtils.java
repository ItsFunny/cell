package com.cell.utils;

import com.cell.bee.loadbalance.model.ServerCmdMetaInfo;
import com.cell.bee.loadbalance.model.ServerMetaInfo;
import com.cell.bee.loadbalance.utils.LBUtils;
import com.cell.discovery.ServiceDiscovery;
import com.cell.log.LOG;
import com.cell.model.Instance;
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
