package com.cell.exceptions;

import com.cell.context.IHttpCommandContext;

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
