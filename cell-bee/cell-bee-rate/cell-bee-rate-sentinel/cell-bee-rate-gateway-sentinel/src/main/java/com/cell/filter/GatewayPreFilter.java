package com.cell.filter;


import com.alibaba.csp.sentinel.AsyncEntry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.ResourceTypeConstants;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.param.GatewayParamParser;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.sc.ServerWebExchangeItemParser;
import com.alibaba.csp.sentinel.adapter.gateway.sc.api.GatewayApiMatcherManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.api.matcher.WebExchangeApiMatcher;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.reactor.SentinelReactorConstants;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.util.function.Predicate;
import com.cell.IRateEntry;
import com.cell.IRateService;
import com.cell.annotations.ActivePlugin;
import com.cell.annotations.AutoPlugin;
import com.cell.config.GatewayConfiguration;
import com.cell.constants.SentinelConstants;
import com.cell.exception.RateBlockException;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.cell.rate.SentinelRateServiceImpl;
import com.cell.utils.GatewayUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import swxa.Q;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Set;
import java.util.stream.Collectors;

import static com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-12 08:35
 */
@ActivePlugin
public class GatewayPreFilter implements GatewayFilter, GlobalFilter, Ordered
{
    private final int order;

    @AutoPlugin
    private IRateService rateService;

    @AutoPlugin
    private BlockRequestHandler blockRequestHandler;

    public GatewayPreFilter()
    {
        this(Ordered.HIGHEST_PRECEDENCE);
    }

    public GatewayPreFilter(int order)
    {
        this.order = order;
    }

    private final GatewayParamParser<ServerWebExchange> paramParser = new GatewayParamParser<>(
            new ServerWebExchangeItemParser());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        String routerResource = exchange.getRequest().getURI().getPath();
        // check if it is in black list ,then we should just discard
        if (this.inBlackList(routerResource))
        {
            return GatewayUtils.fastFinish(exchange, "BLACK");
        }
        Deque<IRateEntry> queue = new ArrayDeque<>();
        String fallBackRoute = routerResource;
        try
        {
            // modified by charlie : we have to track more ,so be it
            Set<String> matchingApis = pickMatchingApiDefinitions(exchange);
            for (String apiName : matchingApis)
            {
                // FIXME ,need fallBack
                fallBackRoute = apiName;
                this.enterSentinelEntryQueue(apiName, RESOURCE_MODE_CUSTOM_API_NAME, exchange, queue);
            }
            this.enterSentinelEntryQueue(routerResource, RESOURCE_MODE_CUSTOM_API_NAME, exchange, queue);
            return chain.filter(exchange);
        } catch (RateBlockException e)
        {
            LOG.info(Module.HTTP_GATEWAY_SENTINEL, "限流,router:{}", routerResource);
            if (GatewayConfiguration.getInstance().getServerRatePropertyNode().isFastFinish())
            {
                return GatewayUtils.fastFinish(exchange, "block");
            }
            return null;
        } finally
        {
            if (!queue.isEmpty())
            {
                exchange.getAttributes().put(SentinelConstants.ENTINEL_ENTRIES_KEY, queue);
            }
        }


//        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
//        Mono<Void> asyncResult = chain.filter(exchange);
//        if (route != null)
//        {
//            String routeId = route.getId();
//            Object[] params = paramParser.parseParameterFor(routeId, exchange,
//                    r -> r.getResourceMode() == SentinelGatewayConstants.RESOURCE_MODE_ROUTE_ID);
//            String origin = Optional.ofNullable(GatewayCallbackManager.getRequestOriginParser())
//                    .map(f -> f.apply(exchange))
//                    .orElse("");
//            asyncResult = asyncResult.transform(
//                    new SentinelReactorTransformer<>(new EntryConfig(routeId, ResourceTypeConstants.COMMON_API_GATEWAY,
//                            EntryType.IN, 1, params, new ContextConfig(contextName(routeId), origin)))
//            );
//        }
//
//        Set<String> matchingApis = pickMatchingApiDefinitions(exchange);
//        for (String apiName : matchingApis)
//        {
//            Object[] params = paramParser.parseParameterFor(apiName, exchange,
//                    r -> r.getResourceMode() == SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME);
//            asyncResult = asyncResult.transform(
//                    new SentinelReactorTransformer<>(new EntryConfig(apiName, ResourceTypeConstants.COMMON_API_GATEWAY,
//                            EntryType.IN, 1, params))
//            );
//        }
//
//        return asyncResult;
    }

    private void enterSentinelEntryQueue(String resourceName, final int resType, ServerWebExchange exchange,
                                         Deque<IRateEntry> queue) throws RateBlockException
    {
        Object[] params = paramParser.parseParameterFor(resourceName, exchange,
                r -> r.getResourceMode() == resType);
        queue.add(this.rateService.acquire(SentinelRateServiceImpl.SentinelRateBody
                .builder()
                .params(params).resourceName(resourceName).build()));
    }

    private boolean inBlackList(String uri)
    {
        return false;
    }

    private String contextName(String route)
    {
        return SentinelGatewayConstants.GATEWAY_CONTEXT_ROUTE_PREFIX + route;
    }

    Set<String> pickMatchingApiDefinitions(ServerWebExchange exchange)
    {
        return GatewayApiMatcherManager.getApiMatcherMap().values()
                .stream()
                .filter(m -> m.test(exchange))
                .map(WebExchangeApiMatcher::getApiName)
                .collect(Collectors.toSet());
    }

    @Override
    public int getOrder()
    {
        return order;
    }
}
