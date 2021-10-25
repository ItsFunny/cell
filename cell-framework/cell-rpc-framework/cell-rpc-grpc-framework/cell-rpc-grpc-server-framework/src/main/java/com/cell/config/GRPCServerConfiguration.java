package com.cell.config;

import lombok.Data;

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

    private static final GRPCServerConfiguration instance = new GRPCServerConfiguration();


    private String address;
    private int port;
}
