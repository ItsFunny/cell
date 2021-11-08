package com.cell.rpc.server.base.framework.context;

import com.cell.base.core.protocol.IBuzzContext;
import com.cell.rpc.common.cmd.IRPCServerCommand;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-21 22:32
 */
public interface IRPCBuzzContext extends IBuzzContext
{
    Class<? extends IRPCServerCommand> getCommand();
}
