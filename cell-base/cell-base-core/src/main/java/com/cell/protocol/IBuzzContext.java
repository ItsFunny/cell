package com.cell.protocol;

import com.cell.concurrent.promise.BasePromise;
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
    void response(ContextResponseWrapper wp);
//    boolean success();

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
