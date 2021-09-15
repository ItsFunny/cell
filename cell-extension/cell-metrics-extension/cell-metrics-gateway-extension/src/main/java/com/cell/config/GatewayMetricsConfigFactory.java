package com.cell.config;

import com.cell.Configuration;
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
    public static final String GatewayMetricsConfigModule = "gateway.metrics";
    public static final GatewayMetricsConfigFactory instance = new GatewayMetricsConfigFactory();

    private GatewayMetricsConfig config;

    @Data
    public static class GatewayMetricsConfig
    {
        private double[] delayTimes;
        private Integer type;
        private Integer typeValue = 1;
    }

    private GatewayMetricsConfigFactory()
    {

    }

    public static GatewayMetricsConfigFactory getInstance()
    {
        return instance;
    }

    static
    {
        // FIXME extension?
        instance.init();
    }

    // FIXME,还是接口化吧
    public void init()
    {
        Configuration.getDefault().getAndMonitorConfig(GatewayMetricsConfigModule, GatewayMetricsConfig.class, (conf) ->
                config = conf);
    }

}
