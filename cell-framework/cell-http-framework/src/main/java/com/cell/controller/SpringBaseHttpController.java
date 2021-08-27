package com.cell.controller;

import com.cell.constant.HttpConstants;
import com.cell.dispatcher.IHttpCommandDispatcher;
import com.cell.exception.HttpFramkeworkException;
import com.cell.handler.IHttpExceptionHandler;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.cell.protocol.CommandContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-27 21:59
 */
public class SpringBaseHttpController
{

    private IHttpExceptionHandler exceptionHandler;

    private IHttpCommandDispatcher dispatcher;


    @RequestMapping("/{command}")
    @ResponseBody
    public DeferredResult<Object> request(HttpServletRequest request, HttpServletResponse response,
                                          @PathVariable("command") String command)
    {
        try
        {
//            DeferredResult<Object> res = this.fireRead(request, response, command);
//            return res;
        } catch (Throwable e)
        {
//            response.setStatus(400);
            LOG.error(Module.HTTP_FRAMEWORK, e, "controller request handle fail command id %s", command);
            return this.exceptionHandler.handle(response, e);
        }
        return null;
    }

    private DeferredResult<Object> execute(HttpServletRequest request, HttpServletResponse response, String command) throws HttpFramkeworkException
    {
        long resultTimeout = this.getResultTimeout();
        DeferredResult<Object> result = new DeferredResult<>(resultTimeout);
        CommandContext context = new CommandContext(request, response, result, command);
        this.dispatcher.dispath(context);
        return result;
    }


    public long getResultTimeout()
    {
        return HttpConstants.DEFAULT_RESULT_TIME_OUT;
    }


}
