package com.cell;

import com.cell.annotation.CellSpringHttpApplication;
import com.cell.application.CellApplication;
import org.springframework.context.ApplicationContext;

/**
 * Hello world!
 */
@CellSpringHttpApplication
public class App
{
    public static void main(String[] args)
    {
        ApplicationContext start = CellApplication.builder(App.class)
                .newReactor()
                .post("/post", new CellApplication.DEFAULT_DEMO_POST()).make()
                .get("/get", new CellApplication.DEFAULT_DEMO_GET()).make()
                .done()
                .withPort(8081)
                .build()
                .start(args);
    }
}
