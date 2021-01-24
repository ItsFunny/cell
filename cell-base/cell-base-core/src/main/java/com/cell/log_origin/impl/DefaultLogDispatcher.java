package com.cell.log_origin.impl;

import com.cell.enums.Bees;
import com.cell.log_origin.*;

/**
 * @author Charlie
 * @When
 * @Description 用于分发日志
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-08 23:03
 */
public class DefaultLogDispatcher implements ILogDispatcher
{
    private ILogPolicy logPolicy;
    // 日志级别,是以全局的为主,还是局部的为主
    private byte logLevelInternalOrGlobal;
    private LogLevel internalLogLevel = LogLevel.ALL;

    interface LogCoreType
    {
        byte GLOBAL = 0;
        byte INTERNAL = 1;
    }

    // 日志级别,先全局过滤,再局部过滤
    private static LogLevel globalLogLevel = LogLevel.ALL;
    private static final DefaultLogDispatcher INSTANCE = new DefaultLogDispatcher();

    public static DefaultLogDispatcher getInstance()
    {
        return INSTANCE;
    }

    public static void info(Bees bee, String msg, Object... data)
    {

    }


    // 直接本地先通过 logLevel,logType过滤 ,logType:指的是日志类型,为业务日志,还是系统日志还是第三方日志
    private static void dispatchFirst(Throwable error, Bees bee, LogLevel logLevel, LogTypeEnums logTypeEnums, String sequenceId, String formatStr, Object ...objects)
    {
        LogLevel compareLevel = globalLogLevel;
        if (INSTANCE.logLevelInternalOrGlobal == LogCoreType.INTERNAL)
        {
            compareLevel = INSTANCE.internalLogLevel;
        }
        if (compareLevel.isBigger(logLevel)) return;

        // 封装成logInfo,然后给policy进行分发
        LogInfo info = LogInfo.builder()
                .error(error)
                .beeId(bee)
                .format(formatStr)
                .objects(objects)
                .sequenceId(sequenceId)
                .logTypeEnums(logTypeEnums)
                .build();
        INSTANCE.dispatch(info);
    }


    @Override
    public void dispatch(LogInfo info)
    {

    }
}
