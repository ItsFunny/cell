package com.cell.context;

import com.cell.grpc.server.framework.command.IHttpCommand;
import com.cell.protocol.CommandContext;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-30 11:36
 */
@Data
public class DefaultHttpCommandContext extends AbstractHttpCommandContext
{
    public DefaultHttpCommandContext(CommandContext commandContext)
    {
        super(commandContext);
    }
}
