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
 * @Date 创建时间：2021-09-08 21:17
 */
@Data
public class NacosConfiguration
{
    private static NacosConfiguration instance;
    private static final String NACOS_CONFIG_MODULE = "nacos.properties";

    private String serverAddr;

    static
    {
        try
        {
            instance = Configuration.getDefault().getConfigValue(NACOS_CONFIG_MODULE).asObject(NacosConfiguration.class);
        } catch (IOException e)
        {
            throw new ProgramaException(e);
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
