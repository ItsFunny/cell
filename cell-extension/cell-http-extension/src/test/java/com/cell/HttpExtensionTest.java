package com.cell;

import com.cell.command.ICommandExecuteResult;
import com.cell.command.IDynamicHttpCommand;
import com.cell.extension.HttpExtension;
import com.cell.reactor.IDynamicHttpReactor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication(scanBasePackages = {"com.cell"})
public class HttpExtensionTest
{
    public static class MyReactor implements IDynamicHttpReactor
    {
        @Override
        public List<IDynamicHttpCommand> getCmds()
        {
            return Arrays.asList(new MyCmd1());
        }
    }

    public static class MyCmd1 implements IDynamicHttpCommand
    {
        @Override
        public ICommandExecuteResult execute()
        {
            System.out.println("execute");
            return null;
        }
    }

    public static void main(String[] args)
    {
        ApplicationContext ctx = SpringApplication.run(HttpExtensionTest.class, args);
        HttpExtension bean = ctx.getBean(HttpExtension.class);
        System.out.println(bean);
    }
}