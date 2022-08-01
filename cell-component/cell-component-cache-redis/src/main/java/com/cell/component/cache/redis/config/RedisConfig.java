package com.cell.component.cache.redis.config;


import com.cell.node.core.context.INodeContext;
import com.cell.sdk.configuration.Configuration;
import lombok.Data;

import java.util.List;

@Data
public class RedisConfig
{
    private Boolean cluster;
    private String host;
    private Integer port;
    private String clientName;
    private String password;
    private String clusterPassword;
    private List<String> nodes;
    private Integer maxIdle;
    private Integer minIdle;
    private Integer maxTotal;
    private Long maxWaitMillis;
    private Boolean testOnBorrow;
    private Integer clusterPort;

    private static RedisConfig instance = new RedisConfig();

    private static final String module = "redis";

    public static RedisConfig getInstance(){
        return instance;
    }
    public void seal(INodeContext context) throws Exception
    {
        RedisConfig redis = Configuration.getDefault().getConfigValue(module).asObject(RedisConfig.class);
        instance = redis;
    }
}
