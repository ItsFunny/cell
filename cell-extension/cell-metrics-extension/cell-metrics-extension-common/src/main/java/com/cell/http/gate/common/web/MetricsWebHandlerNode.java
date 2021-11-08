package com.cell.http.gate.common.web;

import com.cell.annotations.AutoPlugin;
import com.cell.annotations.CellOrder;
import com.cell.annotations.ManagerNode;
import com.cell.constants.ManagerConstants;
import com.cell.base.common.constants.OrderConstants;
import com.cell.context.IHttpCommandContext;
import com.cell.context.IHttpHandlerSuit;
import com.cell.executor.IBaseChainExecutor;
import com.cell.handler.impl.AbstractHttpHandler;
import com.cell.prometheus.CellGaugeStator;
import com.cell.prometheus.HistogramStator;
import com.cell.services.IHandlerSuit;
import com.cell.services.IStatContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
public class MetricsWebHandlerNode extends AbstractHttpHandler
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


    @Override
    protected Mono<Void> onHandle(IHandlerSuit context, IBaseChainExecutor executor)
    {
        IHttpHandlerSuit suit = (IHttpHandlerSuit) context;
        IHttpCommandContext buzContext = (IHttpCommandContext) suit.getBuzContext();
        HttpServletRequest request = buzContext.getHttpRequest();
        totalCounter.labels(statContextService.getNodeName(), statContextService.getClusterName(), request.getMethod()).inc(1);
        return executor.execute(context).onErrorResume(e ->
        {
            this.failCounter.labels(getLabels(request)).inc();
            return Mono.error(e);
        }).then(Mono.fromRunnable(() ->
        {
            long endTime = System.currentTimeMillis();
            Long startTime = buzContext.getRequestTimestamp();
            long cost = endTime - startTime;
            int status = buzContext.getHttpResponse().getStatus();
            String[] labels = getLabels(request);
            if (status != HttpStatus.OK.value())
            {
                this.failCounter.labels(labels).inc();
            } else
            {
                this.successCounter.labels(labels).inc();
            }
            exceedDelayThresoldCount.labels(getLabels(request)).observe(cost);
        }));
    }

    private String[] getLabels(HttpServletRequest request)
    {
        return new String[]{statContextService.getNodeName(), statContextService.getClusterName(), "DEFAULT", request.getRequestURI(), request.getMethod()};
    }
}
