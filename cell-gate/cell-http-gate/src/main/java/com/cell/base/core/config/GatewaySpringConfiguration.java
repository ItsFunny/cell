package com.cell.base.core.config;

import com.cell.base.core.annotations.ActiveConfiguration;
import com.cell.base.core.annotations.Plugin;
import com.cell.constants.GatewayConstants;
import com.cell.enums.EnumStatOperateMask;
import com.cell.enums.EnumStatisticType;
import com.cell.lb.ILoadBalancerStrategy;
import com.cell.lb.impl.DefaultWeightRoubineStrategy;
import com.cell.prometheus.HistogramStator;
import io.prometheus.client.CollectorRegistry;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-10 04:36
 */
@ActiveConfiguration
public class GatewaySpringConfiguration
{
    @Plugin
    public ILoadBalancerStrategy defaultLoadBalancerStrategy()
    {
        return new DefaultWeightRoubineStrategy();
    }

    @Plugin
    public RouteLocator globalLocator(RouteLocatorBuilder builder)
    {
        return builder.routes()
                .route("global_route", p ->
                        p.path("/**")
                                .uri("https://www.baidu.com")).build();
    }


    // metrics
    @Bean
    public HistogramStator exceedDelayThresoldStator(CollectorRegistry registry)
    {
        GatewayMetricsConfigFactory.GatewayMetricsConfig config = GatewayMetricsConfigFactory.getInstance().getConfig();
        EnumStatisticType type = EnumStatisticType.fromValue(config.getType());
        Integer typeValue = config.getTypeValue();
        return HistogramStator.
                build("ExceedDelayThresoldStator", "ExceedDelayThresoldStator")
                .labelNames(GatewayConstants.HTTP_GATE_LABELS)
                .average(type, typeValue)
                .operate(EnumStatOperateMask.MAX_VALUE, "MaxResponseDelay")
                .operate(EnumStatOperateMask.MIN_VALUE, "MinResponseDelay")
                .operate(EnumStatOperateMask.STANDARD_DEVIATION, "ResponseDelayStandardDivision")
                .operate(EnumStatOperateMask.AVERAGE, "AverageResponseDelay")
                .buckets(config.getDelayTimes())
                .register(registry);
    }


}
