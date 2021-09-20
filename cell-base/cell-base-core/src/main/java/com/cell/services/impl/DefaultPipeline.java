package com.cell.services.impl;

import com.cell.hooks.IChainExecutor;
import com.cell.hooks.IListChainExecutor;
import com.cell.services.ChainExecutorFactory;
import com.cell.services.Pipeline;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-19 05:06
 */
@Data
public class DefaultPipeline<T, CHAIN_T extends IChainExecutor> implements Pipeline<T, CHAIN_T>
{
    private List<T> executors = new ArrayList<>();
    private ChainExecutorFactory<? extends IListChainExecutor> factory;

    public DefaultPipeline(ChainExecutorFactory<? extends IListChainExecutor> factory)
    {
        this.factory = factory;
    }

    public DefaultPipeline()
    {
        this.factory = () -> new DefaultCommonMutableChainExecutor();
    }

    @Override
    public CHAIN_T chainExecutor()
    {
        IListChainExecutor newInstance = factory.createNewInstance();
        newInstance.setExecutors(this.executors);
        return (CHAIN_T) newInstance;
    }

    @Override
    public final Pipeline addFirst(String name, T handler)
    {
        synchronized (this)
        {
            this.executors.add(handler);
        }
        return this;
    }

    @Override
    public Pipeline addLast(T... handlers)
    {
        for (T handler : handlers)
        {
            this.executors.add(handler);
        }
        return this;
    }

    @Override
    public Pipeline remove(T handler)
    {
        return null;
    }
}
