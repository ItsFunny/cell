package com.cell.manager;

import com.cell.base.common.utils.CollectionUtils;
import com.cell.executor.ChainExecutorFactory;
import com.cell.executor.IChainExecutor;
import com.cell.executor.IListChainExecutor;
import com.cell.executor.IReactorExecutor;
import com.cell.pipeline.DefaultPipeline;
import com.cell.pipeline.Pipeline;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.util.Collection;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-28 13:11
 */
@Data
public abstract class AbstractReflectManager<T extends IReactorExecutor<V>, CHAIN_T extends IChainExecutor<V>, V> implements IReflectManager<T, CHAIN_T, V>
{
    private boolean setted;

    protected Pipeline<T, CHAIN_T> pipeline;

    public AbstractReflectManager()
    {
        this.pipeline = new DefaultPipeline<T, CHAIN_T, V>(this.factory());
    }

    @Override
    public void invokeInterestNodes(Collection<Object> nodes)
    {
        if (this.setted || CollectionUtils.isEmpty(nodes)) return;
        this.onInvokeInterestNodes(nodes);
        this.setted = true;
    }

    protected abstract ChainExecutorFactory<? extends IListChainExecutor> factory();

    public Pipeline<T, CHAIN_T> pipeline()
    {
        return this.pipeline;
    }

    protected void onInvokeInterestNodes(Collection<Object> nodes)
    {
        for (Object node : nodes)
        {
            try
            {
                T t = (T) node;
                this.pipeline.addFirst(node.getClass().getName(), t);
            } catch (ClassCastException e)
            {

            }
        }
    }

    public Mono<Void> execute(V v)
    {
        return this.pipeline.chainExecutor().execute(v);
    }


}
