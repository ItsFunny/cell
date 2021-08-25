package com.cell.initializer;

import com.cell.bridge.SpringExtensionManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import static org.junit.Assert.*;

@ComponentScan(basePackages = "com.cell")
@SpringBootApplication
public class SpringInitializerTest
{
    public static void main(String[] args)
    {
        ApplicationContext ctx = SpringApplication.run(SpringInitializerTest.class, args);
        SpringExtensionManager bean = ctx.getBean(SpringExtensionManager.class);
        System.out.println(bean);
    }
}