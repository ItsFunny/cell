package com.cell;

import com.cell.annotation.CellSpringHttpApplication;
import com.cell.application.CellApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Hello world!
 */
@CellSpringHttpApplication
@EnableDiscoveryClient
public class App
{
    public static void main(String[] args)
    {
        CellApplication.run(App.class, args);
    }
}
