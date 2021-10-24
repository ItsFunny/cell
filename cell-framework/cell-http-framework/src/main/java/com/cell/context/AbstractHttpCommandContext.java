package com.cell.context;

import com.cell.adapter.HandlerMethodReturnValueHandler;
import com.cell.adapter.XMLHandlerMethodReturnValuleHandler;
import com.cell.annotation.HttpCmdAnno;
import com.cell.command.IHttpCommand;
import com.cell.command.impl.DummyHttpCommand;
import com.cell.constants.ContextConstants;
import com.cell.constants.ProtocolConstants;
import com.cell.couple.IHttpServerRequest;
import com.cell.couple.IHttpServerResponse;
import com.cell.enums.EnumHttpResponseType;
import com.cell.exception.HttpFramkeworkException;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.cell.protocol.AbstractBaseContext;
import com.cell.protocol.CommandContext;
import com.cell.protocol.ContextResponseWrapper;
import com.cell.protocol.HttpCommandContext;
import com.cell.reactor.IHttpReactor;
import com.cell.util.HttpUtils;
import com.cell.utils.ClassUtil;
import com.cell.utils.StringUtils;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-30 11:34
 */
@Data
public abstract class AbstractHttpCommandContext extends AbstractBaseContext implements IHttpCommandContext
{
    private HttpCmdAnno httpCmdAnno;
    private Class<? extends IHttpCommand> command;

    private String ip;

    private boolean success;

    public AbstractHttpCommandContext(CommandContext commandContext)
    {
        super(commandContext);
    }


    public void setCommand(Class<? extends IHttpCommand> cmd)
    {
        this.httpCmdAnno = cmd.getAnnotation(HttpCmdAnno.class);
        this.command = cmd;
    }

    @Override
    public Object getParameter(String key)
    {
        return this.getHttpServerRequest().getInternalRequest().getParameter(key);
    }

    protected IHttpServerRequest getHttpServerRequest()
    {
        return (IHttpServerRequest) this.getCommandContext().getRequest();
    }

    protected HttpCommandContext getHttpCommandContext()
    {
        return (HttpCommandContext) this.getCommandContext();
    }

    @Override
    public IHttpReactor getHttpReactor()
    {
        return (IHttpReactor) this.getReactor();
    }


    @Override
    public DeferredResult<Object> getResult()
    {
        return this.getHttpCommandContext().getResponseResult();
    }

    public Map<String, String> getUriRegexValue()
    {
        String uri = this.getURI();
        String patten = this.httpCmdAnno.uri();
        return HttpUtils.getRegexValues(patten, uri);
    }

    protected void done(HttpStatus status, Object ret)
    {
        this.response(ContextResponseWrapper.builder()
                .other(HttpContextResponseBody.builder()
                        .status(status)
                        .build())
                .build());
    }

    public boolean success()
    {
        return this.success;
    }

    protected IHttpServerResponse getServerResponse()
    {
        return (IHttpServerResponse) this.getCommandContext().getResponse();
    }

