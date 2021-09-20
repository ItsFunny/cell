package com.cell.executor;

import com.cell.hooks.IReactorExecutor;
import com.cell.protocol.ICommand;
import com.cell.protocol.IContext;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-20 08:02
 */
public interface IMetricsExecutor extends IReactorExecutor
{
    void recordRequest(ICommand cmd);
}
