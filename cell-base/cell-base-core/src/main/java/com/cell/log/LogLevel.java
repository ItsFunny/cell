package com.cell.log;


import java.util.HashMap;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description 日志级别
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-14 05:58
 */
public enum LogLevel
{
    TRACE((short) (0)),
    DEBUG((short) (1)),
    INFO((short) (2)),
    WARN((short) (3)),
    ERROR((short) (4))
    ;

    private short value;

    LogLevel(short value)
    {
        this.value = value;
    }

    public boolean isBiggerOrEqual(LogLevel other)
    {
        return value >= other.getValue();
    }

    public boolean isBigger(LogLevel other)
    {
        return value > other.getValue();
    }

    public int getValue()
    {
        return value;
    }

    private final static Map<Integer, LogLevel> ENUM_MAP = new HashMap<>();

    static
    {
        registerErrorEnum(LogLevel.values());
    }

    public static LogLevel getLogType(int logType)
    {
        LogLevel enm = ENUM_MAP.get(logType);
        if (enm == null)
        {
        }
        return enm;
    }

    public static void registerErrorEnum(LogLevel[] enums)
    {
        if (enums != null)
        {
            for (LogLevel enm : enums)
            {
                int key = enm.getValue();
                LogLevel old = ENUM_MAP.put(key, enm);
            }
        }
    }

}
