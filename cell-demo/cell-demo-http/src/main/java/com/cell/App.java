package com.cell;

import com.cell.annotations.Command;
import com.cell.annotations.HttpCmdAnno;
import com.cell.annotations.ReactorAnno;
import com.cell.command.impl.AbstractJsonHttpCommand;
import com.cell.constants.ContextConstants;
import com.cell.context.IHttpContext;
import com.cell.context.InitCTX;
import com.cell.controller.SpringBaseHttpController;
import com.cell.dispatcher.IHttpCommandDispatcher;
import com.cell.protocol.ICommandExecuteResult;
import com.cell.reactor.impl.AbstractHttpCommandReactor;
import com.cell.serialize.ISerializable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Hello world!
 */
@SpringBootApplication(scanBasePackages = {"com.cell"})
public class App
{
    @RequestMapping("/my")
    @RestController
    public static class MyController extends SpringBaseHttpController
    {
//        @Override
//        protected void initDispatcher(IHttpCommandDispatcher dispatcher)
//        {
//            MyReactor a = new MyReactor();
//            a.registerCmd(new MyCMd());
//            dispatcher.addReactor(a);
//        }
    }

    @ReactorAnno
    public static class MyReactor extends AbstractHttpCommandReactor
    {
        @Override
        protected void onInit(InitCTX ctx)
        {
            this.registerCmd(new MyCMd());
        }
    }

    @Command(commandId = 1)
    @HttpCmdAnno(uri = "/my/demo")
    public static class MyCMd extends AbstractJsonHttpCommand
    {
        @Override
        protected ICommandExecuteResult doExecuteDirectly(IHttpContext ctx, ISerializable bo) throws IOException
        {
            System.out.println("exeecute");
            ctx.response(this.createResponseWp()
                    .status(ContextConstants.SUCCESS)
                    .ret(123)
                    .build());
            return null;
        }

        @Override
        public ISerializable getBO()
        {
            return null;
        }
    }

//    @Command(commandId = 2, couple = MyCMd.class)
//    public static class MyCmdCouple extends AbstractJsonHttpCommand
//    {
//
//    }

    public static void main(String[] args)
    {
        SpringApplication.run(App.class, args);
    }
}
