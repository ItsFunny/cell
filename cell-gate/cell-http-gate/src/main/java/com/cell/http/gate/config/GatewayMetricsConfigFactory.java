package com.cell.http.gate.config;

import com.cell.base.common.exceptions.ProgramaException;
import com.cell.sdk.configuration.Configuration;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-15 21:39
 */
@Data
public class GatewayMetricsConfigFactory
{

    public static final String GatewayMetricsConfigModule = "gateway.metrics.properties";
    public static final GatewayMetricsConfigFactory instance = new GatewayMetricsConfigFactory();

    private GatewayMetricsConfig config;

    @Data
    public static class GatewayMetricsConfig
    {
        private double[] delayTimes = new double[]{2000, 1000};
        private int type = 1;
        private int typeValue = 1;
    }

    private GatewayMetricsConfigFactory()
    {

    }

    public static GatewayMetricsConfigFactory getInstance()
    {
        return instance;
    }

    // FIXME,还是接口化吧
    public void init()
    {
        try
        {
            this.config = Configuration.getDefault().getConfigValue(GatewayMetricsConfigModule).asObject(GatewayMetricsConfig.class);
        } catch (Exception e)
        {
            throw new ProgramaException(e);
        }
        Configuration.getDefault().getAndMonitorConfig(GatewayMetricsConfigModule, GatewayMetricsConfig.class, (conf) ->
                config = conf);
    }

}
