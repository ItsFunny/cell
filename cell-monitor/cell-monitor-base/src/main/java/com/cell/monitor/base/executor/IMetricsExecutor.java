package com.cell.monitor.base.executor;


import com.cell.base.core.protocol.IContext;
import com.cell.plugin.pipeline.executor.IChainExecutor;
import com.cell.plugin.pipeline.executor.IReactorExecutor;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-20 08:02
 */
public interface IMetricsExecutor extends IReactorExecutor<IContext>
{
    String recordRequest = "recordRequest";

    IReactorExecutor<IContext> recordRequest();
}
