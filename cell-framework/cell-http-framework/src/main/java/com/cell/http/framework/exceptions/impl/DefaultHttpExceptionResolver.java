package com.cell.http.framework.exceptions.impl;

import com.cell.base.core.annotations.ActivePlugin;
import com.cell.base.core.constants.ContextConstants;
import com.cell.base.core.protocol.ContextResponseWrapper;
import com.cell.http.framework.context.HttpContextResponseBody;
import com.cell.http.framework.context.IHttpCommandContext;
import com.cell.http.framework.exceptions.IHttpExceptionResolver;
import org.springframework.http.HttpStatus;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-20 06:11
 */
@ActivePlugin
public class DefaultHttpExceptionResolver implements IHttpExceptionResolver
{
    @Override
    public void exceptionCaught(Throwable e, IHttpCommandContext ctx)
    {
        ctx.response(ContextResponseWrapper.builder()
                .status(ContextConstants.FAIL)
                .other(HttpContextResponseBody.builder().status(HttpStatus.BAD_REQUEST).build())
                .exception(e)
                .msg("error:" + e.getMessage())
                .ret(e.getMessage())
                .build());
    }
}
