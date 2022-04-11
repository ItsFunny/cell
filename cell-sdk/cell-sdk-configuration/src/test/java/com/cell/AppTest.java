package com.cell;

import com.cell.sdk.configuration.Configuration;
import com.cell.sdk.configuration.exception.ConfigurationException;
import com.cell.sdk.configuration.model.IConfigValue;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    /**
     * Rigorous Test :-)
     */
    public static void main(String[] args)
    {
        try
        {
            Configuration.getDefault().initialize("/Users/lvcong/java/cell/config", "test-asd");
            IConfigValue configValue = Configuration.getDefault().getConfigValue("nacos.properties");
            System.out.println(configValue);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
