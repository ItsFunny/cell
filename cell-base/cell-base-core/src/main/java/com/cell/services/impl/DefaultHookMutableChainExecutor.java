package com.cell.services.impl;

import com.cell.hooks.IChainExecutor;
import com.cell.hooks.IChainHook;
import com.cell.hooks.IHook;

import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-19 19:53
 */
public class DefaultHookMutableChainExecutor extends BaseMutableChainExecutor<IHook> implements IChainHook
{
    public DefaultHookMutableChainExecutor() {}

    public DefaultHookMutableChainExecutor(List<IHook> executors)
    {
        super(executors);
    }

    @Override
    protected IChainExecutor childNewExecutor(List<IHook> executors, int index)
    {
        return new DefaultHookMutableChainExecutor(executors, index);
    }

    public DefaultHookMutableChainExecutor(List<IHook> executors, int index)
    {
        super(executors, index);
    }
}
