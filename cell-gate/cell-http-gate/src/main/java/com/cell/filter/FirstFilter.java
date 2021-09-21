package com.cell.filter;

import com.cell.annotations.ActivePlugin;
import com.cell.constants.GatewayConstants;
import com.cell.constants.OrderConstants;
import com.cell.discovery.ServiceDiscovery;
import com.cell.model.ServerMetaInfo;
import com.cell.utils.GatewayUtils;
import com.cell.wrapper.ServerMetaInfoWrapper;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-21 15:27
 */
@ActivePlugin
public class FirstFilter implements GlobalFilter, Ordered
{

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        ServiceDiscovery serviceDiscovery = ServiceDiscovery.getInstance();
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        ServerMetaInfo metaInfo = serviceDiscovery.choseServer(request.getMethod().name().toLowerCase(), uri.getPath());
        ServerMetaInfoWrapper wrapper = new ServerMetaInfoWrapper();
        wrapper.setMetaInfo(metaInfo);
        wrapper.setUri(uri);
        wrapper.setMethod(request.getMethod().name());
        // FIXME ,定制化信息
        if (metaInfo == null)
        {
            return GatewayUtils.fastFinish(exchange, "command not exists");
        }
        exchange.getAttributes().put(GatewayConstants.attributeCmdInfo, wrapper);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder()
    {
        return 0;
    }
}
