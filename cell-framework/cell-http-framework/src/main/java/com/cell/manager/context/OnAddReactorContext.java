package com.cell.manager.context;

import com.cell.base.common.annotation.HttpCmdAnno;
import com.cell.grpc.server.framework.command.IHttpCommand;
import com.cell.protocol.IContext;
import com.cell.reactor.IHttpReactor;
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
