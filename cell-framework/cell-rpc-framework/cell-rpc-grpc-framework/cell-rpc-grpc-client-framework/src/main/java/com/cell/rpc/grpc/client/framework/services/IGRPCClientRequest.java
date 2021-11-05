package com.cell.rpc.grpc.client.framework.services;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-28 18:41
 */
public interface IGRPCClientRequest
{
    byte serializeType();

    String protocol();

    Class<?> response();
}
