package com.cell.http.framework.command.impl;

import com.cell.http.framework.annotation.HttpCmdAnno;
import com.cell.http.framework.context.IHttpCommandContext;

import java.io.IOException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-04 08:38
 */
@HttpCmdAnno(uri = "/dummy")
public class DummyHttpCommand extends AbstractHttpCommand
{
    @Override
    protected void onExecute(IHttpCommandContext ctx, Object bo) throws IOException
    {
        ctx.discard();
    }
}
