package com.cell.log_origin;


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
    ALL((short) (1 << 0)),
    TRACE((short) (1 << 1)),
    DEBUG((short) (1 << 2)),
    INFO((short) (1 << 3)),
    WARN((short) (1 << 4)),
    ERROR((short) (1 << 5)),
    FATAL((short) (1 << 6)),
    OFF((short) (1 << 7));

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
        if (other==null)return true;
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
            enm = LogLevel.ALL;
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
