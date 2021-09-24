package com.cell;

import com.cell.annotation.CellSpringHttpApplication;
import com.cell.annotations.AutoPlugin;
import com.cell.annotations.HttpCmdAnno;
import com.cell.annotations.ReactorAnno;
import com.cell.application.CellApplication;
import com.cell.command.IHttpCommand;
import com.cell.command.impl.AbstractHttpCommand;
import com.cell.context.IHttpCommandContext;
import com.cell.reactor.impl.AbstractHttpDymanicCommandReactor;
import com.cell.serialize.ISerializable;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-04 05:59
 */
@CellSpringHttpApplication
public class App
{
    @HttpCmdAnno(uri = "/cmd1", httpCommandId = 1)
    public static class CMD1 extends AbstractHttpCommand
    {
        @Override
        protected void onExecute(IHttpCommandContext ctx, Object bo) throws IOException
        {
            ctx.response(this.createResponseWp().ret("cmd1").build());
        }
    }

    @HttpCmdAnno(uri = "/cmd2", httpCommandId = 1)
    public static class CMD2 extends AbstractHttpCommand
    {
        @Override
        protected void onExecute(IHttpCommandContext ctx, Object bo) throws IOException
        {
            Reactor2 reactor2 = (Reactor2) ctx.getHttpReactor();
            Assert.notNull(reactor2.autoasd, "asd");
            ctx.response(this.createResponseWp().ret("cmd2").build());
        }
    }

    // 与Spring的bean可以任意结合
    public static class Autoasd
    {

    }

    @Bean
    public Autoasd asd()
    {
        return new Autoasd();
    }

    @ReactorAnno(group = "/demo")
    public static class Reactor2 extends AbstractHttpDymanicCommandReactor
    {
        @AutoPlugin
        private Autoasd autoasd;

        @Override
        public List<Class<? extends IHttpCommand>> getHttpCommandList()
        {
            return Arrays.asList(CMD1.class, CMD2.class);
        }
    }

    public static void main(String[] args)
    {
        CellApplication.run(App.class, args);
    }
}
