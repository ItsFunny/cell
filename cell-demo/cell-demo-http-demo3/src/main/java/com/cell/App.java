package com.cell;

import com.cell.annotation.CellSpringHttpApplication;
import com.cell.annotations.*;
import com.cell.application.CellApplication;
import com.cell.command.IHttpCommand;
import com.cell.command.impl.AbstractHttpCommand;
import com.cell.context.IHttpContext;
import com.cell.dispatcher.IHttpCommandDispatcher;
import com.cell.protocol.ICommandExecuteResult;
import com.cell.reactor.IMapDynamicHttpReactor;
import com.cell.reactor.impl.AbstractHttpDymanicCommandReactor;
import com.cell.serialize.ISerializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description 混合的api接口实现
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-04 06:09
 */
@CellSpringHttpApplication
public class App
{
    public static class CC1
    {

    }

    @ActivePlugin
    public static class CC2
    {
        @Plugin
        public CC3 cc3()
        {
            return new CC3();
        }
    }

    public static class CC3
    {

    }

    @Bean
    public CC1 cc()
    {
        return new CC1();
    }


    @HttpCmdAnno(uri = "/cmd1", httpCommandId = 1)
    public static class Reactor1Cmd1 extends AbstractHttpCommand
    {
        @Override
        protected ICommandExecuteResult onExecute(IHttpContext ctx, ISerializable bo) throws IOException
        {
            Reactor1 reactor1 = (Reactor1) ctx.getReactor();
            Assert.notNull(reactor1.logic, "asd");
            ctx.response(this.createResponseWp().ret("cmd1").build());
            return null;
        }
    }

    @HttpCmdAnno(uri = "/reactor2cmd1", httpCommandId = 1)
    public static class Reactor2CMD1 extends AbstractHttpCommand
    {

        @Override
        protected ICommandExecuteResult onExecute(IHttpContext ctx, ISerializable bo) throws IOException
        {
            ctx.response(this.createResponseWp().ret("reactor2#cmd1").build());
            return null;
        }
    }

    @ActivePlugin
    public static class LogicImpl
    {

    }

    @ReactorAnno(group = "/reactor1")
    public static class Reactor1 extends AbstractHttpDymanicCommandReactor
    {
        @AutoPlugin
        private IHttpCommandDispatcher commandDispatcher;
        @AutoPlugin
        private LogicImpl logic;

        @Override
        public List<Class<? extends IHttpCommand>> getHttpCommandList()
        {
            return Arrays.asList(Reactor1Cmd1.class);
        }
    }

    public static class Reactor2 extends AbstractHttpDymanicCommandReactor
    {
        @Override
        public List<Class<? extends IHttpCommand>> getHttpCommandList()
        {
            return Arrays.asList(Reactor2CMD1.class);
        }
    }

    public static void main(String[] args)
    {
        CellApplication.builder(App.class)
                .withReactor(new Reactor2())
                .newReactor()
                .withBean(CC1.class)
                .withBean(CC2.class)
                .withGroup("/demo")
                .post("/cc1", (wp) ->
                {
                    IMapDynamicHttpReactor reactor = wp.getReactor();
                    CC1 cc1 = (CC1) reactor.getDependency(CC1.class);
                    Assert.notNull(cc1, "bean cc1不可为空");
                    wp.success("post");
                    return null;
                }).make().get("/get", (wp) ->
        {
            wp.success("get");
            return null;
        }).make().done().build().start( args);
    }
}
