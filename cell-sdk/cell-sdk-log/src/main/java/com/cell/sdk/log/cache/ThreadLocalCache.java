package com.cell.sdk.log.cache;

// TODO: fast threadlocal
public class ThreadLocalCache
{
    private final ThreadLocal<String> sequenceCache = new ThreadLocal<>();
    public static final ThreadLocalCache instance = new ThreadLocalCache();

    public static ThreadLocalCache getInstance()
    {
        return instance;
    }

    public static String getCurrentSequenceId()
    {
        return instance.sequenceCache.get();
    }

    public static void setCurrentSequenceId(String sequenceId)
    {
        instance.sequenceCache.set(sequenceId);
    }

    public static void cleanCurrentSequenceId()
    {
        instance.sequenceCache.remove();
    }

}
