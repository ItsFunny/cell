package com.cell;

import com.cell.annotations.Command;
import com.cell.annotations.HttpCmdAnno;
import com.cell.annotations.ReactorAnno;
import com.cell.command.IHttpCommand;
import com.cell.command.impl.AbstractHttpCommand;
import com.cell.constants.ContextConstants;
import com.cell.context.IHttpContext;
import com.cell.factory.ReactoryFactory;
import com.cell.protocol.ContextResponseWrapper;
import com.cell.protocol.ICommandExecuteResult;
import com.cell.reactor.IMapDynamicHttpReactor;
import com.cell.reactor.impl.AbstractHttpCommandReactor;
import com.cell.reactor.impl.AbstractHttpDymanicCommandReactor;
import com.cell.serialize.ISerializable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 */

@SpringBootApplication(scanBasePackages = {"com.cell"})
public class App
{
    public static class CC
    {

    }

    @Bean
    public CC cc()
    {
        return new CC();
    }

    @Command(commandId = 4)
    @HttpCmdAnno(uri = "/demo/demo2")
    public static class MyComd2 extends AbstractHttpCommand
    {
        @Override
        protected ICommandExecuteResult onExecute(IHttpContext ctx, ISerializable bo) throws IOException
        {
            ctx.response(this.createResponseWp()
                    .ret("123").build());
            return null;
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
        ReactoryFactory.builder()
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
                    return null;
                })
                .newCommand()
                .withUri("/getFile")
                .withBuzzHandler((ctx) ->
                {
                    ctx.success("getFile");
                    return null;
                }).make().build();

        SpringApplication.run(App.class, args);
    }
}
