package com.cell.dispatcher;

import com.cell.exception.HttpFramkeworkException;
import com.cell.protocol.CommandContext;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-27 23:00
 */
public class DefaultHttpCommandDispatcher implements  IHttpCommandDispatcher
{

    @Override
    public short getPort()
    {
        return 0;
    }

    @Override
    public void setPort(short port)
    {

    }

    @Override
    public void dispath(CommandContext ctx) throws HttpFramkeworkException
    {

    }
}
