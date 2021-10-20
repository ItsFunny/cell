package com.cell.cmd.client;

import com.cell.cmd.IRPCInvoker;
import com.cell.context.IRPCContext;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-20 22:04
 */
public interface IRPCClientInvoker extends IRPCInvoker
{
    void call(IRPCContext context);
}
