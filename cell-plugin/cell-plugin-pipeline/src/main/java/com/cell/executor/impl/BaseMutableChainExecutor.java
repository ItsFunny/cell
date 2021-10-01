package com.cell.executor.impl;

import com.cell.executor.IChainExecutor;
import com.cell.executor.IListChainExecutor;
import com.cell.executor.IReactorExecutor;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-19 15:45
 */
public abstract class BaseMutableChainExecutor<V> implements IListChainExecutor<V>
{
    protected List<? extends IReactorExecutor<V>> executors;
    protected int index;

    public BaseMutableChainExecutor()
    {

    }

    public BaseMutableChainExecutor(List<? extends IReactorExecutor<V>> executors)
    {
        this.executors = executors;
        this.index = 0;
    }

    public BaseMutableChainExecutor(List<? extends IReactorExecutor<V>> executors, int index)
    {
        this.executors = executors;
        this.index = index;
    }

    @Override
    public void setExecutors(List<? extends IReactorExecutor<V>> ts)
    {
        this.executors = ts;
    }


    @Override
    public Mono<Void> execute(V v)
    {
        return Mono.defer(() ->
        {
            boolean find = false;
            if (this.index < this.executors.size())
            {
                IReactorExecutor<V> h = null;
                for (; this.index < this.executors.size(); this.index++)
                {

                    h = this.executors.get(this.index);
                    if (h.predict(v))
                    {
                        find = true;
                        break;
                    }
                }
                if (!find) return Mono.empty();
                IChainExecutor hh = this.childNewExecutor(this.executors, this.index + 1);
                return h.execute(v, hh);
            } else
            {
                return Mono.empty();
            }
        });
    }


    protected abstract IChainExecutor childNewExecutor(List<? extends IReactorExecutor<V>> executors, int index);
}
