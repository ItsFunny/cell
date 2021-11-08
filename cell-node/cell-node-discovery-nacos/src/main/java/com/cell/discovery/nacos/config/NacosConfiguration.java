package com.cell.discovery.nacos.config;

import com.cell.sdk.configuration.Configuration;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-08 21:17
 */
@Data
public class NacosConfiguration
{
    private static NacosConfiguration instance;
    private static final String NACOS_CONFIG_MODULE = "nacos.properties";

    private String serverAddr = "127.0.0.1:8848";

    static
    {
        try
        {
            instance = Configuration.getDefault().getConfigValue(NACOS_CONFIG_MODULE).asObject(NacosConfiguration.class);
        } catch (Exception e)
        {
            NacosConfiguration def = new NacosConfiguration();
            instance = def;
        }
    }

    private NacosConfiguration()
    {

    }

    public static NacosConfiguration getInstance()
    {
        return instance;
    }

}
