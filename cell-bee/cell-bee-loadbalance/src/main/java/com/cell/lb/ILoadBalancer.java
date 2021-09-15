package com.cell.lb;

import com.cell.model.ServerCmdMetaInfo;
import com.cell.model.ServerMetaInfo;

import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-08 05:56
 */
public interface ILoadBalancer
{
    ServerCmdMetaInfo choseServer(List<ServerCmdMetaInfo> servers, String method, String uri);
}
