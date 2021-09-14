package com.cell.filter;

import com.cell.annotations.ActivePlugin;
import com.cell.annotations.AutoPlugin;
import com.cell.center.JobCenter;
import com.cell.constants.HttpConstant;
import com.cell.constants.OrderConstants;
import com.cell.discovery.ServiceDiscovery;
import com.cell.exception.GatewayException;
import com.cell.extension.SentinelGatewayExtension;
import com.cell.hook.ErrorResponseHook;
import com.cell.log.LOG;
import com.cell.model.ErrorResponseEvent;
import com.cell.model.ServerMetaInfo;
import com.cell.models.Module;
import com.cell.utils.GatewayUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
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
        ServiceDiscovery serviceDiscovery = ServiceDiscovery.getInstance();
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();

        ServerMetaInfo metaInfo = serviceDiscovery.choseServer(request.getMethod().name().toLowerCase(), uri.getPath());
        // FIXME ,定制化信息
        if (metaInfo == null)
        {
            JobCenter.getInstance().addJob(ErrorResponseEvent.builder().test("asdd").build());
//            return chain.filter(exchange);
//            throw new GatewayException("command not exists");
            return GatewayUtils.fastFinish(exchange, "command not exists");
        }
        URI mergedUrl = UriComponentsBuilder.fromUri(uri).host(metaInfo.getIp()).port(metaInfo.getPort())
                .replacePath(uri.getPath()).build().toUri();
        exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, mergedUrl);
        LOG.info(Module.CONFIGURATION, "转发uri:{},host:{},port:{},serviceName:{}", uri, metaInfo.getIp(), metaInfo.getPort(), metaInfo.getServiceName());
        return chain.filter(exchange);
    }


    @Override
    public int getOrder()
    {
        return OrderConstants.MAX_ORDER;
    }
}
