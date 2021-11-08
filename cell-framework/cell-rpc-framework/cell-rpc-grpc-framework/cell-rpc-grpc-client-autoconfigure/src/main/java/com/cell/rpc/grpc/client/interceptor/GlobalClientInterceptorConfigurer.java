package com.cell.rpc.grpc.client.interceptor;

import io.grpc.ClientInterceptor;

import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-27 18:56
 */
public interface GlobalClientInterceptorConfigurer
{
    void configureClientInterceptors(List<ClientInterceptor> interceptors);
}
