package com.cell.services;

import com.cell.hooks.IChainExecutor;
import com.cell.hooks.IListChainExecutor;

import java.util.List;

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
