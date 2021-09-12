package com.cell.config;

import com.cell.Configuration;
import com.cell.exceptions.ProgramaException;
import lombok.Data;

import java.io.IOException;

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

    public static GatewayConfiguration getInstance()
    {
        return instance;
    }

    public static void init()
    {
        try
        {
            instance = Configuration.getDefault().getConfigValue(GATEWAY_CONFIG_MODULE).asObject(GatewayConfiguration.class);
        } catch (IOException e)
        {
            throw new ProgramaException(e);
        }
    }

    private GatewayConfiguration()
    {

    }


}
