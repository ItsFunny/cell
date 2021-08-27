package com.cell.handler;

import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-27 22:23
 */
public interface IHttpExceptionHandler<T>
{
    DeferredResult<T> handle(HttpServletResponse response,Throwable e);
}
