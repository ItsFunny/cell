package com.cell.gprc.server;

import com.cell.context.IHttpCommandContext;
import com.cell.context.IRPCServerCommandContext;
import com.cell.grpc.server.framework.command.AbstractGRPCServerCommand;
import com.cell.reactor.abs.AbstractRPCServerReactor;
import com.cell.reactor.impl.AbstractHttpDymanicCommandReactor;
import com.cell.rpc.grpc.client.framework.annotation.CellSpringHttpApplication;
import com.cell.rpc.grpc.client.framework.annotation.HttpCmdAnno;
import com.cell.rpc.grpc.client.framework.annotation.RPCServerReactorAnno;
import com.cell.rpc.grpc.client.framework.command.impl.AbstractHttpCommand;
import com.cell.rpc.server.base.annotation.RPCServerCmdAnno;
import com.cell.serialize.DefaultSelfJsonSerialize;
import lombok.Data;
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

    @HttpCmdAnno(uri = "/demo")
    public static class AAA extends AbstractHttpCommand
    {
        @Override
        protected void onExecute(IHttpCommandContext ctx, Object o) throws IOException
        {
            ctx.response(this.createResponseWp().ret("123").build());
        }
    }

    @RPCServerCmdAnno(protocol = "/demo/2.0.0", reactor = DemoRPCServerReactor1.class)
    public static class DemoRPCCmd2 extends AbstractGRPCServerCommand
    {
        @Override
        protected void onExecute(IRPCServerCommandContext ctx, Object o) throws IOException
        {
            ctx.response(this.createResponseWp().ret("xxxx").build());
        }
    }

    @Data
    public static class ServerRPCResponseaa extends DefaultSelfJsonSerialize
    {
        private String addr = "zhejiang222";

    }

    @RPCServerCmdAnno(protocol = "/demo/1.0.0", reactor = DemoRPCServerReactor1.class)
    public static class DemoRPCCmd1 extends AbstractGRPCServerCommand
    {
        @Override
        protected void onExecute(IRPCServerCommandContext ctx, Object o) throws IOException
        {
            ServerRPCResponseaa ret = new ServerRPCResponseaa();
            ctx.response(this.createResponseWp().ret(ret).build());
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
