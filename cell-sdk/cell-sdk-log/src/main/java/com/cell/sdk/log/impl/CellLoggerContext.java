package com.cell.sdk.log.impl;

import com.cell.base.common.models.ModuleInterface;
import com.cell.base.common.utils.DebugUtil;
import com.cell.sdk.log.ILogHook;
import com.cell.sdk.log.LogEntry;
import com.cell.sdk.log.LogLevel;
import com.cell.sdk.log.LogTypeEnums;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-19 05:47
 */
public class CellLoggerContext
{
    private static final CellLoggerContext LOG_CONTEXT = new CellLoggerContext();
    final static char[] LOG_LEVEL_SIMPLE = {'T', 'D', 'I', 'W', 'E', 'F', 'O'};

    public static LogEntry createLogEntry(ModuleInterface module, String sequenceId, List<String> blackList, LogLevel logLevel, LogTypeEnums logTypeEnums, Throwable err, Optional<ILogHook> afterCreate, String formatMsg, Object... params)
    {
        LogEntry logEntry = LogEntryFactory.createLogEntry(module, sequenceId, blackList, logLevel, logTypeEnums, err, formatMsg, params);
        afterCreate.ifPresent(l -> l.hook(logEntry));
        return logEntry;
    }

    final static ThreadLocal<SimpleDateFormat> dateFormatter = new ThreadLocal<SimpleDateFormat>()
    {
        @Override
        protected SimpleDateFormat initialValue()
        {
            return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss,SSS");
        }

        ;
    };

    private static class LogEntryFactory
    {
        static LogEntry createLogEntry(ModuleInterface module, String sequencId, List<String> blackList, LogLevel logLevel, LogTypeEnums logTypeEnums, Throwable err, String msg, Object... data)
        {
            LogEntry logEntry = LogEntry.builder()
                    .logLevel(logLevel)
                    .module(module)
                    .logType(logTypeEnums.getCode())
                    .message(defaultLogFormat(true, logLevel, module, sequencId, blackList, msg, new Date(), err))
                    .objects(data)
                    .build();
            return logEntry;
        }

        public static String defaultLogFormat(boolean isDebug,
                                              LogLevel logLevel,
                                              ModuleInterface module,
                                              String sequenceId,
                                              List<String> blackList,
                                              String format,
                                              Date timeStamp, Throwable error)
        {
            StringBuilder sb = new StringBuilder();
            String dateString = formatDate(timeStamp);
            sb.append(dateString);
            sb.append(' ');
            if (isDebug)
            {
                sb.append(getCallerString(blackList));
                sb.append(' ');
            }
            if (module != null)
            {
                sb.append("[").append(module.name()).append("]");
            }
            sb.append('[').append(LOG_LEVEL_SIMPLE[logLevel.getValue()]).append(']');
//            sb.append(String.format(" sequenceId=%s ", sequenceId));
            sb.append(format);

            if (error != null)
            {
                sb.append(DebugUtil.exceptionStackTraceToString(error));
            }
            if (error != null)
            {
                sb.append(error);
            }
            return sb.toString();
        }

        private static String getCallerString(List<String> blackList)
        {
            StackTraceElement[] sts = Thread.currentThread().getStackTrace();
            for (int i = 1; i < sts.length; ++i)
            {
                StackTraceElement st = sts[i];
                boolean skip = false;
                for (String b : blackList)
                {
                    if (st.getClassName().contains(b))
                    {
                        skip = true;
                        break;
                    }
                }
                if (skip)
                {
                    continue;
                }
                String f = st.getFileName();
                int l = st.getLineNumber();
                StringBuilder sb = new StringBuilder(f.length() + 10);
                sb.append('(').append(f).append(':').append(l).append(')');
                return sb.toString();
            }
            return "";
        }

        public static String formatDate(Date date)
        {
            return dateFormatter.get().format(date);
        }
    }
}
