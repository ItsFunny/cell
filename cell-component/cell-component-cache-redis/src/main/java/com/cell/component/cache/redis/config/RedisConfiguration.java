package com.cell.component.cache.redis.config;

import com.cell.component.cache.redis.service.impl.RedisCacheServiceImpl;
import com.cell.component.cache.service.ICacheService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

@Configuration
@EnableRedisRepositories("com.cell")
public class RedisConfiguration
{

    @Bean
    @Primary
    public ICacheService<String, String> cacheService()
    {
        return new RedisCacheServiceImpl();
    }

    @Bean
    public RedisConnectionFactory jedisConnectionFactory()
    {
        RedisConfig redisConfig = RedisConfig.getInstance();
        if (redisConfig.getCluster())
        {
            RedisClusterConfiguration configuration = new RedisClusterConfiguration(redisConfig.getNodes());
            configuration.setPassword(RedisPassword.of(redisConfig.getClusterPassword()));
            return new JedisConnectionFactory(configuration);
        }

        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisConfig.getHost(),
                redisConfig.getPort());
        configuration.setPassword(RedisPassword.of(redisConfig.getPassword()));
        JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder().usePooling().
                poolConfig(jedisPoolConfig()).and().readTimeout(Duration.ofMillis(redisConfig.getMaxWaitMillis())).build();
        return new JedisConnectionFactory(configuration, jedisClientConfiguration);
    }

    @Bean
    public RedisStringCommands redisStringCommands(RedisConnectionFactory connectionFactory)
    {
        return connectionFactory.getConnection().stringCommands();
    }

    @Bean
    public StringBytesRedisTemplate stringBytesRedisTemplate(RedisConnectionFactory connectionFactory)
    {
        StringBytesRedisTemplate template = new StringBytesRedisTemplate();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }


    @Bean
    public RedisTemplate<String, Long> redisLongTemplate(RedisConnectionFactory connectionFactory)
    {
        RedisTemplate<String, Long> template = new RedisTemplate<String, Long>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(LongSerializer.getInstance());
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory)
    {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(connectionFactory);
        return stringRedisTemplate;
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig()
    {
        RedisConfig redisConfig = RedisConfig.getInstance();
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(redisConfig.getMaxTotal());
        jedisPoolConfig.setTestOnBorrow(redisConfig.getTestOnBorrow());
        jedisPoolConfig.setMaxIdle(redisConfig.getMaxIdle());
        jedisPoolConfig.setMinIdle(redisConfig.getMinIdle());
        jedisPoolConfig.setMaxWaitMillis(redisConfig.getMaxWaitMillis());
        return jedisPoolConfig;
    }
}