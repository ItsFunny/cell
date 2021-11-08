package com.cell.rpc.server.base.framework.context;

import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-21 18:24
 */
@Data
public class RPCServerContextResponseWrapper
{
    private Throwable error;
    private String logicMsg;

}
