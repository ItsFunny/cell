package com.cell.service.impl;

import com.cell.annotation.CellSpringHttpApplication;
import com.cell.annotations.Command;
import com.cell.annotations.HttpCmdAnno;
import com.cell.annotations.ReactorAnno;
import com.cell.application.CellApplication;
import com.cell.command.IHttpCommand;
import com.cell.command.impl.AbstractHttpCommand;
import com.cell.constants.ContextConstants;
import com.cell.context.HttpContextResponseBody;
import com.cell.context.IHttpContext;
import com.cell.protocol.ICommandExecuteResult;
import com.cell.reactor.impl.AbstractHttpDymanicCommandReactor;
import com.cell.serialize.ISerializable;
import lombok.Data;
import org.springframework.http.HttpStatus;

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
@CellSpringHttpApplication
public class MixTest
{
    @Data
    static class B
    {
        private String name;
    }

    @HttpCmdAnno(uri = "/my/demo", httpCommandId = 1)
    public static class MyAA extends AbstractHttpCommand
    {
        @Override
        protected ICommandExecuteResult onExecute(IHttpContext ctx, ISerializable bo) throws IOException
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

//    @RestController
//    @RequestMapping("/my")
//    public static class MyController extends SpringBaseHttpController
//    {
//
//    }

    @HttpCmdAnno(uri = "/my/demo2", httpCommandId = 2)
    public static class NonAsMappingCmd extends AbstractHttpCommand
    {

        @Override
        protected ICommandExecuteResult onExecute(IHttpContext ctx, ISerializable bo) throws IOException
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
    public static class NonAsMappingReactor extends AbstractHttpDymanicCommandReactor
    {
        @Override
        public List<Class<? extends IHttpCommand>> getHttpCommandList()
        {
            return Arrays.asList(NonAsMappingCmd.class);
        }
    }

    public static void main(String[] args)
    {
        CellApplication.run(MixTest.class, args);
    }
}
