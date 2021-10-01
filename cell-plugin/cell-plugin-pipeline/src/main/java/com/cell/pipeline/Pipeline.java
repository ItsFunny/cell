package com.cell.pipeline;

import com.cell.executor.IChainExecutor;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-18 19:54
 */
public interface Pipeline<T, E extends IChainExecutor>
{
    E chainExecutor();

    Pipeline addFirst(String name, T handler);

    Pipeline addLast(T... handlers);

    Pipeline remove(T handler);
}
