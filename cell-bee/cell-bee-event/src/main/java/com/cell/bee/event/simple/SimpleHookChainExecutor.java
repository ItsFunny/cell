package com.cell.bee.event.simple;

import com.cell.base.core.events.IEvent;
import com.cell.executor.IChainExecutor;
import com.cell.executor.IReactorExecutor;
import com.cell.executor.impl.BaseMutableChainExecutor;

import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-08 10:14
 */
public class SimpleHookChainExecutor extends BaseMutableChainExecutor<IEvent> implements ISimpleChainEvent
{
    public SimpleHookChainExecutor()
    {

    }

    public SimpleHookChainExecutor(List<? extends IReactorExecutor<IEvent>> iReactorExecutors)
    {
        super(iReactorExecutors);
    }

    public SimpleHookChainExecutor(List<? extends IReactorExecutor<IEvent>> iReactorExecutors, int index)
    {
        super(iReactorExecutors, index);
    }

    @Override
    protected IChainExecutor childNewExecutor(List<? extends IReactorExecutor<IEvent>> iReactorExecutors, int index)
    {
        return new SimpleHookChainExecutor(executors, index);
    }
}
