package com.cell.exceptions.impl;

import com.cell.annotations.ActivePlugin;
import com.cell.constants.ContextConstants;
import com.cell.context.HttpContextResponseBody;
import com.cell.context.IHttpCommandContext;
import com.cell.exception.HttpFramkeworkException;
import com.cell.exceptions.IHttpExceptionResolver;
import com.cell.protocol.ContextResponseWrapper;
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
