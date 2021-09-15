package com.cell;

import com.cell.annotation.CellSpringHttpApplication;
import com.cell.annotations.CellApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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
