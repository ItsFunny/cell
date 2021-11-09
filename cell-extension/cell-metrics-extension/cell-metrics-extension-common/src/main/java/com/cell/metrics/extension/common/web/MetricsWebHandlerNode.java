package com.cell.metrics.extension.common.web;

import com.cell.base.common.constants.OrderConstants;
import com.cell.base.core.annotations.AutoPlugin;
import com.cell.base.core.annotations.CellOrder;
import com.cell.base.core.annotations.ManagerNode;
import com.cell.base.core.constants.ManagerConstants;
import com.cell.base.core.executor.IBaseChainExecutor;
import com.cell.base.core.services.IHandlerSuit;
import com.cell.bee.statistic.prometheus.CellGaugeStator;
import com.cell.bee.statistic.prometheus.HistogramStator;
import com.cell.bee.statistic.prometheus.services.IStatContextService;
import com.cell.http.framework.context.IHttpCommandContext;
import com.cell.http.framework.context.IHttpHandlerSuit;
import com.cell.http.framework.handler.impl.AbstractHttpHandler;
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

    public MetricsWebHandlerNode() {}


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
