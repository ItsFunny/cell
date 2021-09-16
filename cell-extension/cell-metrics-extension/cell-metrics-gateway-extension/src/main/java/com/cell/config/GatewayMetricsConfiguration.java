package com.cell.config;

import com.cell.annotations.ActiveConfiguration;
import com.cell.annotations.Plugin;
import com.cell.constants.StatConstants;
import com.cell.enums.EnumStatOperateMask;
import com.cell.enums.EnumStatisticType;
import com.cell.prometheus.HistogramStator;
import io.prometheus.client.CollectorRegistry;
import org.springframework.context.annotation.Bean;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-15 21:38
 */
@ActiveConfiguration
public class GatewayMetricsConfiguration
{
    @Plugin
    public HistogramStator exceedDelayThresoldCount(CollectorRegistry registry)
    {
        GatewayMetricsConfigFactory.GatewayMetricsConfig config = GatewayMetricsConfigFactory.getInstance().getConfig();
        EnumStatisticType type = EnumStatisticType.fromValue(config.getType());
        Integer typeValue = config.getTypeValue();
        return HistogramStator.
                build("ExceedDelayThresoldCount", "ExceedDelayThresoldCount")
                .labelNames(StatConstants.HTTP_GATE_LABELS)
                .average(type, typeValue)
                .operate(EnumStatOperateMask.MAX_VALUE, "MaxResponseDelay")
                .operate(EnumStatOperateMask.MIN_VALUE, "MinResponseDelay")
                .operate(EnumStatOperateMask.STANDARD_DEVIATION, "ResponseDelayStandardDivision")
                .operate(EnumStatOperateMask.AVERAGE, "AverageResponseDelay")
                .buckets(config.getDelayTimes())
                .register(registry);
    }

}
