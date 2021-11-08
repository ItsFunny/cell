package com.cell.grpc.common.config;

import com.cell.Configuration;
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
    private static GatewayConfiguration instance = null;
    private static final String GATEWAY_CONFIG_MODULE = "gateway.properties";

    private ServerRatePropertyNode serverRatePropertyNode=new ServerRatePropertyNode();
    private GatePropertyNode gatePropertyNode=new GatePropertyNode();

    public static GatewayConfiguration getInstance()
    {
        return instance;
    }

    public static void init()
    {
        try
        {
            instance = Configuration.getDefault().getConfigValue(GATEWAY_CONFIG_MODULE).asObject(GatewayConfiguration.class);
        } catch (Exception e)
        {
            instance=new GatewayConfiguration();
        }
    }

    private GatewayConfiguration()
    {

    }


}
