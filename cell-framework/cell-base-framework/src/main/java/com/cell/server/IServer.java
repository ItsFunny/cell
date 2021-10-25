package com.cell.server;

import com.cell.config.IInitOnce;
import com.cell.manager.ProcessManager;
import com.cell.protocol.IServerRequest;
import com.cell.protocol.IServerResponse;
import com.cell.proxy.IProxy;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 23:18
 */
public interface IServer extends IInitOnce
{
    void start();

    void shutdown();

    IProxy getProxy();

    void serve(IServerRequest request, IServerResponse response);

    void setSwitch(ProcessManager manager);
}