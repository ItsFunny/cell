//package com.cell.extension;
//
//import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
//import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.DefaultBlockRequestHandler;
//import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
//import com.cell.annotations.Plugin;
//import com.cell.context.INodeContext;
//import com.cell.filter.GatewayPreFilter;
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//
///**
// * @author Charlie
// * @When
// * @Description
// * @Detail
// * @Attention:
// * @Date 创建时间：2021-09-12 21:32
// */
//public class SentinelGatewayExtension extends AbstractSpringNodeExtension
//{
//    private GatewayPreFilter gatewayPreFilter;
//    private BlockRequestHandler handler;
//
//
//    @Plugin
//    public RouteLocator globalLocator(RouteLocatorBuilder builder)
//    {
//        return builder.routes()
//                .route("global_route", p ->
//                        p.path("/**")
//                                .uri("https://www.baidu.com")).build();
//    }
//
//
//    @Override
//    public void onInit(INodeContext ctx) throws Exception
//    {
//        this.gatewayPreFilter = new GatewayPreFilter();
//    }
//
//    @Override
//    public void onStart(INodeContext ctx) throws Exception
//    {
//        GatewayCallbackManager.setBlockHandler(this.handler);
//    }
//
//    @Override
//    public void onReady(INodeContext ctx) throws Exception
//    {
//
//    }
//
//    @Override
//    public void onClose(INodeContext ctx) throws Exception
//    {
//
//    }
//}
