package com.cell.utils;

import com.cell.model.Instance;
import com.cell.model.ServerMetaInfo;
import com.cell.models.Couple;
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

    public static Couple<ServerMetaInfo, ServerMetaData> fromInstance(Instance inst){
        final ServerMetaInfo info = new ServerMetaInfo();
        info.setIp(inst.getIp());
        info.setPort(Short.valueOf(String.valueOf(inst.getPort())));
        info.setServiceName(inst.getServiceName());
        info.setHealthy(inst.isHealthy());
        info.setEnable(inst.isEnable());
        ServerMetaData metaData = ServerMetaData.fromMetaData(inst.getMetaData());
        return new Couple<>(info,metaData);
    }

}
