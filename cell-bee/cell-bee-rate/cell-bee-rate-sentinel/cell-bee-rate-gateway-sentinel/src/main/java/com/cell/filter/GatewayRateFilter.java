package com.cell.filter;


import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.param.GatewayParamParser;
import com.alibaba.csp.sentinel.adapter.gateway.sc.ServerWebExchangeItemParser;
import com.alibaba.csp.sentinel.adapter.gateway.sc.api.GatewayApiMatcherManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.api.matcher.WebExchangeApiMatcher;
import com.cell.IRateEntry;
import com.cell.IRateService;
import com.cell.annotations.ActivePlugin;
import com.cell.annotations.AutoPlugin;
import com.cell.base.IScheduleCounter;
import com.cell.center.JobCenter;
import com.cell.constants.DebugConstants;
import com.cell.constants.SentinelConstants;
import com.cell.event.StasticEvent;
import com.cell.exception.RateBlockException;
import com.cell.handler.IGatewayBlockHandler;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.cell.rate.SentinelRateServiceImpl;
import com.cell.utils.GatewayUtils;
import com.cell.utils.RateUtils;
import com.cell.utils.StringUtils;
import com.cell.utils.UUIDUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Set;
import java.util.function.Consumer;
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
public class GatewayRateFilter implements GlobalFilter, Ordered
{
    @AutoPlugin
    private IRateService rateService;

    @AutoPlugin
    private IScheduleCounter scheduleCounter;

    @AutoPlugin
    private IGatewayBlockHandler blockRequestHandler;
    // FIXME ,NEED HANDLER FOR FALLBACK AND BLOCK_HANDLER


    private final GatewayParamParser<ServerWebExchange> paramParser = new GatewayParamParser<>(
            new ServerWebExchangeItemParser());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        String routerResource = exchange.getRequest().getURI().getPath();
        this.preRequest(routerResource, exchange);
        // check if it is in black list ,then we should just discard
        long start = System.currentTimeMillis();
        if (this.inBlackList(routerResource))
        {
            try
            {
                return GatewayUtils.fastFinish(exchange, "BLACK");
            } finally
            {
                this.postRequest(start, routerResource, exchange);
            }
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
            return chain.filter(exchange).then(Mono.fromRunnable(() ->
                    this.postRequest(start, routerResource, exchange)));
        } catch (RateBlockException e)
        {
            LOG.info(Module.HTTP_GATEWAY_SENTINEL, "触发限流,router:{}", routerResource);
            return this.blockRequestHandler.denyForReason(exchange);
        } finally
        {
            if (!queue.isEmpty())
            {
                exchange.getAttributes().put(SentinelConstants.ENTINEL_ENTRIES_KEY, queue);
            }
        }
    }

    private void postRequest(long startTime, String path, ServerWebExchange exchange)
    {
        long cost = System.currentTimeMillis() - startTime;

        StasticEvent event = StasticEvent.builder()
                .startTIme(startTime)
                .method(exchange.getRequest().getMethod().name().toLowerCase())
                .uri(exchange.getRequest().getURI().getPath())
                .endTime(System.currentTimeMillis()).build();
        JobCenter.getInstance().addJob(event);

        LOG.info(Module.HTTP_GATEWAY, "执行完毕,uri:{},sequenceId:{},耗时:{}", path, GatewayUtils.getSequenceId(exchange.getRequest()), cost);
        RateUtils.exitEntry(exchange);
    }

    private void preRequest(String path, ServerWebExchange exchange)
    {
        ServerHttpRequest request = exchange.getRequest();
        String sequenceId = request.getHeaders().getFirst(DebugConstants.SEQUENCE_ID);
        if (StringUtils.isEmpty(sequenceId))
        {
            final String sid = UUIDUtils.uuid2();
            Consumer<HttpHeaders> httpHeaders = httpHeader ->
                    httpHeader.set(DebugConstants.SEQUENCE_ID, sid);
            sequenceId = sid;
            ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate().headers(httpHeaders).build();
            exchange.mutate().request(serverHttpRequest).build();
        }
        LOG.info(Module.HTTP_GATEWAY, "收到请求:{},sequenceId:{}", path, sequenceId);
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
        return Ordered.HIGHEST_PRECEDENCE;
    }


}
