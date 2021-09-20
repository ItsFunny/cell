package com.cell;

import com.cell.annotation.CellSpringHttpApplication;
import com.cell.application.CellApplication;

/**
 * Hello world!
 */
@CellSpringHttpApplication
public class App
{

    public static void main(String[] args)
    {
        startNRestFulAPI(args);
    }

    private static void simpleDemo(String[] args)
    {
        CellApplication.builder(App.class)
                .newReactor()
                .withGroup("/demo")
                .post("/post", (wp) ->
                {
                    wp.success("post");
                }).make().get("/get", (wp) ->
        {
            wp.success("get");
        }).make().done().build().start(args);
    }

    private static void startNRestFulAPI(String[] args)
    {
        CellApplication.CellApplicationBuilder builder = CellApplication.builder(App.class);
        CellApplication.ReactorBuilder reactorBuilder = builder.newReactor();
        reactorBuilder.withGroup("/demo");
        for (int i = 0; i < 100; i++)
        {
            final Integer ret = i;
            reactorBuilder.post("/post" + i, (wp) ->
            {
                wp.success(ret);
            });
        }
        reactorBuilder.done().build().start(args);
    }
}
