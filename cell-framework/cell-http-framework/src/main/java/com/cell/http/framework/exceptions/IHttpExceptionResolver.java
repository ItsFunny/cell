package com.cell.http.framework.exceptions;

import com.cell.http.framework.context.IHttpCommandContext;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-20 06:10
 */
public interface IHttpExceptionResolver
{
    void exceptionCaught(Throwable e, IHttpCommandContext commandContext);
}
