package com.cell.node.discovery.nacos.discovery.abs;

import com.cell.base.common.events.IEvent;
import com.cell.bee.loadbalance.model.ServerCmdMetaInfo;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-07 07:29
 */
@Data
public class Snap implements IEvent
{
    Map<String, Set<ServerCmdMetaInfo>> newProtocols;
    Map<String, Set<ServerCmdMetaInfo>> deltaAddProtocols;
    Map<String, Set<ServerCmdMetaInfo>> downProtocols;
    Map<String, Set<ServerCmdMetaInfo>> deltaDownProtocols;
    // 当前的所有 instance信息,其中key为serviceName,因为存在一个serviceName可能会有多个instance的情况
    Map<String, List<InstanceWrapper>> instances;

}
