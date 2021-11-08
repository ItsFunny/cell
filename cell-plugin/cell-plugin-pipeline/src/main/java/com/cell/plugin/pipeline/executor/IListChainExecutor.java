package com.cell.plugin.pipeline.executor;



import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-19 20:58
 */
public interface IListChainExecutor<V> extends IChainExecutor<V>
{
    void setExecutors(List<? extends IReactorExecutor<V>> ts);
}
