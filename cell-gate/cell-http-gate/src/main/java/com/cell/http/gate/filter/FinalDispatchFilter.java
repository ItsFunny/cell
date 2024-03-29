package com.cell.http.gate.filter;

import com.cell.base.common.constants.OrderConstants;
import com.cell.base.common.models.Module;
import com.cell.base.core.annotations.ActivePlugin;
import com.cell.http.gate.constants.GatewayConstants;
import com.cell.http.gate.wrapper.ServerMetaInfoWrapper;
import com.cell.sdk.log.LOG;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-11 08:59
 */
@ActivePlugin
public class FinalDispatchFilter implements GlobalFilter, Ordered
{

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        ServerMetaInfoWrapper wrapper = exchange.getAttribute(GatewayConstants.attributeCmdInfo);
//        ServerMetaInfo metaInfo = wrapper.getMetaInfo();
        URI uri = wrapper.getUri();
        URI mergedUrl = UriComponentsBuilder.fromUri(uri).host(wrapper.getIp()).port(wrapper.getPort())
                .replacePath(uri.getPath()).build().toUri();
        exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, mergedUrl);
        LOG.info(Module.CONFIGURATION, "转发uri:{},host:{},port:{},serviceName:{},method:{},module:{}", mergedUrl, wrapper.getIp(), wrapper.getPort(), wrapper.getServiceName(), wrapper.getMethod(), wrapper.getModule());
        return chain.filter(exchange);
    }


    @Override
    public int getOrder()
    {
        return OrderConstants.MAX_ORDER;
    }
}
