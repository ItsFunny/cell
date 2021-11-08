package com.cell.metrics.extension.common.config;

import com.cell.base.core.annotations.ActiveConfiguration;
import com.cell.bee.statistic.base.enums.EnumStatOperateMask;
import com.cell.bee.statistic.base.enums.EnumStatisticType;
import com.cell.bee.statistic.prometheus.CellGaugeStator;
import com.cell.bee.statistic.prometheus.HistogramStator;
import io.prometheus.client.CollectorRegistry;
import org.springframework.context.annotation.Bean;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-27 10:18
 */
@ActiveConfiguration
public class MetricsConfiguration
{
    public static final String[] COMMON_LABELS = new String[]{"nodeName", "cluster", "module", "uri", "method"};
    public static final double[] defaultDealys = new double[]{1000, 2000};

    // 总的请求数统计
    @Bean
    public CellGaugeStator totalCounter(CollectorRegistry registry)
    {
        return CellGaugeStator.build("totalCounter", "总共请求数")
                .labelNames("nodeName", "cluster", "method")
//                .average(EnumStatisticType.SECOND, 1)
                .register(registry);
    }

    // 耗时统计
    @Bean
    public HistogramStator exceedDelayThresoldCount(CollectorRegistry registry)
    {
        EnumStatisticType type = EnumStatisticType.SECOND;
        Integer typeValue = 1;
        return HistogramStator.
                build("ExceedDelayThresoldCount", "ExceedDelayThresoldCount")
                .labelNames(COMMON_LABELS)
                .average(type, typeValue)
                .operate(EnumStatOperateMask.MAX_VALUE, "MaxResponseDelay")
                .operate(EnumStatOperateMask.MIN_VALUE, "MinResponseDelay")
                .operate(EnumStatOperateMask.STANDARD_DEVIATION, "ResponseDelayStandardDivision")
                .operate(EnumStatOperateMask.AVERAGE, "AverageResponseDelay")
                .buckets(defaultDealys)
                .register(registry);
    }

    // 失败统计
    @Bean
    public CellGaugeStator failCounter(CollectorRegistry registry)
    {
        return CellGaugeStator.build("failCounter", "failCounter")
                .labelNames(COMMON_LABELS)
//                .average(EnumStatisticType.SECOND, 1)
                .register(registry);
    }

    public MetricsConfiguration()
    {
    }

    // 成功统计
    @Bean
    public CellGaugeStator successCounter(CollectorRegistry registry)
    {
        return CellGaugeStator.build("successCounter", "successCounter")
                .labelNames(COMMON_LABELS)
//                .average(EnumStatisticType.SECOND, 1)
                .register(registry);
    }
}
