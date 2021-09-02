package com.cell.factory;

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
 * @Date 创建时间：2021-09-02 11:17
 */

@FunctionalInterface
public interface JsonHandlerBundle
{
    ICommandExecuteResult doExecuteDirectly(IHttpContext ctx, ISerializable bo) throws IOException;
}
