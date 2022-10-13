package com.cell.base.common.models;


import java.util.HashMap;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-07-04 16:53
 */
public enum Module implements ModuleInterface
{
    ALL((short) 0, "全部模块"),
    LOG((short) 1, "日志模块"),
    COMMON((short) 10001, "公共模块"),
    NETWORK((short) 10002, "network"),
    RPC((short) 10003, "rpc"),
    THIRD_PARTY((short) 10004, "third party"),
    COMMONS_CONFIG((short) 10009, "commons config"),
    EXCEPTION_HANDLER((short) 10010,"exception"),
    CONTAINER_REGISTRY((short) 100, "container registry"),
    CONTAINER((short) 101, "container"),
    HTTP_FRAMEWORK((short) 102, "HTTP_FRAMEWORK"),
    MVC((short) 103, "MVC"),
    CONFIGURATION((short) 104, "CONFIGURATION"),
    DISCOVERY((short) 105, "DISCOVERY"),
    HTTP_GATEWAY((short) 106, "GATEWAY"),
    HTTP_DISPATCHER((short) 107, "HTTP_DISPATCHER"),
    HTTP_GATEWAY_SENTINEL((short) 108, "SENTINEL"),
    STATISTIC((short) 109, "STATISTIC"),
    MANAGER((short) 110, "MANAGER"),
    SD_PROMETHEUS((short) 111, "SD_PROMETHEIUS"),
    SHELL((short) 112, "SHELL"),
    RPC_FRAMEWORK((short) 113, "RPC_FRAMEWORK"),
    GRPC((short) 114, "GRPC"),
    GRPC_SERVER((short) 115, "GRPC_SERVER"),
    GRPC_CLIENT((short) 116, "GRPC_CLIENT"),
    HOOK((short) 117, "HOOK"),
    AOP((short) 118,"AOP"),
    ////////////
    RABBITMQ((short) 38, "rabbitmq"),
    KAFKA((short) 39, "kafka"),
    ZOO_KEEPER((short) 41, "zookeeper"),
    TOMCAT((short) 42, "tomcat"),
    SPRING((short) 43, "spring framwork"),
    NETTY((short) 44, "netty"),
    NETFLIX((short) 45, "netflix"),
    NOTIFICATION((short) 46, "notification"),
    MYBATIS((short) 47, "mybatis"),
    FABRIC_CLIENT((short) 200, "fabric client"),
    HTTP_CLIENT((short) 10006, "http client"),
    TASK_CA((short) 48, "CA task"),
    DRUID((short) 49, "druid"),
    ALIBABA((short) 50, "alibaba"),
    COMPONENT_TASK((short) 51, "task"),
    DYNAMIC_DAO((short) 52,"DYNAMIC_DAO"),
    WEB_3J((short) 53,"WEB_3J"),
    WORKER((short) 54,"WORKER"),
    MICROMETER((short) 10008, "micrometer"),
    UNKNOWN((short) 65535, "UNKNOWN"),
    ;
    private short moduleId;
    private String desc;

    Module(short moduleId, String desc)
    {
        this.moduleId = moduleId;
        this.desc = desc;
    }

    private final static Map<Short, Module> MODULE_ENUM_MAP = new HashMap<>();
    private static IllegalStateException duplicatedException = null;

    static
    {
        registerModuleEnum(Module.values());
    }

    public static Module getModule(short moduleId)
    {
        return MODULE_ENUM_MAP.get(moduleId);
    }

    public short getModuleId()
    {
        return moduleId;
    }

    public static Module getModule(short moduleId, Module def)
    {
        Module ret = MODULE_ENUM_MAP.get(moduleId);
        if (ret != null)
        {
            return ret;
        }
        return def;
    }

    public static void registerModuleEnum(Module[] origineModules)
    {
        if (origineModules != null)
        {
            for (Module module : origineModules)
            {
                Module old = MODULE_ENUM_MAP.put(module.getModuleId(), module);
                if (old != null)
                {
                    System.out.println("重复的Module:" + old + ", " + module);
                    duplicatedException = new IllegalStateException("重复的Module:" + old.name());
                    throw duplicatedException;
                }
            }
        }
    }

}
