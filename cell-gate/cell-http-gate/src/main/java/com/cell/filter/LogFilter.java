package com.cell.filter;

import com.cell.annotations.ActivePlugin;
import com.cell.log.LOG;
import com.cell.base.common.models.Module;
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
 * @Date 创建时间：2021-11-08 04:52
 */
@ActivePlugin
public class LogFilter implements GlobalFilter, Ordered
{
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        return chain.filter(exchange).onErrorResume(e -> Mono.fromRunnable(() ->
                LOG.error(Module.HTTP_GATEWAY, e, "fail")));
    }

    @Override
    public int getOrder()
    {
        return -1;
    }
}
