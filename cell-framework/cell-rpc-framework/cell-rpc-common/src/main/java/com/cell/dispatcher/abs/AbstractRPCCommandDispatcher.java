package com.cell.dispatcher.abs;

import com.cell.channel.IChannel;
import com.cell.dispatcher.AbstractCommandDispatcher;
import com.cell.dispatcher.IRPCCommandDispatcher;
import com.cell.handler.IChainHandler;
import com.cell.handler.IHandler;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-21 20:53
 */
public abstract class AbstractRPCCommandDispatcher extends AbstractCommandDispatcher implements IRPCCommandDispatcher
{
    public AbstractRPCCommandDispatcher(IChannel<IHandler, IChainHandler> channel)
    {
        super(channel);
    }
}
