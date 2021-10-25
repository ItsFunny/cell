package com.cell.command;

import com.cell.annotation.RPCServerCmdAnno;
import com.cell.cmd.impl.AbstractRPCServerCommand;
import com.cell.context.IRPCServerCommandContext;

import java.io.IOException;

import static org.junit.Assert.*;


public class AbstractGRPCServerCommandTest
{
    @RPCServerCmdAnno(protocol = "/demo/1.0.0")
    public static class DemoRpcCommand1 extends AbstractRPCServerCommand
    {
        @Override
        protected void onExecute(IRPCServerCommandContext ctx, Object o) throws IOException
        {
            ctx.response(this.createResponseWp().ret("123").build());
        }
    }

}