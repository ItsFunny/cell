package com.cell.http.framework.manager.context;

import com.cell.base.core.protocol.IContext;
import com.cell.http.framework.reactor.IHttpReactor;
import com.cell.rpc.grpc.client.framework.annotation.HttpCmdAnno;
import com.cell.http.framework.command.IHttpCommand;
import lombok.Builder;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-24 05:10
 */
@Data
@Builder
public class OnAddReactorContext extends IContext.EmptyContext
{
    private HttpCmdAnno anno;
    private Class<? extends IHttpCommand> cmd;
    private IHttpReactor reactor;
    private boolean satisfy;
}
