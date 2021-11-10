package com.cell.metrics.prometheus.sd.extension.reactor.commands;

import com.cell.base.common.enums.EnumHttpRequestType;
import com.cell.base.core.annotations.Optional;
import com.cell.base.core.constants.ContextConstants;
import com.cell.http.framework.annotation.HttpCmdAnno;
import com.cell.http.framework.command.impl.AbstractHttpCommand;
import com.cell.http.framework.context.IHttpCommandContext;
import com.cell.metrics.prometheus.sd.extension.model.ChangeItem;
import com.cell.metrics.prometheus.sd.extension.model.ServiceInstanceHealth;
import com.cell.metrics.prometheus.sd.extension.reactor.ServiceReactor;
import com.cell.metrics.prometheus.sd.extension.sd.IPrometheusServiceDiscovery;
import com.cell.metrics.prometheus.sd.extension.utils.SDUtils;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-26 21:46
 */
@HttpCmdAnno(
        uri = "/health/service/{appName}",
        requestType = EnumHttpRequestType.HTTP_URL_GET,
        buzzClz = ServiceHealthCommand.ServiceHealthCommandBO.class,
        reactor = ServiceReactor.class)
public class ServiceHealthCommand extends AbstractHttpCommand
{
    @Data
    public static class ServiceHealthCommandBO
    {
        @Optional
        private String wait;
        @Optional
        private Long index;
    }

    @Override
    protected void onExecute(IHttpCommandContext ctx, Object o) throws IOException
    {
        Map<String, String> pathUri = ctx.getPathUri();
        String appName = pathUri.get("appName");
        ServiceReactor reactor = (ServiceReactor) ctx.getHttpReactor();
        IPrometheusServiceDiscovery registrationService = reactor.getRegistrationService();
        ServiceHealthCommandBO bo = (ServiceHealthCommandBO) o;
        Mono<ChangeItem<List<ServiceInstanceHealth>>> mono = registrationService.getServiceHealth(appName, SDUtils.getWaitMillis(bo.getWait()), bo.getIndex());
        ChangeItem<List<ServiceInstanceHealth>> block = mono.block();
        ctx.response(this.createResponseWp()
                .status(ContextConstants.SUCCESS)
                .ret(ServiceReactor.createResponseEntity(block)).build());
    }


}
