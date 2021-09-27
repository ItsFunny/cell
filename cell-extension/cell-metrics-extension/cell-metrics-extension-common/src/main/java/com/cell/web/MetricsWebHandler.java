package com.cell.web;

import com.cell.annotations.AutoPlugin;
import com.cell.annotations.CellOrder;
import com.cell.annotations.ManagerNode;
import com.cell.constants.ManagerConstants;
import com.cell.constants.OrderConstants;
import com.cell.context.IHttpCommandContext;
import com.cell.context.IHttpHandlerSuit;
import com.cell.handler.impl.AbstractHttpHandler;
import com.cell.hooks.IChainExecutor;
import com.cell.prometheus.CellGaugeStator;
import com.cell.prometheus.HistogramStator;
import com.cell.services.IHandlerSuit;
import com.cell.services.IStatContextService;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-27 13:35
 */
@ManagerNode(group = ManagerConstants.MANAGER_WEB_HANDLER, name = "metrics")
@CellOrder(OrderConstants.MIN_ORDER)
public class MetricsWebHandler extends AbstractHttpHandler
{
    @Autowired
    private HistogramStator exceedDelayThresoldCount;

    @AutoPlugin
    private CellGaugeStator successCounter;

    @AutoPlugin
    private CellGaugeStator failCounter;

    @AutoPlugin
    private IStatContextService statContextService;

    @AutoPlugin
    private CellGaugeStator totalCounter;

    private static final String httpRequestStartTime = "httpRequestStartTime";


    @Override
    protected Mono<Void> onHandle(IHandlerSuit context, IChainExecutor executor)
    {
        IHttpHandlerSuit suit = (IHttpHandlerSuit) context;
        IHttpCommandContext buzContext = suit.getBuzContext();
        buzContext.getHttpRequest().setAttribute(httpRequestStartTime, System.currentTimeMillis());
        totalCounter.inc(1);
        return executor.execute(context).onErrorResume(e ->
        {
            this.failCounter.inc();
            return Mono.error(e);
        }).then(Mono.fromRunnable(() ->
        {
            HttpServletRequest httpRequest = buzContext.getHttpRequest();
            long endTime = System.currentTimeMillis();
            Long startTime = (Long) httpRequest.getAttribute(httpRequestStartTime);
            long cost = endTime - startTime;
            String nodeName = statContextService.getNodeName();
            exceedDelayThresoldCount.labels(nodeName,
                    statContextService.getClusterName(),
                    "DEFAULT",
                    httpRequest.getRequestURI(), httpRequest.getMethod()).observe(cost);
        }));
    }
}
