package com.cell.dispatcher;

import com.cell.exception.HttpFramkeworkException;
import com.cell.protocol.CommandContext;
import com.cell.reactor.IHttpReactor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-27 22:56
 */
public interface IHttpCommandDispatcher
{
    short getPort();

    void setPort(short port);

    //    void handlRequestResponse(HttpServletRequest request, HttpServletResponse response)
//    void dispath(CommandContext ctx) throws HttpFramkeworkException;
    DeferredResult<Object> request(HttpServletRequest request, HttpServletResponse response) throws HttpFramkeworkException;

    void addReactor(IHttpReactor reactor);

    boolean ready();

   Collection<IHttpReactor> getReactors();
}
