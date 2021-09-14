package com.cell.manager;

import java.util.Collection;
import java.util.Set;

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
public interface IReflectManager
{
    void invokeInterestNodes(Collection<Object> nodes);

    IReflectManager createOrDefault();
}
