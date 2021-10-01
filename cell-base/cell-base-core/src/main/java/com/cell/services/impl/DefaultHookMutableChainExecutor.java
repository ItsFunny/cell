package com.cell.services.impl;

import com.cell.executor.IChainExecutor;
import com.cell.executor.IReactorExecutor;
import com.cell.executor.impl.BaseMutableChainExecutor;
import com.cell.hooks.IChainHook;
import com.cell.protocol.IContext;

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
