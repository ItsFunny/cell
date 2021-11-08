package com.cell.base.core.services;

import com.cell.executor.IListChainExecutor;

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
