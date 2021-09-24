package com.cell;

import com.cell.annotation.CellSpringHttpApplication;
import com.cell.annotations.*;
import com.cell.application.CellApplication;
import com.cell.command.IHttpCommand;
import com.cell.command.impl.AbstractHttpCommand;
import com.cell.context.IHttpCommandContext;
import com.cell.dispatcher.IHttpCommandDispatcher;
import com.cell.enums.EnumHttpRequestType;
import com.cell.reactor.IMapDynamicHttpReactor;
import com.cell.reactor.impl.AbstractHttpDymanicCommandReactor;
import com.cell.serialize.ISerializable;
import lombok.Data;
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
        protected void onExecute(IHttpCommandContext ctx, Object bo) throws IOException
        {
            Reactor1 reactor1 = (Reactor1) ctx.getReactor();
            Assert.notNull(reactor1.logic, "asd");
            ctx.response(this.createResponseWp().ret("cmd1").build());
        }
    }

    @HttpCmdAnno(uri = "/reactor2cmd1", httpCommandId = 1)
    public static class Reactor2CMD1 extends AbstractHttpCommand
    {
        @Override
        protected void onExecute(IHttpCommandContext ctx, Object bo) throws IOException
        {
            ctx.response(this.createResponseWp().ret("reactor2#cmd1").build());
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

    @ReactorAnno(group = "/reactor3")
    public static class Reactor3 extends AbstractHttpDymanicCommandReactor
    {

        @Override
        public List<Class<? extends IHttpCommand>> getHttpCommandList()
        {
            return Arrays.asList(cm3.class);
        }
    }

    @Data
    public static class Cmd3Buz
    {
        private String name;
        @Optional
        private Integer age;
    }

    @HttpCmdAnno(uri = "/cmd3", httpCommandId = 1, buzzClz = Cmd3Buz.class, requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class cm3 extends AbstractHttpCommand
    {
        @Override
        protected void onExecute(IHttpCommandContext ctx, Object bo) throws IOException
        {
            System.out.println(bo);
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
                }).make().get("/get", (wp) ->
        {
            wp.success("get");
        }).make().done().build().start(args);
    }
}
