package com.cell.filter;

import com.cell.annotations.ActivePlugin;
import com.cell.bee.loadbalance.model.ServerMetaInfo;
import com.cell.constants.GatewayConstants;
import com.cell.constants.OrderConstants;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.cell.wrapper.ServerMetaInfoWrapper;
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
        ServerMetaInfo metaInfo = wrapper.getMetaInfo();
        URI uri = wrapper.getUri();
        URI mergedUrl = UriComponentsBuilder.fromUri(uri).host(metaInfo.getIp()).port(metaInfo.getPort())
                .replacePath(uri.getPath()).build().toUri();
        exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, mergedUrl);
        LOG.info(Module.CONFIGURATION, "转发uri:{},host:{},port:{},serviceName:{},method:{},module:{}", uri, metaInfo.getIp(), metaInfo.getPort(), metaInfo.getServiceName(), wrapper.getMethod(), metaInfo.getModule());
        return chain.filter(exchange);
    }


    @Override
    public int getOrder()
    {
        return OrderConstants.MAX_ORDER;
    }
}
