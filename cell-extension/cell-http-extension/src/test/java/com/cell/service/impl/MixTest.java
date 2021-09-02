package com.cell.service.impl;

import com.cell.HttpExtensionTest;
import com.cell.annotations.Command;
import com.cell.annotations.HttpCmdAnno;
import com.cell.annotations.ReactorAnno;
import com.cell.command.IHttpCommand;
import com.cell.command.impl.AbstractJsonHttpCommand;
import com.cell.constants.ContextConstants;
import com.cell.context.HttpContextResponseBody;
import com.cell.context.IHttpContext;
import com.cell.controller.SpringBaseHttpController;
import com.cell.protocol.ICommandExecuteResult;
import com.cell.reactor.impl.AbstractHttpDymanicCommandReactor;
import com.cell.reactor.impl.AbstractHttpStaticCommandReactor;
import com.cell.serialize.ISerializable;
import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description 一个为dynamicReactor, 另外一个为staticReactor
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-02 05:59
 */
@SpringBootApplication(scanBasePackages = {"com.cell"})
public class MixTest
{
    @Data
    static class B
    {
        private String name;
    }

    @Command(commandId = 1)
    @HttpCmdAnno(uri = "/my/demo")
    public static class MyAA extends AbstractJsonHttpCommand
    {
        @Override
        protected ICommandExecuteResult doExecuteDirectly(IHttpContext ctx, ISerializable bo) throws IOException
        {
            System.out.println("execution");
            B b = new B();
            b.name = "mydemo1";
            ctx.response(this.createResponseWp()
                    .status(ContextConstants.SUCCESS)
                    .other(HttpContextResponseBody.builder().status(HttpStatus.OK).build())
                    .ret(b)
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

    @RestController
    @RequestMapping("/my")
    public static class MyController extends SpringBaseHttpController
    {

    }

    @Command(commandId = 2)
    @HttpCmdAnno(uri = "/my/demo2")
    public static class NonAsMappingCmd extends AbstractJsonHttpCommand
    {

        @Override
        protected ICommandExecuteResult doExecuteDirectly(IHttpContext ctx, ISerializable bo) throws IOException
        {
            System.out.println("mydemo2");
            B b = new B();
            b.name = "mydemo2";
            ctx.response(this.createResponseWp()
                    .status(ContextConstants.SUCCESS)
                    .other(HttpContextResponseBody.builder().status(HttpStatus.OK).build())
                    .ret(b)
                    .build());
            return null;
        }
    }

    @ReactorAnno
    public static class NonAsMappingReactor extends AbstractHttpStaticCommandReactor
    {
        @Override
        public List<Class<? extends IHttpCommand>> getHttpCommandList()
        {
            return Arrays.asList(NonAsMappingCmd.class);
        }
    }

    public static void main(String[] args)
    {
        SpringApplication.run(MixTest.class, args);
    }
}
