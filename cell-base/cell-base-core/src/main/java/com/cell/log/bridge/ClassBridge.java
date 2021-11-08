package com.cell.log.bridge;


import com.cell.base.common.models.Module;

import java.util.HashMap;
import java.util.Map;


public class ClassBridge
{
    public static Map<String, Module> moduleMap = new HashMap<>();

    static
    {
//        moduleMap.put("com.cell", Module.COMMON);
        moduleMap.put("org.apache.kafka", Module.KAFKA);
        moduleMap.put("org.apache.zookeeper", Module.ZOO_KEEPER);
        moduleMap.put("org.apache.tomcat", Module.TOMCAT);
        moduleMap.put("org.apache.catalina", Module.TOMCAT);
        moduleMap.put("org.springframework", Module.SPRING);
        moduleMap.put("com.rabbitmq", Module.RABBITMQ);
        moduleMap.put("io.netty", Module.NETTY);
        moduleMap.put("com.netflix", Module.NETFLIX);
        moduleMap.put("grpc-netty", Module.NETTY);
        moduleMap.put("com.baomidou.mybatisplus", Module.MYBATIS);
        moduleMap.put("org.mybatis", Module.MYBATIS);
        moduleMap.put("org.hyperledger", Module.FABRIC_CLIENT);
        moduleMap.put("org.apache.http", Module.HTTP_CLIENT);
        moduleMap.put("com.cell", Module.COMMON);
        moduleMap.put("org.apache.ibatis", Module.NETTY);
        moduleMap.put("com.alibaba.druid", Module.DRUID);
        moduleMap.put("io.micrometer", Module.MICROMETER);
        moduleMap.put("io.grpc.netty", Module.NETTY);
        moduleMap.put("com.alibaba.cloud", Module.ALIBABA);
        moduleMap.put("org.apache.commons.configuration", Module.COMMONS_CONFIG);
    }

    public static final Module moduleFromClass(String className)
    {
        for (Map.Entry<String, Module> entry : moduleMap.entrySet())
        {
            if (className.startsWith(entry.getKey()))
            {
                return entry.getValue();
            }
        }
        return Module.COMMON;
    }
}
