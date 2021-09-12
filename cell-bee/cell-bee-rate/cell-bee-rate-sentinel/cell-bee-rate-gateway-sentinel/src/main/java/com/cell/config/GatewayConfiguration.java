package com.cell.config;

import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-12 17:34
 */
@Data
public class GatewayConfiguration
{
    private static final GatewayConfiguration instance = new GatewayConfiguration();

    private ServerRatePropertyNode serverRatePropertyNode;

    public static GatewayConfiguration getInstance()
    {
        return instance;
    }

    private GatewayConfiguration()
    {

    }


}
