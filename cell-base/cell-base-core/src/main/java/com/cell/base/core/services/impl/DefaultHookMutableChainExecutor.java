package com.cell.base.core.services.impl;

import com.cell.base.core.hooks.IChainHook;
import com.cell.base.core.protocol.IContext;
import com.cell.plugin.pipeline.executor.IChainExecutor;
import com.cell.plugin.pipeline.executor.IChainExecutor;
import com.cell.plugin.pipeline.executor.IChainExecutor;
import com.cell.plugin.pipeline.executor.IReactorExecutor;
import com.cell.plugin.pipeline.executor.impl.BaseMutableChainExecutor;

import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-19 19:53
 */
public class DefaultHookMutableChainExecutor extends BaseMutableChainExecutor<IContext> implements IChainHook
{
    public DefaultHookMutableChainExecutor()
    {
    }

    public DefaultHookMutableChainExecutor(List<? extends IReactorExecutor<IContext>> iReactorExecutors)
    {
        super(iReactorExecutors);
    }

    public DefaultHookMutableChainExecutor(List<? extends IReactorExecutor<IContext>> iReactorExecutors, int index)
    {
        super(iReactorExecutors, index);
    }

    @Override
    protected IChainExecutor childNewExecutor(List<? extends IReactorExecutor<IContext>> iReactorExecutors, int index)
    {
        return new DefaultHookMutableChainExecutor(executors, index);
    }
}
