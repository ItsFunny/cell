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
//
//    @Bean
//    public RouteLocatorBuilder routeLocatorBuilder(ConfigurableApplicationContext context)
//    {
//        return new RouteLocatorBuilder(context);
//    }

//    @Bean
//    public AddRequestHeaderGatewayFilterFactory addRequestHeaderGatewayFilterFactory()
//    {
//        return new AddRequestHeaderGatewayFilterFactory();
//    }
//
//
//    @Bean
//    public PathRoutePredicateFactory pathRoutePredicateFactory()
//    {
//        return new PathRoutePredicateFactory();
//    }
//
//    @Bean
//    public HostRoutePredicateFactory hostRoutePredicateFactory()
//    {
//        return new HostRoutePredicateFactory();
//    }
//
//    @Bean
//    public RewritePathGatewayFilterFactory rewritePathGatewayFilterFactory()
//    {
//        return new RewritePathGatewayFilterFactory();
//    }
//
//    @Bean
//    public RetryGatewayFilterFactory retryGatewayFilterFactory()
//    {
//        return new RetryGatewayFilterFactory();
//    }
//
//    @Bean
//    public SetPathGatewayFilterFactory setPathGatewayFilterFactory()
//    {
//        return new SetPathGatewayFilterFactory();
//    }
//
//
//    @Bean
//    public SetRequestHeaderGatewayFilterFactory setRequestHeaderGatewayFilterFactory()
//    {
//        return new SetRequestHeaderGatewayFilterFactory();
//    }
//
//    @Bean
//    public SetResponseHeaderGatewayFilterFactory setResponseHeaderGatewayFilterFactory()
//    {
//        return new SetResponseHeaderGatewayFilterFactory();
//    }
//
//    @Bean
//    public RewriteResponseHeaderGatewayFilterFactory rewriteResponseHeaderGatewayFilterFactory()
//    {
//        return new RewriteResponseHeaderGatewayFilterFactory();
//    }
//
//    @Bean
//    public RewriteLocationResponseHeaderGatewayFilterFactory rewriteLocationResponseHeaderGatewayFilterFactory()
//    {
//        return new RewriteLocationResponseHeaderGatewayFilterFactory();
//    }
//
//    @Bean
//    public SetStatusGatewayFilterFactory setStatusGatewayFilterFactory()
//    {
//        return new SetStatusGatewayFilterFactory();
//    }
//
//    @Bean
//    public SaveSessionGatewayFilterFactory saveSessionGatewayFilterFactory()
//    {
//        return new SaveSessionGatewayFilterFactory();
//    }
//
//    @Bean
//    public StripPrefixGatewayFilterFactory stripPrefixGatewayFilterFactory()
//    {
//        return new StripPrefixGatewayFilterFactory();
//    }
//
//    @Bean
//    public RequestHeaderToRequestUriGatewayFilterFactory requestHeaderToRequestUriGatewayFilterFactory()
//    {
//        return new RequestHeaderToRequestUriGatewayFilterFactory();
//    }
//
//    @Bean
//    public RequestSizeGatewayFilterFactory requestSizeGatewayFilterFactory()
//    {
//        return new RequestSizeGatewayFilterFactory();
//    }
//
//    @Bean
//    public RequestHeaderSizeGatewayFilterFactory requestHeaderSizeGatewayFilterFactory()
//    {
//        return new RequestHeaderSizeGatewayFilterFactory();
//    }

    @Plugin
    public RouteLocator globalLocator(RouteLocatorBuilder builder)
    {
        return builder.routes()
                .route("global_route", p ->
                        p.path("/**")
                                .uri("https://www.baidu.com")).build();
    }

}
