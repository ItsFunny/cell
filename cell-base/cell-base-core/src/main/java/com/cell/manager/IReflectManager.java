package com.cell.manager;

import com.cell.hooks.IAutoReactorExecutor;
import com.cell.hooks.IChainExecutor;
import com.cell.hooks.IReactorExecutor;
import com.cell.services.Pipeline;

import java.util.Collection;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail 使用方法:
 * 1. 添加@Manager 注解  + 实现 IReflectManager 该接口
 * 2. 对应的managerNode 添加注解
 * 参考: hook / filter
 * @Attention:
 * @Date 创建时间：2021-08-28 13:38
 */
public interface IReflectManager<T extends IReactorExecutor, CHAIN_T extends IChainExecutor> extends IAutoReactorExecutor
{
    void invokeInterestNodes(Collection<Object> nodes);

    IReflectManager createOrDefault();

    Pipeline<T, CHAIN_T> pipeline();
}
