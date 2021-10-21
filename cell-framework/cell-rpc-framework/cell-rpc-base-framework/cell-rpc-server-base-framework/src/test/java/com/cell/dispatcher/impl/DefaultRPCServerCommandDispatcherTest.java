package com.cell.dispatcher.impl;

import com.cell.annotation.RPCServerCmdAnno;
import com.cell.annotation.RPCServerReactorAnno;
import com.cell.cmd.impl.AbstractRPCServerCommand;
import com.cell.context.IRPCServerCommandContext;
import com.cell.reactor.abs.AbstractRPCServerReactor;
import org.junit.Test;

public class DefaultRPCServerCommandDispatcherTest
{
    @Test
    public void addReactor()
    {
    }

    @RPCServerReactorAnno
    public static class MyServerRPCReactor extends AbstractRPCServerReactor{

    }

    @RPCServerCmdAnno(func = "/demo")
    public static class MyServerRPCCOmmand extends AbstractRPCServerCommand{
        @Override
        protected void onExecute(IRPCServerCommandContext ctx, Object bo)
        {

        }
    }

    @Test
    public void rpcCmdTest()
    {

    }
}