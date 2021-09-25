package com.cell.reactor.commands;

import com.cell.annotations.HttpCmdAnno;
import com.cell.annotations.Optional;
import com.cell.command.impl.AbstractHttpCommand;
import com.cell.context.IHttpCommandContext;
import com.cell.enums.EnumHttpRequestType;
import com.cell.model.ChangeItem;
import com.cell.reactor.IHttpReactor;
import com.cell.reactor.ServiceReactor;
import com.cell.sd.RegistrationService;
import com.cell.utils.SDUtils;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-25 08:48
 */
@HttpCmdAnno(uri = "/catalog/service/{service}",
        httpCommandId = 1,
        group = ServiceReactor.prometheusServiceReactor,
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
        RegistrationService registrationService = reactor.getRegistrationService();
        ServiceBo bo = (ServiceBo) o;
        Mono<ChangeItem<List<Map<String, Object>>>> service = registrationService.getService(serviceName, SDUtils.getWaitMillis(bo.getQUERY_PARAM_WAIT()), bo.getQUERY_PARAM_INDEX());
        ChangeItem<List<Map<String, Object>>> items = service.block();
        ctx.response(this.createResponseWp()
                .ret(createResponseEntity(items.getItem(), items.getChangeIndex()))
                .build());
    }

    private <T> ResponseEntity<T> createResponseEntity(T body, long index)
    {
        return new ResponseEntity<>(body, createHeaders(index), HttpStatus.OK);
    }

    private MultiValueMap<String, String> createHeaders(long index)
    {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(CONSUL_IDX_HEADER, "" + index);
        return headers;
    }
}
