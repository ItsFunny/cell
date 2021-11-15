package com.cell.demo.gprc.server;

import com.cell.node.spring.annotation.CellSpringHttpApplication;
import org.springframework.boot.SpringApplication;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-05 15:31
 */
@CellSpringHttpApplication
public class DemoGrpcServer
{
    public static void main(String[] args)
    {
        try
        {
            SpringApplication.run(DemoGrpcServer.class, args);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
