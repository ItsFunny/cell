package com.cell;

import com.cell.annotation.CellSpringHttpApplication;

/**
 * Hello world!
 */
@CellSpringHttpApplication
@EnableEurekaServer
@EnableEurekaClient
public class EurekaServerApplication
{
    public static void main(String[] args)
    {
        System.out.println("Hello World!");
    }
}
