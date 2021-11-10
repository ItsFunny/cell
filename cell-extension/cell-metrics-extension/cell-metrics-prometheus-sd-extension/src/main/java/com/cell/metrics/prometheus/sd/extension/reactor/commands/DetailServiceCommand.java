package com.cell.metrics.prometheus.sd.extension.reactor.commands;

import com.cell.base.common.enums.EnumHttpRequestType;
import com.cell.base.core.annotations.Optional;
import com.cell.http.framework.annotation.HttpCmdAnno;
import com.cell.http.framework.command.impl.AbstractHttpCommand;
import com.cell.http.framework.context.IHttpCommandContext;
import com.cell.metrics.prometheus.sd.extension.model.ChangeItem;
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
 * @Date 创建时间：2021-09-25 08:48
 */
@HttpCmdAnno(uri = "/catalog/service/{service}",
        reactor = ServiceReactor.class,
        requestType = EnumHttpRequestType.HTTP_URL_GET,
        buzzClz = DetailServiceCommand.ServiceBo.class)
public class DetailServiceCommand extends AbstractHttpCommand
{
    private static final String CONSUL_IDX_HEADER = "X-Consul-Index";

    @Data
    public static class ServiceBo
    {
        @Optional
        private String QUERY_PARAM_WAIT;
        @Optional
        private Long QUERY_PARAM_INDEX;
    }

    @Override
    protected void onExecute(IHttpCommandContext ctx, Object o) throws IOException
    {
        Map<String, String> pathUri = ctx.getPathUri();
        String serviceName = pathUri.get("service");
        ServiceReactor reactor = (ServiceReactor) ctx.getHttpReactor();
        IPrometheusServiceDiscovery registrationService = reactor.getRegistrationService();
        ServiceBo bo = (ServiceBo) o;
        Mono<ChangeItem<List<Map<String, Object>>>> service = registrationService.getService(serviceName, SDUtils.getWaitMillis(bo.getQUERY_PARAM_WAIT()), bo.getQUERY_PARAM_INDEX());
        ChangeItem<List<Map<String, Object>>> items = service.block();
        ctx.response(this.createResponseWp()
                .ret(ServiceReactor.createResponseEntity(items))
                .build());
    }

}
