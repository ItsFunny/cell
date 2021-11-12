package com.cell.demo.common;

import com.cell.base.core.annotations.AutoPlugin;
import com.cell.base.core.serialize.DefaultSelfJsonSerialize;
import com.cell.grpc.server.framework.command.AbstractGRPCServerCommand;
import com.cell.rpc.common.annotation.RPCServerReactorAnno;
import com.cell.rpc.common.context.IRPCServerCommandContext;
import com.cell.rpc.server.base.framework.annotation.RPCServerCmdAnno;
import com.cell.rpc.server.base.framework.reactor.abs.AbstractRPCServerReactor;
import lombok.Data;

import java.io.IOException;

/**
 * Hello world!
 */
public class Demo
{

    @RPCServerReactorAnno
    public static class DemoRPCServerReactor1 extends AbstractRPCServerReactor
    {
        @AutoPlugin
        private DemoServiceImpl demoService;

        public DemoServiceImpl getDemoService()
        {
            return demoService;
        }

        public void setDemoService(DemoServiceImpl demoService)
        {
            this.demoService = demoService;
        }
    }


    @RPCServerCmdAnno(protocol = "/demo/1.0.0", reactor = Demo.DemoRPCServerReactor1.class)
    public static class DemoRPCCmd1 extends AbstractGRPCServerCommand
    {
        @Override
        protected void onExecute(IRPCServerCommandContext ctx, Object o) throws IOException
        {
            Demo.ServerRPCResponseaa ret = new Demo.ServerRPCResponseaa();
            Demo.DemoRPCServerReactor1 r = (Demo.DemoRPCServerReactor1) ctx.getReactor();
            String name = r.getDemoService().getRpcName();
            ret.decorateAddr(name);
            ctx.response(this.createResponseWp().ret(ret).build());
        }
    }

    @Data
    public static class ServerRPCResponseaa extends DefaultSelfJsonSerialize
    {
        private String addr = "zhejiang";

        public void decorateAddr(String name)
        {
            this.addr += "_grpcName:" + name;
        }
    }
}
