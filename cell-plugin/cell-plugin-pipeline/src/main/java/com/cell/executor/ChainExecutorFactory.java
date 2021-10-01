package com.cell.executor;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-19 20:47
 */
public interface ChainExecutorFactory<CT extends IListChainExecutor>
{
    CT createNewInstance();
}
