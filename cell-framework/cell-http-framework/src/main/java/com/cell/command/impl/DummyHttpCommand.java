package com.cell.command.impl;

import com.cell.annotations.HttpCmdAnno;
import com.cell.context.IHttpContext;
import com.cell.protocol.ICommandExecuteResult;
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
    protected ICommandExecuteResult onExecute(IHttpContext ctx, ISerializable bo) throws IOException
    {
        ctx.discard();
        return null;
    }
}
