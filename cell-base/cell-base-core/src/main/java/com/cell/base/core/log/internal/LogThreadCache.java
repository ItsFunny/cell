package com.cell.base.core.log.internal;

import com.cell.base.core.log.ILogConsumer;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
public class LogThreadCache
{
    private Map<Long, Set<ILogConsumer>> logReceiverSetCacheByMask;
    private Map<Long, Set<ILogConsumer>> logReceiverSetCacheByKey;
    private int version;

    public Map<Long, Set<ILogConsumer>> getLogReceiverSetCacheByMask()
    {
        return logReceiverSetCacheByMask;
    }

    public void setLogReceiverSetCacheByMask(Map<Long, Set<ILogConsumer>> logReceiverSetCacheByMask)
    {
        this.logReceiverSetCacheByMask = logReceiverSetCacheByMask;
    }

    public Map<Long, Set<ILogConsumer>> getLogReceiverSetCacheByKey()
    {
        return logReceiverSetCacheByKey;
    }

    public void setLogReceiverSetCacheByKey(Map<Long, Set<ILogConsumer>> logReceiverSetCacheByKey)
    {
        this.logReceiverSetCacheByKey = logReceiverSetCacheByKey;
    }

    public int getVersion()
    {
        return version;
    }

    public void setVersion(int version)
    {
        this.version = version;
    }

    public LogThreadCache()
    {
        super();
        this.logReceiverSetCacheByKey = new HashMap<>();
        this.logReceiverSetCacheByMask = new HashMap<>();
    }
}
