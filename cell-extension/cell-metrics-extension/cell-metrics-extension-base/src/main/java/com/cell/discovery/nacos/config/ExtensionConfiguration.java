package com.cell.discovery.nacos.config;

import com.cell.annotations.ActiveConfiguration;
import io.prometheus.client.CollectorRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-16 04:49
 */
@ActiveConfiguration
public class ExtensionConfiguration
{
    @Bean
    @ConditionalOnMissingBean
    public CollectorRegistry collectorRegistry()
    {
        return new CollectorRegistry(true);
    }

}
