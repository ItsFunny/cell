package com.cell.service.impl;

import com.cell.annotation.CellSpringHttpApplication;
import com.cell.annotation.HttpCmdAnno;
import com.cell.application.CellApplication;
import com.cell.command.impl.AbstractHttpCommand;
import com.cell.constants.ContextConstants;
import com.cell.context.HttpContextResponseBody;
import com.cell.context.IHttpCommandContext;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.IOException;

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

    @HttpCmdAnno(uri = "/my/demo")
    public static class MyAA extends AbstractHttpCommand
    {
        @Override
        protected void onExecute(IHttpCommandContext ctx, Object bo) throws IOException
        {
            System.out.println("execution");
            B b = new B();
            b.name = "mydemo1";
            ctx.response(this.createResponseWp()
                    .status(ContextConstants.SUCCESS)
                    .other(HttpContextResponseBody.builder().status(HttpStatus.OK).build())
                    .ret(b)
                    .build());
        }
    }

//    @RestController
//    @RequestMapping("/my")
//    public static class MyController extends SpringBaseHttpController
//    {
//
//    }

    @HttpCmdAnno(uri = "/my/demo2")
    public static class NonAsMappingCmd extends AbstractHttpCommand
    {

        @Override
        protected void onExecute(IHttpCommandContext ctx, Object bo) throws IOException
        {
            System.out.println("mydemo2");
            B b = new B();
            b.name = "mydemo2";
            ctx.response(this.createResponseWp()
                    .status(ContextConstants.SUCCESS)
                    .other(HttpContextResponseBody.builder().status(HttpStatus.OK).build())
                    .ret(b)
                    .build());
        }
    }


    public static void main(String[] args)
    {
        CellApplication.run(MixTest.class, args);
    }
}
