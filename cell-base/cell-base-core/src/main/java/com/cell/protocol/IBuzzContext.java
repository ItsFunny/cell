package com.cell.protocol;

import com.cell.concurrent.base.EventExecutor;
import com.cell.concurrent.base.Promise;
import com.cell.reactor.ICommandReactor;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-30 11:20
 */
public interface IBuzzContext extends IContext
{
    CommandContext getCommandContext();
    void setEventExecutor(EventExecutor executor);
    EventExecutor getEventExecutor();
    void response(ContextResponseWrapper wp);
    long getRequestTimestamp();
    void setSequenceId(String sequenceId);
    String getSequenceId();
    Promise<Object> getPromise();
    ICommandReactor getReactor();

    void setIp(String ip);
    String getIp();
}
