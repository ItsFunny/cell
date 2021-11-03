package com.cell;

import com.cell.rpc.client.base.framework.annotation.CellSpringHttpApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Hello world!
 */
@CellSpringHttpApplication
public class Gateway
{
    public static void main(String[] args)
    {
        ConfigurableApplicationContext run = SpringApplication.run(Gateway.class, args);
    }

}
