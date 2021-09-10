package com.cell.config;

import com.cell.annotations.ActiveConfiguration;
import com.cell.annotations.Plugin;
import com.cell.lb.DefaultWeightRoubineStrategy;
import com.cell.lb.ILoadBalancerStrategy;
import org.springframework.cloud.gateway.filter.factory.AddRequestHeaderGatewayFilterFactory;
import org.springframework.cloud.gateway.handler.predicate.PathRoutePredicateFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-10 04:36
 */
@ActiveConfiguration
public class GatewayConfiguration
{
    @Plugin
    public ILoadBalancerStrategy defaultLoadBalancerStrategy()
    {
        return new DefaultWeightRoubineStrategy();
    }

    @Bean
    public RouteLocatorBuilder routeLocatorBuilder(ConfigurableApplicationContext context)
    {
        return new RouteLocatorBuilder(context);
    }

    @Bean
    public AddRequestHeaderGatewayFilterFactory addRequestHeaderGatewayFilterFactory()
    {
        return new AddRequestHeaderGatewayFilterFactory();
    }


    @Bean
    public PathRoutePredicateFactory pathRoutePredicateFactory()
    {
        return new PathRoutePredicateFactory();
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
