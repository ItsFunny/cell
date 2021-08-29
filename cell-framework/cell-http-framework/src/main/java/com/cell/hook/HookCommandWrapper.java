package com.cell.hook;

import com.cell.command.IHttpCommand;
import com.cell.protocol.CommandContext;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-28 11:41
 */
@Data
public class HookCommandWrapper
{
    private IHttpCommand cmd;

    private CommandContext ctx;

    private HttpCommandHookResult lastResult;
}
