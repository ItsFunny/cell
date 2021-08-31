package com.cell.context;

import com.cell.annotations.HttpCmdAnno;
import com.cell.command.IHttpCommand;
import com.cell.constant.HttpConstants;
import com.cell.constants.ContextConstants;
import com.cell.enums.CellError;
import com.cell.enums.EnumHttpResponseType;
import com.cell.exception.HttpFramkeworkException;
import com.cell.exceptions.ProgramaException;
import com.cell.hook.IHttpCommandHook;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.cell.protocol.AbstractBaseContext;
import com.cell.protocol.CommandContext;
import com.cell.protocol.ContextResponseWrapper;
import com.cell.reactor.IHttpReactor;
import com.cell.util.HttpUtils;
import com.cell.utils.ClassUtil;
import com.cell.utils.StringUtils;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-30 11:34
 */
@Data
public abstract class AbstractHttpCommandContext extends AbstractBaseContext implements IHttpContext
{
    protected CommandContext commandContext;
    // FIXME ,有更好的做法
    protected IHttpCommandHook hook;

    protected IHttpReactor reactor;
    private HttpCmdAnno httpCmdAnno;

    public void setReactor(IHttpReactor reactor)
    {
        this.reactor = reactor;
    }

    public AbstractHttpCommandContext(CommandContext commandContext, IHttpCommandHook hk)
    {
        this.commandContext = commandContext;
        this.hook = hk;
    }

    @Override
    public DeferredResult<Object> getResult()
    {
        return this.commandContext.getResponseResult();
    }

    @Override
    public void response(ContextResponseWrapper wp)
    {
        long currentTime = System.currentTimeMillis();
        long consumeTime = currentTime - this.getRequestTimestamp();
        final String sequenceId = this.commandContext.getSummary().getSequenceId();
        LOG.info(Module.HTTP_FRAMEWORK, "response,sequenceId={},cost={}", sequenceId, consumeTime);

        this.commandContext.getHttpResponse().addHeader(HttpConstants.HTTP_HEADER_CODE, String.valueOf(wp.getStatus()));
        this.commandContext.getHttpResponse().addHeader(HttpConstants.HTTP_HEADER_MSG, wp.getMsg());
        if (wp.getOther().containsKey(ContextConstants.HTTP_STATUS))
        {
            this.commandContext.getHttpResponse().setStatus((Integer) wp.getOther().get(ContextConstants.HTTP_STATUS));
        }


        // 提前结束
        if (null != wp.getException())
        {
            LOG.erroring(Module.HTTP_FRAMEWORK, "调用失败,exception:{},from:{}", wp.getException(), wp.getFrom());
            this.hook.exceptionCaught(wp.getException());
            this.getPromise().trySuccess();
            this.commandContext.getResponseResult().setResult(wp.getRet());
            return;
        }
        if (null == this.httpCmdAnno)
        {
            this.httpCmdAnno = (HttpCmdAnno) ClassUtil.mustGetAnnotation(this.reactor.getCmd(this.commandContext.getURI()), HttpCmdAnno.class);
        }

        long status = wp.getStatus();
        if (this.timeout(status))
        {
            LOG.warn(Module.HTTP_FRAMEWORK, "触发了超时,cost={},info={}", consumeTime, wp);
            this.hook.exceptionCaught(new HttpFramkeworkException((IHttpCommand) wp.getCmd(), CellError.PROMISE_TIMEOUT, "asd"));
        }
        if (this.getResult().isSetOrExpired())
        {
            LOG.error("sequenceId = {}, duplicated response for request {} ", sequenceId, wp.getCmd());
            // FIXME
            this.getPromise().setFailure(new HttpFramkeworkException("duplicate result", ""));
            return;
        }

        if (this.viewMode())
        {
            String viewName = this.httpCmdAnno.viewName();
            if (StringUtils.isEmpty(viewName))
            {
                LOG.erroring(Module.HTTP_FRAMEWORK, "没有设置viewName,cmd:{}", this);
                this.getPromise().setFailure(new HttpFramkeworkException("zzz", "asd"));
                // FIXME
                this.getResult().setErrorResult(wp.getRet());
                return;
            }
            // FIXME ,添加前缀
            ModelAndView view = new ModelAndView(viewName);
            if (wp.getRet() != null)
            {
                view.addObject(wp.getRet());
            }
            this.commandContext.getResponseResult().setResult(view);
            this.getPromise().trySuccess();
            return;
        }

        try
        {
            // success
            if (this.success(status))
            {
                return;
            }

            if (this.programError(status))
            {
                this.commandContext.getHttpResponse().setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        } finally
        {
            this.commandContext.getResponseResult().setResult(wp.getRet());
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

    private boolean timeout(long status)
    {
        return (status & ContextConstants.TIMEOUT) >= ContextConstants.TIMEOUT;
    }

    private boolean programError(long status)
    {
        return (status & ContextConstants.PROGRAMA_ERROR) >= ContextConstants.PROGRAMA_ERROR;
    }

    @Override
    public void discard() throws IOException
    {
        this.commandContext.discard();
    }

//    @Override
//    public void autoResponse()
//    {
//        long now = System.currentTimeMillis();
//        long requestTimestamp = this.commandContext.getRequestTimestamp();
//        long consumeTime = now - requestTimestamp;
//        final String sequenceId = HttpUtils.get(httpCommand);
//    }


    @Override
    public String getURI()
    {
        return this.commandContext.getURI();
    }

    @Override
    public HttpServletRequest getHttpRequest()
    {
        return this.commandContext.getHttpRequest();
    }

    public boolean viewMode()
    {
        return this.httpCmdAnno.responseType() == EnumHttpResponseType.HTTP_HTML;
    }
}
