package com.cell.dispatcher;

import com.cell.cmd.IRPCServerCommand;
import com.cell.reactor.IRPCServerReactor;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-21 17:01
 */
@Data
public class RPCServerCommandWrapper
{
    private IRPCServerReactor reactor;
    private Class<? extends IRPCServerCommand> cmd;
}
