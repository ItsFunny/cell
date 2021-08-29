package com.cell;

import com.cell.annotations.HttpCmdAnno;
import com.cell.annotations.ReactorAnno;
import com.cell.command.ICommandExecuteResult;
import com.cell.command.IDynamicHttpCommand;
import com.cell.controller.SpringBaseHttpController;
import com.cell.extension.HttpExtension;
import com.cell.reactor.IDynamicHttpReactor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.jvm.hotspot.jdi.SAPIDAttachingConnector;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication(scanBasePackages = {"com.cell"})
public class HttpExtensionTest
{
//    @ReactorAnno
//    public static class MyReactor implements IDynamicHttpReactor
//    {
//        @Override
//        public List<IDynamicHttpCommand> getCmds()
//        {
//            return Arrays.asList(new MyCmd1());
//        }
//    }
//
//    @HttpCmdAnno(uri = "/test")
//    public static class MyCmd1 implements IDynamicHttpCommand
//    {
//        @Override
//        public ICommandExecuteResult execute()
//        {
//            System.out.println("execute");
//            return null;
//        }
//    }

    @RequestMapping("/demo")
    public static class MyController extends SpringBaseHttpController
    {

    }

    public static void main(String[] args)
    {
        ApplicationContext ctx = SpringApplication.run(HttpExtensionTest.class, args);
        HttpExtension bean = ctx.getBean(HttpExtension.class);
        System.out.println(bean);
    }
}