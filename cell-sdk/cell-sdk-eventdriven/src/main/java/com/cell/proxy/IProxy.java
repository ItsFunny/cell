package com.cell.proxy;

import com.cell.concurrent.base.Promise;
import com.cell.event.IProcessEvent;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 16:16
 */
public interface IProxy
{
    void proxy(IProcessEvent event, Promise<Object> promise);

    void proxy(IProcessEvent event);
}
