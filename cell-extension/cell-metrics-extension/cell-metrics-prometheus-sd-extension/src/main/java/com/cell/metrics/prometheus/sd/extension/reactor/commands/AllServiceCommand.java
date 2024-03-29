package com.cell.metrics.prometheus.sd.extension.reactor.commands;

import com.cell.base.common.enums.EnumHttpRequestType;
import com.cell.base.common.utils.StringUtils;
import com.cell.base.core.serialize.IInputArchive;
import com.cell.base.core.serialize.IOutputArchive;
import com.cell.base.core.serialize.ISerializable;
import com.cell.http.framework.annotation.HttpCmdAnno;
import com.cell.http.framework.command.impl.AbstractHttpCommand;
import com.cell.http.framework.context.IHttpCommandContext;
import com.cell.metrics.prometheus.sd.extension.model.ChangeItem;
import com.cell.metrics.prometheus.sd.extension.reactor.ServiceReactor;
import com.cell.metrics.prometheus.sd.extension.sd.IPrometheusServiceDiscovery;
import com.cell.metrics.prometheus.sd.extension.utils.SDUtils;
import lombok.Data;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-22 21:30
 */
@HttpCmdAnno(uri = "/catalog/services",
        reactor = ServiceReactor.class,
        requestType = EnumHttpRequestType.HTTP_URL_GET,
        buzzClz = AllServiceCommand.AllServiceCommandBO.class)
public class AllServiceCommand extends AbstractHttpCommand
{
    @Data
    public static class AllServiceCommandBO implements ISerializable
    {
        private String QUERY_PARAM_WAIT;
        private Long index;

        @Override
        public void read(IInputArchive input) throws IOException
        {
            this.QUERY_PARAM_WAIT = input.readStringNullable("QUERY_PARAM_WAIT");
            this.index = input.readLongNullable("index");
        }

        @Override
        public void write(IOutputArchive output) throws IOException
        {

        }
    }


    @Override
    protected void onExecute(IHttpCommandContext ctx, Object bo) throws IOException
    {
        ServiceReactor reactor = (ServiceReactor) ctx.getHttpReactor();
        IPrometheusServiceDiscovery registrationService = reactor.getRegistrationService();
        HttpServletRequest request = ctx.getHttpRequest();
        String query_param_wait = request.getParameter("QUERY_PARAM_WAIT");
        String index = request.getParameter("index");
        if (StringUtils.isEmpty(index)) index = "0";
        Mono<ChangeItem<Map<String, String[]>>> serviceNames = registrationService.getServiceNames(SDUtils.getWaitMillis(query_param_wait), Long.valueOf(index));
        ChangeItem<Map<String, String[]>> ret = serviceNames.block();
        ctx.response(this.createResponseWp()
                .status(HttpStatus.OK.value()).ret(ServiceReactor.createResponseEntity(ret)).build());
    }


}
