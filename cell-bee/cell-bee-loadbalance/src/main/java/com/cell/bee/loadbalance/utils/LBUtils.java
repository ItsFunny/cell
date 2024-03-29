package com.cell.bee.loadbalance.utils;

import com.cell.bee.loadbalance.model.ServerMetaInfo;
import com.cell.bee.transport.model.ServerMetaData;
import com.cell.node.discovery.model.Instance;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-05 04:51
 */
public class LBUtils
{
    public static ServerMetaInfo fromInstance(Instance inst)
    {
        final ServerMetaInfo info = new ServerMetaInfo();
        info.setVisualAddress(inst.getIp());
        info.setVisualPort(Short.valueOf(String.valueOf(inst.getPort())));
        info.setServiceName(inst.getServiceName());
        info.setHealthy(inst.isHealthy());
        info.setEnable(inst.isEnable());
        ServerMetaData metaData = ServerMetaData.fromMetaData(inst.getMetaData());
        info.setMetaData(metaData);
        return info;
    }

}
