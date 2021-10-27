package com.cell;

import com.cell.annotation.CellSpringHttpApplication;
import com.cell.application.CellApplication;
import org.springframework.stereotype.Component;

/**
 * Hello world!
 */
@CellSpringHttpApplication
public class App
{
    @Component
    public static class AAA
    {
    }

    public static void main(String[] args)
    {
        CellApplication.run(App.class, args);
    }
}
