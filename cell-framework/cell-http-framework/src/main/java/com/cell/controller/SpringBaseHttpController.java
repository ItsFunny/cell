package com.cell.controller;

import cn.tass.math.raw.Mod;
import com.cell.annotations.AutoPlugin;
import com.cell.constant.HttpConstants;
import com.cell.dispatcher.IHttpCommandDispatcher;
import com.cell.exception.HttpFramkeworkException;
import com.cell.handler.IHttpExceptionHandler;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.cell.protocol.CommandContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-27 21:59
 */
public abstract class SpringBaseHttpController
{
    private IHttpExceptionHandler exceptionHandler;

    private IHttpCommandDispatcher dispatcher;

    protected abstract void initDispatcher(IHttpCommandDispatcher dispatcher);

    @AutoPlugin
    public void setDispacther(IHttpCommandDispatcher dispatcher) throws Exception
    {
        this.dispatcher = dispatcher;
        try
        {
            initDispatcher(dispatcher);
        } catch (Throwable e)
        {
            LOG.error(Module.HTTP_FRAMEWORK, e, "init dispacther fail");
            throw e;
        }
    }


    @RequestMapping("/{command}")
    @ResponseBody
    public DeferredResult<Object> request(HttpServletRequest request, HttpServletResponse response,
                                          @PathVariable("command") String command)
    {
        try
        {
            DeferredResult<Object> res = this.execute(request, response, command);
            return res;
        } catch (Throwable e)
        {
            LOG.error(Module.HTTP_FRAMEWORK, e, "controller request handle fail command id %s", command);
            return this.exceptionHandler.handle(response, e);
        }
    }

    private DeferredResult<Object> execute(HttpServletRequest request, HttpServletResponse response, String command) throws HttpFramkeworkException
    {

        if (!this.dispatcher.ready())
        {
            try
            {
                this.deny(response);
                return null;
            } catch (Throwable e)
            {
                return null;
            }
        }

        long resultTimeout = this.getResultTimeout();
        DeferredResult<Object> result = new DeferredResult<>(resultTimeout);
        CommandContext context = new CommandContext(request, response, result, command);
        this.dispatcher.dispath(context);
        return result;
    }

    private void deny(HttpServletResponse response) throws IOException
    {
        response.addHeader("code", String.valueOf(HttpConstants.HTTP_ARCHIVE_NOT_READY));
        response.addHeader("msg", "not ready");
        response.sendError(HttpStatus.OK.value());
    }

    public long getResultTimeout()
    {
        return HttpConstants.DEFAULT_RESULT_TIME_OUT;
    }
}
