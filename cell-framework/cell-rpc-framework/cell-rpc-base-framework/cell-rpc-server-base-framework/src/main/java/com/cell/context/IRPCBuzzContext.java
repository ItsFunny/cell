package com.cell.context;

import com.cell.cmd.IRPCServerCommand;
import com.cell.base.core.protocol.IBuzzContext;

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
