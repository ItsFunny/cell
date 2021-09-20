package com.cell.services.impl;

import com.cell.hooks.*;

import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-20 09:52
 */
public class DefaultCommonMutableChainExecutor extends BaseMutableChainExecutor<IReactorExecutor>
{

    public DefaultCommonMutableChainExecutor()
    {
    }

    public DefaultCommonMutableChainExecutor(List<IReactorExecutor> executors)
    {
        super(executors);
    }

    public DefaultCommonMutableChainExecutor(List<IReactorExecutor> executors, int index)
    {
        super(executors, index);
    }

    @Override
    protected IChainExecutor childNewExecutor(List<IReactorExecutor> executors, int index)
    {
        return new DefaultCommonMutableChainExecutor(executors, index);
    }
}
