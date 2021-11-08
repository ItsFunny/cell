package com.cell.bee.rate.gateway.sentinel.handler;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-13 12:52
 */
public interface IGatewayBlockHandler extends BlockRequestHandler
{
    Mono<Void> denyForReason(ServerWebExchange exchange);
}
