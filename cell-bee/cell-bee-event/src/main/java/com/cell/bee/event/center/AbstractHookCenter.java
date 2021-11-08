package com.cell.bee.event.center;

import com.cell.executor.ChainExecutorFactory;
import com.cell.executor.IListChainExecutor;
import com.cell.hooks.IChainHook;
import com.cell.hooks.IHook;
import com.cell.manager.AbstractReflectManager;
import com.cell.protocol.IContext;
import com.cell.services.impl.DefaultHookMutableChainExecutor;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-17 19:44
 */
public abstract class AbstractHookCenter extends AbstractReflectManager<IHook, IChainHook, IContext>
{
    public static final String GROUP_EVENT_CENTER = "GROUP_EVENT_CENTER";

    @Override
    protected ChainExecutorFactory<? extends IListChainExecutor> factory()
    {
        return () ->
                new DefaultHookMutableChainExecutor();
    }


    protected abstract void afterInvoke();
}
