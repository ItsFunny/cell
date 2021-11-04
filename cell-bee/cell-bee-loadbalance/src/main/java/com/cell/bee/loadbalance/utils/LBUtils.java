package com.cell.bee.loadbalance.utils;

import com.cell.bee.loadbalance.model.ServerMetaInfo;
import com.cell.model.Instance;
import com.cell.models.Couple;
import com.cell.transport.model.ServerMetaData;

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

}
