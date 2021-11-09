package com.cell.http.gate.filter;

import com.cell.base.common.models.Module;
import com.cell.base.core.annotations.ActivePlugin;
import com.cell.bee.loadbalance.model.ServerCmdMetaInfo;
import com.cell.gate.common.utils.GatewayUtils;
import com.cell.http.gate.constants.GatewayConstants;
import com.cell.http.gate.discovery.HttpGateServiceDiscovery;
import com.cell.http.gate.wrapper.ServerMetaInfoWrapper;
import com.cell.sdk.log.LOG;
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
        HttpGateServiceDiscovery serviceDiscovery = HttpGateServiceDiscovery.getInstance();
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        ServerCmdMetaInfo metaInfo = serviceDiscovery.choseServer(request.getMethod().name().toLowerCase(), uri.getPath());
        // FIXME ,定制化信息
        if (metaInfo == null)
        {
            return GatewayUtils.fastFinish(exchange, "command not exists");
        }
        ServerMetaInfoWrapper wrapper = new ServerMetaInfoWrapper();
        wrapper.fillWithMeta(metaInfo);
        wrapper.setUri(uri);
        wrapper.setMethod(request.getMethod().name());
        exchange.getAttributes().put(GatewayConstants.attributeCmdInfo, wrapper);
        return chain.filter(exchange).onErrorResume(e ->
                Mono.fromRunnable(() ->
                {
                    LOG.error(Module.HTTP_GATEWAY, e, "fail");
                    // TODO ,定制化error
                    GatewayUtils.fastFinish(exchange, e.getMessage());
                }));
    }

    @Override
    public int getOrder()
    {
        return 0;
    }
}
