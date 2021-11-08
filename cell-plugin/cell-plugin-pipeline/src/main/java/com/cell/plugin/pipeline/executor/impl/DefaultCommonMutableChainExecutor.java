package com.cell.plugin.pipeline.executor.impl;

import com.cell.plugin.pipeline.executor.IChainExecutor;
import com.cell.plugin.pipeline.executor.IReactorExecutor;


import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-20 09:52
 */
public class DefaultCommonMutableChainExecutor<V> extends BaseMutableChainExecutor<V>
{
    public DefaultCommonMutableChainExecutor()
    {
    }

    public DefaultCommonMutableChainExecutor(List<? extends IReactorExecutor<V>> executors)
    {
        super(executors);
    }

    public DefaultCommonMutableChainExecutor(List<? extends IReactorExecutor<V>> executors, int index)
    {
        super(executors, index);
    }

    @Override
    protected IChainExecutor childNewExecutor(List<? extends IReactorExecutor<V>> executors, int index)
    {
        return new DefaultCommonMutableChainExecutor(executors, index);
    }
}
