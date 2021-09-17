package com.cell.hooks;

import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-19 20:58
 */
public interface IListChainExecutor<T> extends IChainExecutor
{
    void setExecutors(List<T> ts);
}
