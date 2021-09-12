package com.cell.config;

import com.cell.annotations.ActiveConfiguration;
import com.cell.annotations.Plugin;
import com.cell.lb.DefaultWeightRoubineStrategy;
import com.cell.lb.ILoadBalancerStrategy;
import org.springframework.cloud.gateway.filter.factory.*;
import org.springframework.cloud.gateway.handler.predicate.HostRoutePredicateFactory;
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
public class GatewaySpringConfiguration
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

    @Bean
    public HostRoutePredicateFactory hostRoutePredicateFactory()
    {
        return new HostRoutePredicateFactory();
    }

    @Bean
    public RewritePathGatewayFilterFactory rewritePathGatewayFilterFactory()
    {
        return new RewritePathGatewayFilterFactory();
    }

    @Bean
    public RetryGatewayFilterFactory retryGatewayFilterFactory()
    {
        return new RetryGatewayFilterFactory();
    }

    @Bean
    public SetPathGatewayFilterFactory setPathGatewayFilterFactory()
    {
        return new SetPathGatewayFilterFactory();
    }


    @Bean
    public SetRequestHeaderGatewayFilterFactory setRequestHeaderGatewayFilterFactory()
    {
        return new SetRequestHeaderGatewayFilterFactory();
    }

    @Bean
    public SetResponseHeaderGatewayFilterFactory setResponseHeaderGatewayFilterFactory()
    {
        return new SetResponseHeaderGatewayFilterFactory();
    }

    @Bean
    public RewriteResponseHeaderGatewayFilterFactory rewriteResponseHeaderGatewayFilterFactory()
    {
        return new RewriteResponseHeaderGatewayFilterFactory();
    }

    @Bean
    public RewriteLocationResponseHeaderGatewayFilterFactory rewriteLocationResponseHeaderGatewayFilterFactory()
    {
        return new RewriteLocationResponseHeaderGatewayFilterFactory();
    }

    @Bean
    public SetStatusGatewayFilterFactory setStatusGatewayFilterFactory()
    {
        return new SetStatusGatewayFilterFactory();
    }

    @Bean
    public SaveSessionGatewayFilterFactory saveSessionGatewayFilterFactory()
    {
        return new SaveSessionGatewayFilterFactory();
    }

    @Bean
    public StripPrefixGatewayFilterFactory stripPrefixGatewayFilterFactory()
    {
        return new StripPrefixGatewayFilterFactory();
    }

    @Bean
    public RequestHeaderToRequestUriGatewayFilterFactory requestHeaderToRequestUriGatewayFilterFactory()
    {
        return new RequestHeaderToRequestUriGatewayFilterFactory();
    }

    @Bean
    public RequestSizeGatewayFilterFactory requestSizeGatewayFilterFactory()
    {
        return new RequestSizeGatewayFilterFactory();
    }

    @Bean
    public RequestHeaderSizeGatewayFilterFactory requestHeaderSizeGatewayFilterFactory()
    {
        return new RequestHeaderSizeGatewayFilterFactory();
    }

    @Plugin
    public RouteLocator globalLocator(RouteLocatorBuilder builder)
    {
        return builder.routes()
                .route("global_route", p ->
                        p.path("/**")
                                .uri("https://www.baidu.com")).build();
    }

//    @Plugin
//    public RouteLocator myRoutes(RouteLocatorBuilder builder)
//    {
////        return builder.routes()
////                .route("global_route", r -> r.path("/get")
////                        .uri("http://httpbin.org"))
////                .route("router2", r -> r.path("/get2").uri("http://httpbin.org"))
////                .build();
//
////        return builder.routes()
////                .route("path_route", r -> r.path("/get")
////                        .uri("http://httpbin.org"))
////                .route("host_route", r -> r.host("*.myhost.org")
////                        .uri("http://httpbin.org"))
////                .route("rewrite_route", r -> r.host("*.rewrite.org")
////                        .filters(f -> f.rewritePath("/foo/(?<segment>.*)", "/${segment}"))
////                        .uri("http://httpbin.org"))
////                .route("hystrix_route", r -> r.host("*.hystrix.org")
//////                        .filters(f -> f.hystrix(c -> c.setName("slowcmd")))
////                        .uri("http://httpbin.org"))
////                .route("hystrix_fallback_route", r -> r.host("*.hystrixfallback.org")
//////                        .filters(f -> f.hystrix(c -> c.setName("slowcmd").setFallbackUri("forward:/hystrixfallback")))
////                        .uri("http://httpbin.org"))
////                .route("limit_route", r -> r
////                        .host("*.limited.org").and().path("/anything/**")
////                        .uri("http://httpbin.org"))
////                .build();
//    }
}
