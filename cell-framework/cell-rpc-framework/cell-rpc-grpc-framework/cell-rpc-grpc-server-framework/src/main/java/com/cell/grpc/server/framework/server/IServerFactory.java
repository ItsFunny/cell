package com.cell.grpc.server.framework.server;

import io.grpc.Server;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-25 11:04
 */
public interface IServerFactory
{
    Server createServer();
}
