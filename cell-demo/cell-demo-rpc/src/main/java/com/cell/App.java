package com.cell;

import com.cell.rpc.client.base.framework.annotation.CellSpringHttpApplication;
import com.cell.grpc.server.framework.annotation.RPCServerCmdAnno;
import com.cell.rpc.client.base.framework.annotation.RPCServerReactorAnno;
import com.cell.cmd.impl.AbstractRPCServerCommand;
import com.cell.context.IRPCServerCommandContext;
import com.cell.reactor.abs.AbstractRPCServerReactor;
import org.springframework.boot.SpringApplication;

import java.io.IOException;

/**
 * Hello world!
 */
@CellSpringHttpApplication
public class App
{
    @RPCServerReactorAnno()
    public static class MyServerRPCReactor extends AbstractRPCServerReactor
    {
    }

    @RPCServerCmdAnno(protocol = "/demo/1.0.0", reactor = MyServerRPCReactor.class)
    public static class DemoRpcCommand1 extends AbstractRPCServerCommand
    {
        @Override
        protected void onExecute(IRPCServerCommandContext ctx, Object o) throws IOException
        {
            ctx.response(this.createResponseWp().ret("123").build());
        }
    }

    public static void main(String[] args)
    {
        SpringApplication.run(App.class, args);
    }
}
