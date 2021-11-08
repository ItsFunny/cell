package com.cell.proxy;

import com.cell.base.common.exceptions.ProgramaException;
import com.cell.concurrent.base.Promise;
import com.cell.event.IProcessEvent;
import com.cell.log.LOG;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 19:08
 */
public abstract class AbstractProxy implements IProxy
{
    @Override
    public void proxy(IProcessEvent event, Promise<Object> promise)
    {
        try
        {
            this.onProxy(event, promise);
        } catch (Exception e)
        {
            LOG.error("proxy 失败", e);
            if (!promise.isDone())
            {
                promise.tryFailure(e);
            }
        } finally
        {
            if (promise.isDone())
            {
                return;
            }
            promise.tryFailure(new ProgramaException("asd"));
        }
    }

    protected abstract void onProxy(IProcessEvent event, Promise<Object> promise);
}