    @Override
    public void response(ContextResponseWrapper wp)
    {
        long currentTime = System.currentTimeMillis();
        long consumeTime = currentTime - this.getRequestTimestamp();
        final String sequenceId = this.getCommandContext().getSummary().getSequenceId();
        LOG.info(Module.HTTP_FRAMEWORK, "response,uri={},method={},ip={},sequenceId={},cost={}", this.getCommandContext().getSummary().getProtocolId(), this.getHttpServerRequest().getInternalRequest().getMethod(), this.getIp(), sequenceId, consumeTime);

        HttpServletResponse response = this.getServerResponse().getInternalResponse();
        if (wp.getHeaders() != null)
        {
            Map<String, String> headers = wp.getHeaders();
            for (String s : headers.keySet())
            {
                response.addHeader(s, headers.get(s));
            }
        }

        this.getCommandContext().getResponse().addHeader(ProtocolConstants.RESPONSE_HEADER_CODE, String.valueOf(wp.getStatus()));
        this.getCommandContext().getResponse().addHeader(ProtocolConstants.RESPONSE_HEADER_MSG, wp.getMsg());
        if (null != wp.getOther() && ((HttpContextResponseBody) wp.getOther()).getStatus() != null)
        {
            this.getCommandContext().getResponse().setStatus(((HttpContextResponseBody) wp.getOther()).getStatus().value());
        }

        // 提前结束
        if (null != wp.getException())
        {
            LOG.error(Module.HTTP_FRAMEWORK, wp.getException(), "调用失败,from:{}", wp.getFrom());
            this.getCommandContext().getResponse().fireResult(null);
            return;
        }
        if (null == this.httpCmdAnno)
        {
            if (wp.getCmd() == null)
            {
                wp.setCmd(new DummyHttpCommand());
            }
            this.httpCmdAnno = (HttpCmdAnno) ClassUtil.mustGetAnnotation(wp.getCmd().getClass(), HttpCmdAnno.class);
        }

        long status = wp.getStatus();
        if (this.timeout(status))
        {
            LOG.warn(Module.HTTP_FRAMEWORK, "触发了超时,cost={},info={}", consumeTime, wp);
        }
        if (this.getResult().isSetOrExpired())
        {
            LOG.error("sequenceId = {}, duplicated response for request {} ", sequenceId, wp.getCmd());
            // FIXME
            this.getCommandContext().getResponse().fireFailure(new HttpFramkeworkException("duplicate result", ""));
            return;
        }

        if (this.viewMode())
        {
            String viewName = this.httpCmdAnno.viewName();
            if (StringUtils.isEmpty(viewName))
            {
                LOG.erroring(Module.HTTP_FRAMEWORK, "没有设置viewName,cmd:{}", this);
                // FIXME
                this.getCommandContext().getResponse().fireFailure(new HttpFramkeworkException("zzz", "asd"));
                return;
            }
            // FIXME ,添加前缀
            ModelAndView view = new ModelAndView(viewName);
            if (wp.getRet() != null)
            {
                view.addObject(wp.getRet());
            }
            this.getCommandContext().getResponse().fireResult(view);
            return;
        }

        try
        {
            // success
            if (this.success(status))
            {
                this.success = true;
            } else if (this.programError(status))
            {
                this.getCommandContext().getResponse().setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        } finally
        {
            // FIXME ,需要干掉@ResponseBody
            Object ret = wp.getRet();
            if (this.xmlMode())
            {
                HandlerMethodReturnValueHandler<Object> handler = new XMLHandlerMethodReturnValuleHandler<>();
                ret = handler.handler(ret);
                this.getCommandContext().getResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE);
            } else
            {
                this.getCommandContext().getResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            }
            this.getCommandContext().getResponse().fireResult(ret);
        }
    }

    private boolean fail(long status)
    {
        return (status & ContextConstants.FAIL) >= ContextConstants.FAIL;
    }

    private boolean success(long status)
    {
        return (status & ContextConstants.SUCCESS) >= ContextConstants.SUCCESS;
    }


    @Override
    public void discard()
    {
        this.response(ContextResponseWrapper.builder()
                .status(ContextConstants.FAIL)
                .other(HttpContextResponseBody.builder().status(HttpStatus.BAD_REQUEST).build())
                .ret("BAD REQUEST")
                .msg("BAD REQUEST")
                .build());
    }

    @Override
    public String getURI()
    {
        return this.getCommandContext().getSummary().getProtocolId();
    }

    public Map<String, String> getPathUri()
    {
        return HttpUtils.getRegexValues(this.httpCmdAnno.uri(), this.getURI());
    }

    @Override
    public HttpServletRequest getHttpRequest()
    {
        return ((IHttpServerRequest) this.getCommandContext().getRequest()).getInternalRequest();
    }

    public HttpServletResponse getHttpResponse()
    {
        return ((IHttpServerResponse) this.getCommandContext().getResponse()).getInternalResponse();
    }

    public boolean viewMode()
    {
        return this.httpCmdAnno.responseType() == EnumHttpResponseType.HTTP_HTML;
    }

    private boolean xmlMode()
    {
        return this.httpCmdAnno.responseType() == EnumHttpResponseType.HTTP_XML;
    }


    @Override
    public void setIp(String ip)
    {
        this.ip = ip;
    }

    @Override
    public String getIp()
    {
        return this.ip;
    }

}
