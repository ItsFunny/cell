package com.cell;

import com.cell.annotation.CellSpringHttpApplication;
import com.cell.annotations.HttpCmdAnno;
import com.cell.annotations.ReactorAnno;
import com.cell.application.CellApplication;
import com.cell.command.IHttpCommand;
import com.cell.command.impl.AbstractHttpCommand;
import com.cell.context.IHttpCommandContext;
import com.cell.reactor.IHttpReactor;
import com.cell.reactor.IMapDynamicHttpReactor;
import com.cell.reactor.impl.AbstractHttpDymanicCommandReactor;
import com.cell.serialize.ISerializable;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 */
@CellSpringHttpApplication
public class App
{
    @HttpCmdAnno(uri = "/demo/auto", httpCommandId = 1)
    public static class CCCmd extends AbstractHttpCommand
    {
        @Override
        protected void onExecute(IHttpCommandContext ctx, Object bo)
        {
            System.out.println("onExecute");
            ctx.response(this.createResponseWp().ret("ccccmd").build());
        }
    }

    public static class CCReactor extends AbstractHttpDymanicCommandReactor
    {
        @Override
        public List<Class<? extends IHttpCommand>> getHttpCommandList()
        {
            return Arrays.asList(CCCmd.class);
        }
    }

    public static class CC
    {

    }

    @Bean
    public CC cc()
    {
        return new CC();
    }

    @HttpCmdAnno(uri = "/demo/demo2", httpCommandId = 2)
    public static class MyComd2 extends AbstractHttpCommand
    {
        @Override
        protected void onExecute(IHttpCommandContext ctx, Object bo) 
        {
            ctx.response(this.createResponseWp()
                    .ret("123").build());
        }
    }

    @ReactorAnno
    public static class MyReactor2 extends AbstractHttpDymanicCommandReactor
    {
        @Override
        public List<Class<? extends IHttpCommand>> getHttpCommandList()
        {
            return Arrays.asList(MyComd2.class);
        }
    }

    public static void main(String[] args) throws Exception
    {
        IHttpReactor aReactor = new CCReactor();
        CellApplication.builder(App.class)
                .withReactor(aReactor)
                .newReactor()
                .withGroup("/demo")
                .withBean(CC.class)
                .newCommand()
                .withUri("/getUserName")
                .withBuzzHandler((ctx) ->
                {
                    IMapDynamicHttpReactor reactor = (IMapDynamicHttpReactor) ctx.getContext().getHttpReactor();
                    Object dependency = reactor.getDependency(CC.class);
                    Assert.notNull(dependency, "cc不可为空");
                    ctx.success("getUserName");
                })
                .newCommand()
                .withUri("/getFile")
                .withBuzzHandler((ctx) ->
                {
                    ctx.success("getFile");
                }).make().done().build().start(args);
    }
}
