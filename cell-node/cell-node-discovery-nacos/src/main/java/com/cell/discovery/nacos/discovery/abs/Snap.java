package com.cell.discovery.nacos.discovery.abs;

import com.cell.bee.loadbalance.model.ServerCmdMetaInfo;
import lombok.Data;

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
public class Snap
{
    Map<String, Set<ServerCmdMetaInfo>> newProtocols;
    Map<String, Set<ServerCmdMetaInfo>> deltaAddProtocols;
    Map<String, Set<ServerCmdMetaInfo>> downProtocols;
    Map<String, Set<ServerCmdMetaInfo>> deltaDownProtocols;

}