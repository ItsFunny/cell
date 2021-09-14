package com.cell.extension;

import com.cell.config.GatewayConfiguration;
import com.cell.context.INodeContext;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-12 21:32
 */
public class SentinelGatewayExtension extends AbstractSpringNodeExtension
{
//    private GatewayPreFilter gatewayPreFilter;
//    private BlockRequestHandler handler;

//    @Plugin
//    public RouteLocator globalLocator(RouteLocatorBuilder builder)
//    {
//        return builder.routes()
//                .route("global_route", p ->
//                        p.path("/**")
//                                .uri("https://www.baidu.com")).build();
//    }


    @Override
    public void onInit(INodeContext ctx) throws Exception
    {
//        this.gatewayPreFilter = new GatewayPreFilter();
        GatewayConfiguration.init();
    }

    @Override
    public void onStart(INodeContext ctx) throws Exception
    {
//        GatewayCallbackManager.setBlockHandler(this.handler);
    }

    @Override
    public void onReady(INodeContext ctx) throws Exception
    {

    }

    @Override
    public void onClose(INodeContext ctx) throws Exception
    {

    }
}
