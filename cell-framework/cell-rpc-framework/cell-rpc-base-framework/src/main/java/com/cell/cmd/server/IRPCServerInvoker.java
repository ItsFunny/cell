package com.cell.cmd.server;

import com.cell.cmd.IRPCInvoker;
import com.cell.context.IRPCContext;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-20 22:00
 */
public interface IRPCServerInvoker extends IRPCInvoker
{
    void execute(IRPCContext context);
}
