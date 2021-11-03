package com.cell;

import com.cell.http.framework.annotation.CellSpringHttpApplication;
import com.cell.http.framework.annotation.GRPCClient;
import com.cell.cluster.BaseGrpcGrpc;
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
