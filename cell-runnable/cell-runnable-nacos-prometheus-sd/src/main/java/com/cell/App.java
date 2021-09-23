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
        CellApplication.run(App.class, args);
    }
}
