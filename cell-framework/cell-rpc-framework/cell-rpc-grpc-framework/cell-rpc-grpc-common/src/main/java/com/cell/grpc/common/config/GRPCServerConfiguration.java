package com.cell.grpc.common.config;

import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-23 23:05
 */
@Data
public class GRPCServerConfiguration
{
    private String address;
    private long keepAliveTime = TimeUnit.HOURS.toNanos(2);
    private long keepAliveTimeOut = TimeUnit.SECONDS.toNanos(20);
    private long permitKeepAliveTime = TimeUnit.MINUTES.toNanos(5);
    private boolean permitKeepAliveWithoutCalls = false;
    private Long maxConnectionIdle;
    private Long maxConnectionAge = null;
    private Long maxConnectionAgeGrace = null;
    private Integer maxInboundMessageSize = null;
    private Integer maxInboundMetadataSize = null;


    public static GRPCServerConfiguration defaultConfiguration()
    {
        GRPCServerConfiguration ret = new GRPCServerConfiguration();
        ret.setAddress("0.0.0.0");
        return ret;
    }
}
