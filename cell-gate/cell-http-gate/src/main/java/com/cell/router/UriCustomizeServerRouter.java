//package com.cell.router;
//
//import com.cell.annotations.ActivePlugin;
//import com.cell.annotations.AutoPlugin;
//import com.cell.config.AbstractInitOnce;
//import com.cell.context.InitCTX;
//import com.cell.discovery.ServiceDiscovery;
//import com.cell.model.ServerMetaInfo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
//import org.springframework.cloud.gateway.route.RouteDefinition;
//import org.springframework.cloud.gateway.route.RouteDefinitionRouteLocator;
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.web.server.ServerWebExchange;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.function.Predicate;
//
///**
// * @author Charlie
// * @When
// * @Description
// * @Detail
// * @Attention:
// * @Date 创建时间：2021-09-11 11:29
// */
//@ActivePlugin
//public class UriCustomizeServerRouter extends AbstractInitOnce
//{
//    @AutoPlugin
//    private RouteLocatorBuilder builder;
//
//    @AutoPlugin
//    private ServiceDiscovery serviceDiscovery;
//
//
//    @Override
//    protected void onInit(InitCTX ctx)
//    {
//        serviceDiscovery.initOnce(ctx);
//        Map<String, List<ServerMetaInfo>> metas = serviceDiscovery.getServerMetas();
//        Set<String> uris = metas.keySet();
////        builder.routes()
////                .route("",p->p)
//    }
//}
