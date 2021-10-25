package com.cell.command;

import com.cell.annotation.CellSpringHttpApplication;
import com.cell.annotation.RPCServerCmdAnno;
import com.cell.annotation.RPCServerReactorAnno;
import com.cell.cmd.impl.AbstractRPCServerCommand;
import com.cell.context.IRPCServerCommandContext;
import com.cell.reactor.abs.AbstractRPCServerReactor;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;


@CellSpringHttpApplication
public class AbstractGRPCServerCommandTest
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
        ConfigurableApplicationContext run = SpringApplication.run(AbstractGRPCServerCommandTest.class, args);
    }
}