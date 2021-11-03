package com.cell.base.common.constants;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 04:57
 */
public interface ProtocolConstants
{
    String RESPONSE_HEADER_CODE = "code";
    String RESPONSE_HEADER_MSG = "msg";
    String INIT_CTX_CMDS = "INIT_CTX_CMDS";

    byte TYPE_HTTP = 1 << 0;
    byte TYPE_RPC = 1 << 1;

    byte REACTOR_TYPE_HTTP = 1 << 2 | TYPE_HTTP;
    byte REACTOR_TYPE_RPC_GRPC_SERVER = 1 << 3 | TYPE_RPC;
    byte REACTOR_TYPE_RPC_CLIENT = 1 << 4 | TYPE_RPC;

    byte SERIALIZE_JSON = 1 << 0;

}
