package com.cell.http.framework.hook;

import com.cell.http.framework.context.IHttpCommandContext;
import com.cell.http.framework.event.IHttpEvent;
import com.cell.http.framework.reactor.IHttpReactor;
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
public class HookCommandWrapper implements IHttpEvent
{
    private IHttpReactor reactor;
    private IHttpCommandContext context;
    private HttpCommandHookResult lastResult;
}
