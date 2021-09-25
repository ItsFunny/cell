package com.cell.services.impl;

import com.cell.handler.IChainHandler;
import com.cell.handler.IHandler;
import com.cell.hooks.IChainExecutor;
import com.cell.hooks.IListChainExecutor;
import com.cell.hooks.IReactorExecutor;
import com.cell.protocol.IContext;
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
public abstract class BaseMutableChainExecutor<T extends IReactorExecutor> implements IListChainExecutor<T>
{
    protected List<T> executors;
    protected int index;

    public BaseMutableChainExecutor()
    {

    }

    public BaseMutableChainExecutor(List<T> executors)
    {
        this.executors = executors;
        this.index = 0;
    }

    public BaseMutableChainExecutor(List<T> executors, int index)
    {
        this.executors = executors;
        this.index = index;
    }

    @Override
    public void setExecutors(List<T> ts)
    {
        this.executors = ts;
    }


    @Override
    public Mono<Void> execute(IContext ctx)
    {
        return Mono.defer(() ->
        {
            boolean find = false;
            if (this.index < this.executors.size())
            {
                T h = null;
                for (; this.index < this.executors.size(); this.index++)
                {
                    h = this.executors.get(this.index);
                    if (h.predict(ctx))
                    {
                        find = true;
                        break;
                    }
                }
                if (!find) return Mono.empty();
                IChainExecutor hh = this.childNewExecutor(this.executors, this.index + 1);
                return h.execute(ctx, hh);
            } else
            {
                return Mono.empty();
            }
        });
    }

    protected abstract IChainExecutor childNewExecutor(List<T> executors, int index);
}
