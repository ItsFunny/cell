package com.cell.grpc.common.constants;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-04 12:55
 */
public interface GRPCConstants
{
    String DEFAULT_GRPC_SERVER = "default";
    Integer DEFAULT_GRPC_SERVER_PORT = 12000;

    String DEFAULT_GRPC_SERVER_ADDR = "127.0.0.1";
    String DEFAULT_GRPC_CONFIG_MODULE = "grpc.client.propertoes";

    String ANY_IP_ADDRESS = "*";
    String DOMAIN_SOCKET_ADDRESS_SCHEME = "unix";
    String DOMAIN_SOCKET_ADDRESS_PREFIX = DOMAIN_SOCKET_ADDRESS_SCHEME + ":";
    String CLOUD_DISCOVERY_METADATA_PORT = "gRPC_port";
    int INTER_PROCESS_DISABLE = -1;
}
