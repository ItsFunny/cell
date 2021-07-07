package com.cell.models;

import cn.tass.math.raw.Mod;

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
public enum Module
{
    ALL((short) 0, "全部模块"),
    COMMON((short) 10001, "公共模块"),
    NETWORK((short) 10002, "network"),
    RPC((short) 10003, "rpc"),
    THIRD_PARTY((short) 10004, "third party"),
    COMMONS_CONFIG((short) 10009, "commons config"),

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
    TASK_CA((short) 48, "CA task"),
    DRUID((short) 49, "druid"),


    ;
    private short moduleId;
    private String desc;

    private Module(short moduleId, String desc)
    {
        this.moduleId = moduleId;
        this.desc = desc;
    }

    private final static Map<Short, Module> MODULE_ENUM_MAP = new HashMap<>();
    private static IllegalStateException duplicatedException = null;

    static {
        registerModuleEnum(Module.values());
    }
    /**
     * 描述:这个方法是根据模块id获取一个模块枚举
     *
     * @return
     * @author Terry
     * @time 2016年7月13日-上午10:34:28
     */
    public static Module getModule(short moduleId) {
        return MODULE_ENUM_MAP.get(moduleId);
    }

    public short getModuleId() {
        return moduleId;
    }

    public static Module getModule(short moduleId, Module def) {
        Module ret = MODULE_ENUM_MAP.get(moduleId);
        if (ret != null) {
            return ret;
        }
        return def;
    }
    public static void registerModuleEnum(Module[] origineModules) {
        if (origineModules != null) {
            for (Module module : origineModules) {
                Module old = MODULE_ENUM_MAP.put(module.getModuleId(), module);
                if (old != null) {
                    System.out.println("重复的Module:" + old + ", " + module);
                    duplicatedException = new IllegalStateException("重复的Module:" + old.name());
                    throw duplicatedException;
                }
            }
        }
    }

}
