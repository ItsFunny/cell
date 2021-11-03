package com.cell;

import com.cell.http.framework.annotation.CellSpringHttpApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

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
