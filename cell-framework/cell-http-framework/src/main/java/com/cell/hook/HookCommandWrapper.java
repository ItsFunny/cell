package com.cell.hook;

import com.cell.command.IHttpCommand;
import com.cell.context.IHttpContext;
import com.cell.protocol.CommandContext;
import com.cell.protocol.IContext;
import com.cell.reactor.IHttpReactor;
import com.cell.reactor.IReactor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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
    private IHttpReactor reactor;
    private IHttpContext context;
    private HttpCommandHookResult lastResult;
}
