package com.cell.rpc.client;

import com.cell.http.framework.annotation.GRPCClientRequestAnno;
import com.cell.serialize.DefaultSelfJsonSerialize;
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
@GRPCClientRequestAnno(protocol = "/demo/1.0.0", async = true, responseType = ServerRPCResponse.class)
public class ClientRequestDemo extends DefaultSelfJsonSerialize
{
    private String name = "charlie";

}
