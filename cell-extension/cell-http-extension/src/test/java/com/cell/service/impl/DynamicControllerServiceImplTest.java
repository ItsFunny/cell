package com.cell.service.impl;

import com.cell.HttpExtensionTest;
import com.cell.annotations.Command;
import com.cell.annotations.HttpCmdAnno;
import com.cell.annotations.Plugin;
import com.cell.annotations.ReactorAnno;
import com.cell.command.IHttpCommand;
import com.cell.command.impl.AbstractJsonHttpCommand;
import com.cell.constants.ContextConstants;
import com.cell.context.HttpContextResponseBody;
import com.cell.context.IHttpContext;
import com.cell.extension.HttpExtension;
import com.cell.protocol.ContextResponseWrapper;
import com.cell.protocol.ICommandExecuteResult;
import com.cell.reactor.impl.AbstractHttpCommandReactor;
import com.cell.reactor.impl.AbstractHttpDymanicCommandReactor;
import com.cell.serialize.ISerializable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootApplication(scanBasePackages = {"com.cell"})
public class DynamicControllerServiceImplTest
{
    @Command(commandId = 1)
    @HttpCmdAnno(uri = "/demo/dymanic")
    public static class MyAA extends AbstractJsonHttpCommand
    {
        @Override
        protected ICommandExecuteResult doExecuteDirectly(IHttpContext ctx, ISerializable bo) throws IOException
        {
            System.out.println("execution");
            ctx.response(ContextResponseWrapper.builder()
                    .status(ContextConstants.SUCCESS)
                    .other(HttpContextResponseBody.builder().status(HttpStatus.OK).build())
                    .ret("123")
                    .build());
            return null;
        }
    }

    @ReactorAnno
    public static class DymanicReactor extends AbstractHttpDymanicCommandReactor
    {
        @Override
        public List<Class<? extends IHttpCommand>> getHttpCommandList()
        {
            return Arrays.asList(MyAA.class);
        }
    }

    public static void main(String[] args)
    {
        SpringApplication.run(HttpExtensionTest.class, args);
//        ApplicationContext ctx = SpringApplication.run(HttpExtensionTest.class, args);
//        HttpExtension bean = ctx.getBean(HttpExtension.class);
//        System.out.println(bean);
    }
}