package com.cell.node.discovery.nacos.config;

import com.cell.sdk.configuration.Configuration;
import com.cell.sdk.log.LOG;
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

    private NacosConfiguration()
    {

    }

    public static NacosConfiguration getInstance()
    {
        if (instance == null)
        {
            synchronized (NacosConfiguration.class)
            {
                if (instance != null)
                {
                    return instance;
                }
                try
                {
                    instance = Configuration.getDefault().getConfigValue(NACOS_CONFIG_MODULE).asObject(NacosConfiguration.class);
                } catch (Exception e)
                {
                    LOG.error("读取nacos的配置失败:{}", e.getMessage());
                    NacosConfiguration def = new NacosConfiguration();
                    instance = def;
                }
            }
        }
        return instance;
    }

}
