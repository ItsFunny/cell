package com.cell;

import com.cell.grpc.common.annotation.GRPCClient;
import com.cell.grpc.common.cluster.BaseGrpcGrpc;
import com.cell.node.spring.annotation.CellSpringHttpApplication;
import org.junit.Test;
import org.springframework.boot.SpringApplication;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
@CellSpringHttpApplication
public class AppTest
{

    @GRPCClient("asd")
    public BaseGrpcGrpc.BaseGrpcStub stub;

    public static void main(String[] args)
    {
        SpringApplication.run(AppTest.class, args);
    }

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue(true);
    }
}
