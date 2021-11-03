package com.cell.server;

import com.cell.proxy.IHttpProxy;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 23:32
 */
public interface IHttpServer extends IServer
{
    IHttpProxy getHttpProxy();
    DeferredResult<Object> request(HttpServletRequest request, HttpServletResponse response);

    boolean ready();

}