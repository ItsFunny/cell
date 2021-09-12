package com.cell.filter;

import com.cell.annotations.ActivePlugin;
import com.cell.constants.OrderConstants;
import com.cell.utils.GatewayUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-12 22:20
 */
@ActivePlugin
public class GatewayPostFilter implements GatewayFilter, GlobalFilter, Ordered
{

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        GatewayUtils.exitEntry(exchange);
        return null;
    }

    @Override
    public int getOrder()
    {
        return Integer.MAX_VALUE;
    }
}
