package com.cell.demo.common;

import com.cell.base.core.serialize.DefaultSelfJsonSerialize;
import com.cell.rpc.grpc.client.framework.annotation.GRPCClientRequestAnno;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-31 12:28
 */
@Data
@GRPCClientRequestAnno(protocol = "/demo/1.0.0", async = true, responseType = Demo.ServerRPCResponseaa.class)
public class ClientRequestDemo extends DefaultSelfJsonSerialize
{
    private String name = "charlie";

}
