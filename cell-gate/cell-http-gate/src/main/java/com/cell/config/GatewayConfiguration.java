package com.cell.config;

import com.cell.annotations.ActivePlugin;
import com.cell.annotations.Plugin;
import com.cell.lb.DefaultWeightRoubineStrategy;
import com.cell.lb.ILoadBalancerStrategy;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-10 04:36
 */
@ActivePlugin
public class GatewayConfiguration
{
    @Plugin
    public ILoadBalancerStrategy defaultLoadBalancerStrategy()
    {
        return new DefaultWeightRoubineStrategy();
    }

    @Plugin
    public RouteLocator myRoutes(RouteLocatorBuilder builder)
    {
        return builder.routes()
                .route(p -> p
                        .path("/get")
                        .filters(f -> f.addRequestHeader("Hello", "World"))
                        .uri("http://www.baidu.com:80"))
                .build();
    }
}
