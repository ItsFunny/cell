package com.cell.suit;

import com.cell.channel.IChannel;
import com.cell.context.IRPCServerCommandContext;
import com.cell.context.RPCServerCommandContext;
import com.cell.context.impl.DefaultRPCServerCommandContext;
import com.cell.handler.IChainHandler;
import com.cell.handler.IHandler;
import com.cell.services.impl.AbstractHandlerSuit;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-21 18:29
 */
public class DefaultServerRPCCommandSuit extends AbstractHandlerSuit implements IRPCHandlerSuit
{
    private RPCServerCommandContext ctx;

    public DefaultServerRPCCommandSuit(IChannel<IHandler, IChainHandler> channel, RPCServerCommandContext ctx)
    {
        super(channel);
        this.ctx = ctx;
    }


    @Override
    public void discard()
    {

    }

    @Override
    public IRPCServerCommandContext getBuzContext()
    {
        return new DefaultRPCServerCommandContext(this.ctx);
    }
}
