package com.cell.center;

import com.cell.hooks.IChainExecutor;
import com.cell.hooks.IListChainExecutor;
import com.cell.hooks.IReactorExecutor;
import com.cell.manager.IReflectManager;
import com.cell.protocol.IContext;
import com.cell.services.ChainExecutorFactory;
import com.cell.services.Pipeline;
import com.cell.services.impl.DefaultPipeline;
import com.cell.utils.CollectionUtils;
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
public abstract class AbstractReflectManager<T extends IReactorExecutor, CHAIN_T extends IChainExecutor> implements IReflectManager<T, CHAIN_T>
{
    private boolean setted;

    protected Pipeline<T, CHAIN_T> pipeline;

    public AbstractReflectManager()
    {
        this.pipeline = new DefaultPipeline<>(this.factory());
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

    public Mono<Void> execute(IContext context)
    {
        return this.pipeline.chainExecutor().execute(context);
    }
}
