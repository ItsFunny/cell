package com.cell.dispatcher;

import com.cell.exception.HttpFramkeworkException;
import com.cell.protocol.CommandContext;
import com.cell.reactor.IHttpReactor;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-27 22:56
 */
public interface IHttpCommandDispatcher
{
    short getPort();

    void setPort(short port);

    void dispath(CommandContext ctx) throws HttpFramkeworkException;

    void addReactor(IHttpReactor reactor);

    boolean ready();
}
