package com.cell.gprc.server;

import com.cell.context.IRPCServerCommandContext;
import com.cell.grpc.server.framework.command.AbstractGRPCServerCommand;
import com.cell.reactor.abs.AbstractRPCServerReactor;
import com.cell.rpc.grpc.client.framework.annotation.CellSpringHttpApplication;
import com.cell.rpc.grpc.client.framework.annotation.RPCServerReactorAnno;
import com.cell.rpc.server.base.annotation.RPCServerCmdAnno;
import org.springframework.boot.SpringApplication;

import java.io.IOException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-05 15:31
 */
@CellSpringHttpApplication
public class App
{
    @RPCServerReactorAnno
    public static class DemoRPCServerReactor1 extends AbstractRPCServerReactor
    {

    }

    @RPCServerCmdAnno(protocol = "/demo/2.0.0", reactor = DemoRPCServerReactor1.class)
    public static class DemoRPCCmd1 extends AbstractGRPCServerCommand
    {
        @Override
        protected void onExecute(IRPCServerCommandContext ctx, Object o) throws IOException
        {
        }
    }

    public static void main(String[] args)
    {
        try
        {
            SpringApplication.run(App.class, args);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
