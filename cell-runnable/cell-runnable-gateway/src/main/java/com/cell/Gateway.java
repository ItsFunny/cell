package com.cell;

import com.cell.rpc.grpc.client.framework.annotation.CellSpringHttpApplication;
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
        try
        {
            ConfigurableApplicationContext run = SpringApplication.run(Gateway.class, args);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

}
