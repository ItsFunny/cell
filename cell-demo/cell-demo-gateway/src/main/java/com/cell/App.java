package com.cell;

import com.cell.annotation.CellSpringHttpApplication;
import org.springframework.boot.SpringApplication;

/**
 * Hello world!
 *
 */
@CellSpringHttpApplication
public class App 
{
    public static void main( String[] args )
    {
        SpringApplication.run(App.class,args);
    }
}