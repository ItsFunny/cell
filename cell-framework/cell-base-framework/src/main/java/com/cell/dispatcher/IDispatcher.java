package com.cell.dispatcher;

import com.cell.base.common.context.IInitOnce;
import com.cell.base.core.channel.IChannel;
import com.cell.base.core.concurrent.base.EventLoopGroup;
import com.cell.context.DispatchContext;
import com.cell.base.core.handler.IChainHandler;
import com.cell.base.core.handler.IHandler;
import com.cell.base.core.reactor.ICommandReactor;

import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 18:39
 */
public interface IDispatcher extends IInitOnce
{
    void dispatch(DispatchContext context);

    void addReactor(ICommandReactor reactor);

    void setChannel(IChannel<IHandler, IChainHandler> channel);

    List<? extends ICommandReactor> getReactors();

    void setEventGroup(EventLoopGroup eventLoopGroup);
}
