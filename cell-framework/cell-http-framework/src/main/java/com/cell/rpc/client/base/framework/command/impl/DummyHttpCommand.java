package com.cell.rpc.client.base.framework.command.impl;

import com.cell.rpc.client.base.framework.annotation.HttpCmdAnno;
import com.cell.context.IHttpCommandContext;

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
