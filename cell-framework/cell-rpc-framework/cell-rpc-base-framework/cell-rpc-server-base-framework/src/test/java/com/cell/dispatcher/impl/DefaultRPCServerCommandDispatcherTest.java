package com.cell.dispatcher.impl;

import com.cell.annotation.RPCServerCmdAnno;
import com.cell.annotation.RPCServerReactorAnno;
import com.cell.channel.DefaultRPCServerChannel;
import com.cell.channel.IRPCChannel;
import com.cell.cmd.impl.AbstractRPCServerCommand;
import com.cell.context.IRPCServerCommandContext;
import com.cell.handler.DefaultRPCServerLogicHandler;
import com.cell.manager.IReflectManager;
import com.cell.manager.RPCHandlerManager;
import com.cell.reactor.abs.AbstractRPCServerReactor;
import org.junit.Test;

import java.util.Arrays;

public class DefaultRPCServerCommandDispatcherTest
{
    @Test
    public void addReactor()
    {
    }

    @RPCServerReactorAnno(cmds = {MyServerRPCCOmmand.class})
    public static class MyServerRPCReactor extends AbstractRPCServerReactor
    {

    }

    @RPCServerCmdAnno(func = "/demo")
    public static class MyServerRPCCOmmand extends AbstractRPCServerCommand
    {
        @Override
        protected void onExecute(IRPCServerCommandContext ctx, Object bo)
        {
            System.out.println(123);
            ctx.response(this.createResponseWp().ret("123").build());
        }
    }


    @Test
    public void addReactorTest()
    {
        IRPCChannel channel = new DefaultRPCServerChannel();
        DefaultRPCServerCommandDispatcher dispatcher = new DefaultRPCServerCommandDispatcher();
        dispatcher.setRpcChannel(channel);
        MyServerRPCReactor reactor = new MyServerRPCReactor();
        dispatcher.addReactor(reactor);
    }

    @Test
    public void rpcCmdTest()
    {
        IRPCChannel channel = new DefaultRPCServerChannel();
        DefaultRPCServerCommandDispatcher dispatcher = new DefaultRPCServerCommandDispatcher();
        dispatcher.setRpcChannel(channel);
        MyServerRPCReactor reactor = new MyServerRPCReactor();
        dispatcher.addReactor(reactor);

        RPCHandlerManager m = new RPCHandlerManager();
        IReflectManager instance = m.createOrDefault();
        instance.invokeInterestNodes(Arrays.asList(new DefaultRPCServerLogicHandler()));


        dispatcher.dispatch();

    }
}