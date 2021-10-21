package com.cell.dispatcher;

import com.cell.context.IRPCContext;
import com.cell.reacotr.IRPCReactor;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-21 18:36
 */
public interface IRPCCommandDispatcher
{
    void dispatch(IRPCContext context);

    void addReactor(IRPCReactor reactor);
}
