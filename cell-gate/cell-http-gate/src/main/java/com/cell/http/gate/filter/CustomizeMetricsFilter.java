package com.cell.http.gate.filter;

import com.cell.base.core.annotations.ActivePlugin;
import com.cell.context.MetricsContext;
import com.cell.manager.MetricsManager;
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
 * @Date 创建时间：2021-09-21 14:14
 */
@ActivePlugin
public class CustomizeMetricsFilter implements GlobalFilter, Ordered
{
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        MetricsContext context = new MetricsContext();
        context.setExchange(exchange);
        return chain.filter(exchange)
                .then(MetricsManager.getInstance().execute(MetricsManager.postFilterHook, context));
    }


    @Override
    public int getOrder()
    {
        return 1;
    }
}
