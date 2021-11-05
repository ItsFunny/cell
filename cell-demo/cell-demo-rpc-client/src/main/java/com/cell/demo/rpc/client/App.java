package com.cell.demo.rpc.client;

import com.cell.rpc.grpc.client.framework.annotation.CellSpringHttpApplication;
import org.springframework.boot.SpringApplication;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-05 16:49
 */
@CellSpringHttpApplication
public class App
{
    public static void main(String[] args)
    {
        SpringApplication.run(App.class, args);
    }
}
