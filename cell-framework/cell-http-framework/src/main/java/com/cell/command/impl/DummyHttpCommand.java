package com.cell.command.impl;

import com.cell.annotations.HttpCmdAnno;
import com.cell.context.IHttpCommandContext;
import com.cell.serialize.ISerializable;

import java.io.IOException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-04 08:38
 */
@HttpCmdAnno(uri = "/dummy", httpCommandId = Short.MAX_VALUE)
public class DummyHttpCommand extends AbstractHttpCommand
{
    @Override
    protected void onExecute(IHttpCommandContext ctx, Object bo) throws IOException
    {
        ctx.discard();
    }
}
