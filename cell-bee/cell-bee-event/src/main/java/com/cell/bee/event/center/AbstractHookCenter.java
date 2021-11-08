package com.cell.bee.event.center;

import com.cell.base.core.hooks.IChainHook;
import com.cell.base.core.hooks.IHook;
import com.cell.base.core.protocol.IContext;
import com.cell.base.core.services.impl.DefaultHookMutableChainExecutor;
import com.cell.plugin.pipeline.executor.ChainExecutorFactory;
import com.cell.plugin.pipeline.executor.IListChainExecutor;
import com.cell.plugin.pipeline.manager.AbstractReflectManager;

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
