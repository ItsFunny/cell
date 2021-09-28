package com.cell;

import com.cell.shell.Shell;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testEcho() throws Exception
    {
        //执行命令
        Shell.build("ping", "www.baidu.com")
                .onProcess((line, helper) ->
                {
                    System.out.println(line);
                    helper.kill(); //结束
                })
                .onError((line, helper) -> System.out.println(line))
                .exec();
        //执行多个命令
        Shell.buildText("echo helloShell \n ls").dir("/")
                .onProcess((line, helper) -> System.out.println(line))
                .exec();
    }

    @Test
    public void testEchoasd() throws Exception
    {
        Shell.buildText("asddd")
                .onProcess(((line, helper) ->
                {
                    System.out.println(line);
                    helper.kill();
                })).onError((e, h) ->
        {
            System.out.println(e);
            h.kill();
        })
                .exec();
    }
}
