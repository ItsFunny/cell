package com.cell.rpc.client;

import com.cell.base.core.serialize.DefaultSelfJsonSerialize;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-31 21:44
 */
@Data
public class ServerRPCResponse extends DefaultSelfJsonSerialize
{
    private String addr = "zhejiang";

}
