package com.cell.constants;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-25 11:42
 */
public interface GRPCConstants
{
    String ANY_IP_ADDRESS = "*";
    String DOMAIN_SOCKET_ADDRESS_SCHEME = "unix";
    String DOMAIN_SOCKET_ADDRESS_PREFIX = DOMAIN_SOCKET_ADDRESS_SCHEME + ":";
    String CLOUD_DISCOVERY_METADATA_PORT = "gRPC_port";
    int INTER_PROCESS_DISABLE = -1;
}
