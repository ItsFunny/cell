package com.cell;

import com.cell.annotations.Command;
import com.cell.annotations.HttpCmdAnno;
import com.cell.command.impl.AbstractHttpCommand;
import com.cell.controller.SpringBaseHttpController;
import com.cell.dispatcher.IHttpCommandDispatcher;
import com.cell.initializer.SpringInitializer;
import com.cell.protocol.ICommandExecuteResult;
import com.cell.reactor.impl.AbstractHttpCommandReactor;
import com.cell.serialize.IInputArchive;
import com.cell.serialize.IOutputArchive;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
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
        @Override
        protected void initDispatcher(IHttpCommandDispatcher dispatcher)
        {
            MyReactor a = new MyReactor();
            a.registerCmd(new MyCMd());
            dispatcher.addReactor(a);
        }
    }

    public static class MyReactor extends AbstractHttpCommandReactor
    {

    }

    @Command(commandId = 1, couple = MyCmdCouple.class)
    @HttpCmdAnno(uri = "/my/demo")
    public static class MyCMd extends AbstractHttpCommand
    {

        @Override
        public ICommandExecuteResult execute()
        {
            System.out.println("exeecute");
            return null;
        }

        @Override
        public void read(IInputArchive input) throws IOException
        {

        }

        @Override
        public void write(IOutputArchive output) throws IOException
        {

        }
    }

    @Command(commandId = 2, couple = MyCMd.class)
    public static class MyCmdCouple extends AbstractHttpCommand
    {

        @Override
        public ICommandExecuteResult execute()
        {
            return null;
        }

        @Override
        public void read(IInputArchive input) throws IOException
        {

        }

        @Override
        public void write(IOutputArchive output) throws IOException
        {

        }
    }

    public static void main(String[] args)
    {
        SpringApplication.run(App.class, args);
    }
}
