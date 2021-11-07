package com.cell.dispatcher;

import com.cell.channel.IChannel;
import com.cell.concurrent.base.EventLoopGroup;
import com.cell.http.gate.config.IInitOnce;
import com.cell.context.DispatchContext;
import com.cell.handler.IChainHandler;
import com.cell.handler.IHandler;
import com.cell.reactor.ICommandReactor;

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
