package com.cell.component.cache;


import com.cell.component.cache.service.ICacheService;
import com.cell.component.cache.service.impl.DefaultMemoryCacheServiceImpl;
import com.cell.timewheel.DefaultHashedTimeWheel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfiguration
{
    @Bean
    @ConditionalOnMissingBean(ICacheService.class)
    public ICacheService<String, String> cacheService()
    {
        return new DefaultMemoryCacheServiceImpl<>(DefaultHashedTimeWheel.getInstance());
    }
}
