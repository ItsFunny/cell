package com.cell.protocol;

import com.cell.concurrent.promise.BasePromise;
import com.cell.reactor.ICommandReactor;

import java.io.IOException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-30 11:20
 */
public interface IContext
{
    void discard() throws IOException;

    void response(ContextResponseWrapper wp);

    long getRequestTimestamp();

    void setSequenceId(String sequenceId);

    String getSequenceId();


    BasePromise getPromise();

    void setReactor(ICommandReactor reactor);

    ICommandReactor getReactor();


    void setIp(String ip);

    String getIp();


    Object getParameter(String key);
}
