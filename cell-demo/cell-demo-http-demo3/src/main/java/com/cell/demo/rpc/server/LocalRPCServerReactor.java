package com.cell.demo.rpc.server;

import com.cell.base.core.serialize.DefaultSelfJsonSerialize;
import com.cell.grpc.server.framework.command.AbstractGRPCServerCommand;
import com.cell.rpc.common.annotation.RPCServerReactorAnno;
import com.cell.rpc.common.context.IRPCServerCommandContext;
import com.cell.rpc.server.base.framework.annotation.RPCServerCmdAnno;
import com.cell.rpc.server.base.framework.reactor.abs.AbstractRPCServerReactor;
import lombok.Data;

import java.io.IOException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-11 10:52
 */
@RPCServerReactorAnno
public class LocalRPCServerReactor extends AbstractRPCServerReactor
{

    @Data
    public static class ServerRPCResponseaa extends DefaultSelfJsonSerialize
    {
        private String addr = "localServer222";

    }

    @RPCServerCmdAnno(protocol = "/demo/1.0.0", reactor = LocalRPCServerReactor.class)
    public static class DemoRPCCmd1 extends AbstractGRPCServerCommand
    {
        @Override
        protected void onExecute(IRPCServerCommandContext ctx, Object o) throws IOException
        {
            ServerRPCResponseaa ret = new ServerRPCResponseaa();
            ctx.response(this.createResponseWp().ret(ret).build());
        }
    }

}
