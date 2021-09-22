package com.cell.reactor.commands;

import com.cell.annotations.HttpCmdAnno;
import com.cell.command.impl.AbstractHttpCommand;
import com.cell.context.IHttpCommandContext;
import com.cell.enums.EnumHttpRequestType;
import com.cell.model.ChangeItem;
import com.cell.protocol.IBuzzContext;
import com.cell.reactor.ServiceReactor;
import com.cell.sd.RegistrationService;
import com.cell.serialize.IInputArchive;
import com.cell.serialize.IOutputArchive;
import com.cell.serialize.ISerializable;
import com.cell.utils.RandomUtils;
import com.cell.utils.StringUtils;
import lombok.Data;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-22 21:30
 */
@HttpCmdAnno(uri = "services", httpCommandId = 1, group = ServiceReactor.prometheusServiceReactor, requestType = EnumHttpRequestType.HTTP_URL_GET)
public class AllServiceCommand extends AbstractHttpCommand
{
    private static final Pattern WAIT_PATTERN = Pattern.compile("(\\d*)(m|s|ms|h)");

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
    public ISerializable getBO(IBuzzContext context)
    {
//        return
        return null;
    }

    @Override
    protected void onExecute(IHttpCommandContext ctx, ISerializable bo) throws IOException
    {
        ServiceReactor reactor = (ServiceReactor) ctx.getHttpReactor();
        RegistrationService registrationService = reactor.getRegistrationService();
        HttpServletRequest request = ctx.getHttpRequest();
        String query_param_wait = request.getParameter("QUERY_PARAM_WAIT");
        String index = request.getParameter("index");
        if (StringUtils.isEmpty(index)) index = "0";
        Mono<ChangeItem<Map<String, String[]>>> serviceNames = registrationService.getServiceNames(getWaitMillis(query_param_wait), Long.valueOf(index));
        ctx.response(this.createResponseWp()
                .status(HttpStatus.OK.value()).ret(serviceNames).build());
    }

    private long getWaitMillis(String wait)
    {
        // default from consul docu
        long millis = TimeUnit.MINUTES.toMillis(5);
        if (wait != null)
        {
            Matcher matcher = WAIT_PATTERN.matcher(wait);
            if (matcher.matches())
            {
                Long value = Long.valueOf(matcher.group(1));
                TimeUnit timeUnit = parseTimeUnit(matcher.group(2));
                millis = timeUnit.toMillis(value);
            } else
            {
                throw new IllegalArgumentException("Invalid wait pattern");
            }
        }
        return millis + RandomUtils.randomLong();
    }

    private TimeUnit parseTimeUnit(String unit)
    {
        switch (unit)
        {
            case "h":
                return TimeUnit.HOURS;
            case "m":
                return TimeUnit.MINUTES;
            case "s":
                return TimeUnit.SECONDS;
            case "ms":
                return TimeUnit.MILLISECONDS;
            default:
                throw new IllegalArgumentException("No valid time unit");
        }
    }
}
