package com.cell.context;

import com.cell.command.IHttpCommand;
import com.cell.hook.IHttpCommandHook;
import com.cell.protocol.IContext;
import com.cell.reactor.IHttpReactor;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-30 15:31
 */
public interface IHttpContext extends IContext
{
    IHttpReactor getHttpReactor();
    HttpServletRequest getHttpRequest();

    DeferredResult<Object> getResult();

    String getURI();
}
